package pl.jozefniemiec.langninja.data.repository.firebase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import pl.jozefniemiec.langninja.data.repository.UserSentenceRepository;
import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;

public class UserSentenceRepositoryImpl implements UserSentenceRepository {

    private static final String TAG = UserSentenceRepositoryImpl.class.getSimpleName();

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference sentenceReference = firebaseDatabase.getReference("sentence");
    private DatabaseReference userReference = firebaseDatabase.getReference("users");
    private DatabaseReference publicSentenceReference = firebaseDatabase.getReference("sentence_public_list");
    private DatabaseReference publicSentencesReference = firebaseDatabase.getReference("public_sentences");


    @Inject
    public UserSentenceRepositoryImpl() {
        publicSentenceReference.keepSynced(true);
        sentenceReference.keepSynced(true);
    }

    @Override
    public void insertByUserUid(String userUid, UserSentence sentenceCandidate) {
        sentenceReference.child(userUid).push().setValue(sentenceCandidate);
    }

    @Override
    public void insert(UserSentence sentenceCandidate) {
        sentenceReference.push().setValue(sentenceCandidate);
    }

    @Override
    public void insertPublic(UserSentence userSentence) {
        publicSentencesReference.push().setValue(userSentence);
    }
    public Single<List<UserSentence>> getUserListSentences(String uid) {
        return Single.create(subscriber ->
                sentenceReference
                        .orderByChild("createdBy")
                        .startAt(uid).endAt(uid)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                ArrayList<UserSentence> list = new ArrayList<>();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    list.add(snapshot.getValue(UserSentence.class));
                                }
                                subscriber.onSuccess(list);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        }));
    }


    public Single<UserSentence> getSentence(String key) {
        return Single.create(subscriber ->
                sentenceReference
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

    public Observable<UserSentence> getPublicSentences() {
        return Observable.create(subscriber ->
                publicSentencesReference
                        .addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                subscriber.onNext(dataSnapshot.getValue(UserSentence.class));
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
                        }));
    }

    public Observable<UserSentence> getUserSentences(String uid) {
        return Observable.create(subscriber ->
                sentenceReference
                        .orderByChild("createdBy")
                        .startAt(uid).endAt(uid)
                        .addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                if (dataSnapshot.exists()) {
                                    subscriber.onNext(dataSnapshot.getValue(UserSentence.class));
                                } else {
                                    subscriber.onError(new RuntimeException("User " + uid + " data does not exist"));
                                }
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
                        }));
    }
}
