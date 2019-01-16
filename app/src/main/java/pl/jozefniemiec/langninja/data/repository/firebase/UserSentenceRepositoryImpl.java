package pl.jozefniemiec.langninja.data.repository.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import pl.jozefniemiec.langninja.data.repository.UserSentenceRepository;
import pl.jozefniemiec.langninja.data.repository.firebase.model.Likes;
import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;
import pl.jozefniemiec.langninja.ui.base.Constants;

public class UserSentenceRepositoryImpl implements UserSentenceRepository {

    private static final String TAG = UserSentenceRepositoryImpl.class.getSimpleName();
    private static final String SENTENCES_NODE = "public_sentences";
    private static final String SENTENCES_BY_LANG_NODE = "public_sentences_by_lang";
    private static final String LIKES_NODE = "likes";
    private static final String SENTENCE_BY_KEY_PATH = "/" + SENTENCES_NODE + "/%s/";
    private static final String SENTENCE_BY_LANG_KEY_PATH = "/" + SENTENCES_BY_LANG_NODE + "/%s/%s";
    private static final String SENTENCE_LIKES_COUNT_PATH = SENTENCE_BY_KEY_PATH + "/likesCount/";
    private static final String SENTENCE_BY_LANG_LIKES_COUNT_PATH = SENTENCE_BY_LANG_KEY_PATH + "/likesCount/";
    private static final String LIKES_PATH = "/" + LIKES_NODE + "/%s/";
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference publicSentencesByLanguageReference =
            firebaseDatabase.getReference(SENTENCES_BY_LANG_NODE);
    private DatabaseReference publicSentencesReference =
            firebaseDatabase.getReference(SENTENCES_NODE);
    private DatabaseReference likesReference =
            firebaseDatabase.getReference(LIKES_NODE);
    private DatabaseReference dbReference;
    private Map<DatabaseReference, ValueEventListener> valueEventListenersMap = new HashMap<>();

    @Inject
    UserSentenceRepositoryImpl() {
        publicSentencesReference.keepSynced(true);
    }

    @Override
    public Single<String> insert(UserSentence userSentence) {
        return Single.create(subscriber -> {
            String userSentenceKey = Objects.requireNonNull(publicSentencesReference.push().getKey());
            Map<String, Object> updateMap = new HashMap<>();
            String sentenceWithKeyPath = String.format(SENTENCE_BY_KEY_PATH, userSentenceKey);
            updateMap.put(sentenceWithKeyPath, userSentence);
            String sentenceWithLangAndKeyPath =
                    String.format(SENTENCE_BY_LANG_KEY_PATH,
                                  userSentence.getLanguageCode(),
                                  userSentenceKey);
            updateMap.put(sentenceWithLangAndKeyPath, userSentence);
            String sentenceLikesWithKeyPath = String.format(LIKES_PATH, userSentenceKey);
            updateMap.put(sentenceLikesWithKeyPath, new Likes());
            databaseReference.updateChildren(updateMap)
                    .addOnSuccessListener(aVoid -> subscriber.onSuccess(userSentenceKey))
                    .addOnFailureListener(e -> subscriber.onError(new RuntimeException(e.getMessage())));
        });
    }

    @Override
    public Single<List<UserSentence>> getUserSentencesByLanguage(String uid, String languageCode) {
        return getPublicSentencesByLanguageAndChildValueOnce(languageCode, "author/uid", uid);
    }

    @Override
    public Single<List<UserSentence>> getSentencesByLanguage(String languageCode) {
        return getPublicSentencesByLanguageAndChildValueOnce(languageCode, "dateCreated", null);
    }

