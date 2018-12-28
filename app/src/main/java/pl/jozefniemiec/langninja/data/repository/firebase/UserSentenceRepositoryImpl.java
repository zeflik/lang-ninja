package pl.jozefniemiec.langninja.data.repository.firebase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import pl.jozefniemiec.langninja.data.repository.UserSentenceRepository;
import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;
import pl.jozefniemiec.langninja.ui.base.Constants;

public class UserSentenceRepositoryImpl implements UserSentenceRepository {

    private static final String TAG = UserSentenceRepositoryImpl.class.getSimpleName();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference publicSentencesByLanguageReference = firebaseDatabase.getReference("public_sentences_by_lang");
    private DatabaseReference publicSentencesReference = firebaseDatabase.getReference("public_sentences");

    @Inject
    public UserSentenceRepositoryImpl() {
        publicSentencesReference.keepSynced(true);
    }

    @Override
    public void insert(UserSentence userSentence) {
        String userSentenceKey = Objects.requireNonNull(
                publicSentencesReference.push().getKey()
        );
        publicSentencesReference
                .child(userSentenceKey)
                .setValue(userSentence);
        publicSentencesByLanguageReference
                .child(userSentence.getLanguageCode())
                .child(userSentenceKey)
                .setValue(userSentence);
    }

    public Single<UserSentence> getSentence(String key) {
        return Single.create(subscriber ->
                publicSentencesReference
                        .child(key)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                subscriber.onSuccess(dataSnapshot.getValue(UserSentence.class));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        }));
    }


    public Observable<UserSentence> getSentences() {
        return getPublicSentencesByLanguageAndChildValue(Constants.DEFAULT_LANG_KEY, "dateCreated", null);
    }

    public Observable<UserSentence> getSentencesByLanguage(String languageCode) {
        return getPublicSentencesByLanguageAndChildValue(languageCode, "dateCreated", null);
    }

    public Observable<UserSentence> getUserSentences(String uid) {
        return getPublicSentencesByLanguageAndChildValue(Constants.DEFAULT_LANG_KEY, "author/uid", uid);
    }

    public Observable<UserSentence> getUserSentencesByLanguage(String uid, String languageCode) {
        return getPublicSentencesByLanguageAndChildValue(languageCode, "author/uid", uid);
    }

    private Observable<UserSentence> getPublicSentencesByLanguageAndChildValue(String languageCode, String childKey, String childValue) {
        return Observable.create(subscriber -> {
            DatabaseReference dbReference;
            if (languageCode.equals(Constants.DEFAULT_LANG_KEY)) {
                dbReference = publicSentencesReference;

            } else {
                dbReference = publicSentencesByLanguageReference.child(languageCode);
            }

            Query query = dbReference.orderByChild(childKey);

            if (childValue != null)
                query = query
                        .equalTo(childValue);
            query
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            UserSentence userSentence =
                                    Objects.requireNonNull(dataSnapshot.getValue(UserSentence.class));
                            userSentence.setId(dataSnapshot.getKey());
                            subscriber.onNext(userSentence);
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        });
    }
}

