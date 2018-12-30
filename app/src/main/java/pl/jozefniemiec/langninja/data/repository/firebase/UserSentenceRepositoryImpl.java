package pl.jozefniemiec.langninja.data.repository.firebase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import pl.jozefniemiec.langninja.data.repository.UserSentenceRepository;
import pl.jozefniemiec.langninja.data.repository.firebase.model.Likes;
import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;
import pl.jozefniemiec.langninja.ui.base.Constants;

public class UserSentenceRepositoryImpl implements UserSentenceRepository {

    private static final String TAG = UserSentenceRepositoryImpl.class.getSimpleName();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference publicSentencesByLanguageReference = firebaseDatabase.getReference("public_sentences_by_lang");
    private DatabaseReference publicSentencesReference = firebaseDatabase.getReference("public_sentences");
    private DatabaseReference dbReference;
    private ChildEventListener childEventListener;

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

    public Observable<UserSentence> getSentence(String key) {
        Log.d(TAG, "getSentence: " + key);
        return Observable.create(
                subscriber ->
                        publicSentencesReference
                                .child(key)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Log.d(TAG, "onDataChange: " + dataSnapshot.getValue());
                                        subscriber.onNext(dataSnapshot.getValue(UserSentence.class));
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Log.d(TAG, "onCancelled: " + databaseError.getMessage());
                                    }
                                })
        );
    }

    @Override
    public Completable like(String sentenceKey, String userUid) {
        return Completable.create(subscriber -> {
            DatabaseReference likesReference = publicSentencesReference
                    .child(sentenceKey)
                    .child("likes");
            likesReference.runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    Likes likes = mutableData.getValue(Likes.class);
                    if (likes == null) {
                        return Transaction.success(mutableData);
                    }
                    if (likes.getLikesMap().containsKey(userUid)) {
                        likes.setCount(likes.getCount() - 1);
                        likes.getLikesMap().remove(userUid);
                    } else if (likes.getDislikesMap().containsKey(userUid)) {
                        likes.setCount(likes.getCount() + 2);
                        likes.getDislikesMap().remove(userUid);
                        likes.getLikesMap().put(userUid, true);
                    } else {
                        likes.setCount(likes.getCount() + 1);
                        likes.getLikesMap().put(userUid, true);
                    }
                    mutableData.setValue(likes);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b,
                                       DataSnapshot dataSnapshot) {
                    subscriber.onComplete();
                    Log.d(TAG, "LikesTransaction:onComplete:" + databaseError);
                }
            });
        });
    }

    @Override
    public Completable dislike(String sentenceKey, String userUid) {
        return Completable.create(subscriber -> {
            DatabaseReference likesReference = publicSentencesReference
                    .child(sentenceKey)
                    .child("likes");
            likesReference.runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    Likes likes = mutableData.getValue(Likes.class);
                    if (likes == null) {
                        return Transaction.success(mutableData);
                    }
                    if (likes.getDislikesMap().containsKey(userUid)) {
                        likes.setCount(likes.getCount() + 1);
                        likes.getDislikesMap().remove(userUid);
                    } else if (likes.getLikesMap().containsKey(userUid)) {
                        likes.setCount(likes.getCount() - 2);
                        likes.getLikesMap().remove(userUid);
                        likes.getDislikesMap().put(userUid, true);
                    } else {
                        likes.setCount(likes.getCount() - 1);
                        likes.getDislikesMap().put(userUid, true);
                    }
                    mutableData.setValue(likes);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b,
                                       DataSnapshot dataSnapshot) {
                    subscriber.onComplete();
                    Log.d(TAG, "Dislike Transaction:onComplete:" + databaseError);
                }
            });
        });
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
        return Observable.create(
                subscriber -> {
                    if (languageCode.equals(Constants.DEFAULT_LANG_KEY)) {
                        dbReference = publicSentencesReference;
                    } else {
                        dbReference = publicSentencesByLanguageReference.child(languageCode);
                    }
                    Query query = dbReference.orderByChild(childKey);
                    if (childValue != null) {
                        query = query.equalTo(childValue);
                    }
                    childEventListener = query
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

    @Override
    public void dispose() {
        if (dbReference != null && childEventListener != null) {
            dbReference.removeEventListener(childEventListener);
        }
    }
}