    public Observable<UserSentence> getSentence(String key) {
        return Observable.create(
                subscriber -> {
                    DatabaseReference databaseReference = publicSentencesReference.child(key);
                    ValueEventListener listener = databaseReference
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        UserSentence userSentence = Objects.requireNonNull(
                                                dataSnapshot.getValue(UserSentence.class)
                                        );
                                        userSentence.setId(dataSnapshot.getKey());
                                        subscriber.onNext(userSentence);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.d(TAG, "onCancelled: " + databaseError.getMessage());
                                }
                            });
                    valueEventListenersMap.put(databaseReference, listener);
                }
        );
    }

    public Observable<Likes> getLikes(String key) {
        return Observable.create(
                subscriber -> {
                    DatabaseReference databaseReference = likesReference.child(key);
                    ValueEventListener listener = databaseReference
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Likes likes = dataSnapshot.getValue(Likes.class);
                                        subscriber.onNext(likes);
                                    } else {
                                        Log.d(TAG, "getLikes: dataSnapshot is empty");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError likesDataSnapshot) {
                                    Log.d(TAG, "getLikes: " + likesDataSnapshot.getMessage());
                                }
                            });
                    valueEventListenersMap.put(databaseReference, listener);
                }
        );
    }

    @Override
    public Completable remove(UserSentence userSentence) {
        return Completable.create(subscriber -> {
            Map<String, Object> updateMap = new HashMap<>();
            String sentenceWithKeyPath = String.format(SENTENCE_BY_KEY_PATH, userSentence.getId());
            updateMap.put(sentenceWithKeyPath, null);
            String sentenceWithLangAndKeyPath =
                    String.format(SENTENCE_BY_LANG_KEY_PATH,
                                  userSentence.getLanguageCode(),
                                  userSentence.getId());
            updateMap.put(sentenceWithLangAndKeyPath, null);
            String sentenceLikesWithKeyPath = String.format(LIKES_PATH, userSentence.getId());
            updateMap.put(sentenceLikesWithKeyPath, null);
            databaseReference.updateChildren(updateMap)
                    .addOnSuccessListener(aVoid -> subscriber.onComplete())
                    .addOnFailureListener(e -> subscriber.onError(new RuntimeException(e.getMessage())));
        });
    }

    @Override
    public Completable like(String sentenceKey, String languageCode, String userUid) {
        return Completable.create(subscriber -> {
            likesReference
                    .child(sentenceKey)
                    .runTransaction(new Transaction.Handler() {
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                            Likes likes = mutableData.getValue(Likes.class);
                            if (likes == null) {
                                return Transaction.success(mutableData);
                            }
                            if (likes.getLikesMap().containsKey(userUid) && likes.getLikesMap().get(userUid)) {
                                likes.setCount(likes.getCount() - 1);
                                likes.getLikesMap().put(userUid, false);
                            } else if (likes.getDislikesMap().containsKey(userUid) && likes.getDislikesMap().get(userUid)) {
                                likes.setCount(likes.getCount() + 2);
                                likes.getDislikesMap().put(userUid, false);
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
                            if (dataSnapshot.exists()) {
                                int count = Objects.requireNonNull(dataSnapshot.getValue(Likes.class)).getCount();
                                Map<String, Object> updateMap = new HashMap<>();
                                String sentenceCountPath = String.format(SENTENCE_LIKES_COUNT_PATH, sentenceKey);
                                updateMap.put(sentenceCountPath, count);
                                String sentenceByLangCountPath = String.format(SENTENCE_BY_LANG_LIKES_COUNT_PATH, languageCode, sentenceKey);
                                updateMap.put(sentenceByLangCountPath, count);
                                databaseReference
                                        .updateChildren(updateMap)
                                        .addOnSuccessListener(aVoid -> subscriber.onComplete())
                                        .addOnFailureListener(subscriber::onError);
                            }
                        }
                    });
        });
    }

    @Override
    public Completable dislike(String sentenceKey, String languageCode, String userUid) {
        return Completable.create(
                subscriber ->
                        likesReference
                                .child(sentenceKey)
                                .runTransaction(new Transaction.Handler() {
                                    @NonNull
                                    @Override
                                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                        Likes likes = mutableData.getValue(Likes.class);
                                        if (likes == null) {
                                            return Transaction.success(mutableData);
                                        }
                                        if (likes.getDislikesMap().containsKey(userUid)
                                                && likes.getDislikesMap().get(userUid)) {
                                            likes.setCount(likes.getCount() + 1);
                                            likes.getDislikesMap().put(userUid, false);
                                        } else if (likes.getLikesMap().containsKey(userUid)
                                                && likes.getLikesMap().get(userUid)) {
                                            likes.setCount(likes.getCount() - 2);
                                            likes.getLikesMap().put(userUid, false);
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
                                        if (dataSnapshot.exists()) {
                                            int count = Objects.requireNonNull(dataSnapshot.getValue(Likes.class)).getCount();
                                            Map<String, Object> updateMap = new HashMap<>();
                                            String sentenceCountPath = String.format(SENTENCE_LIKES_COUNT_PATH, sentenceKey);
                                            updateMap.put(sentenceCountPath, count);
                                            String sentenceByLangCountPath = String.format(SENTENCE_BY_LANG_LIKES_COUNT_PATH, languageCode, sentenceKey);
                                            updateMap.put(sentenceByLangCountPath, count);
                                            databaseReference
                                                    .updateChildren(updateMap)
                                                    .addOnSuccessListener(aVoid -> subscriber.onComplete())
                                                    .addOnFailureListener(subscriber::onError);
                                        }
                                    }
                                }));
    }

    @Override
    public void dispose() {
        for (Map.Entry<DatabaseReference, ValueEventListener> reference : valueEventListenersMap.entrySet()) {
            reference.getKey().removeEventListener(reference.getValue());
        }
        valueEventListenersMap.clear();
    }

    private Single<List<UserSentence>> getPublicSentencesByLanguageAndChildValueOnce(String languageCode, String childKey, String childValue) {
        return Single.create(
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
                    query
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    ArrayList<UserSentence> result = new ArrayList<>();
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        UserSentence userSentence = Objects.requireNonNull(snapshot.getValue(UserSentence.class));
                                        userSentence.setId(snapshot.getKey());
                                        result.add(userSentence);
                                    }
                                    subscriber.onSuccess(result);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                });
    }
}

