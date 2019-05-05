package pl.jozefniemiec.langninja.data.repository.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import pl.jozefniemiec.langninja.data.repository.CommentsRepository;
import pl.jozefniemiec.langninja.data.repository.NoInternetConnectionException;
import pl.jozefniemiec.langninja.data.repository.firebase.model.Comment;
import pl.jozefniemiec.langninja.data.repository.firebase.model.Likes;
import pl.jozefniemiec.langninja.service.InternetConnectionService;

public class CommentsRepositoryImpl implements CommentsRepository {

    private static final String TAG = CommentsRepositoryImpl.class.getSimpleName();
    private static final String COMMENTS_BY_SENTENCE_NODE = "comments_by_sentence";
    private static final String COMMENTS_NODE = "comments";
    private static final String LIKES_NODE = "comment_likes";
    private static final String COMMENTS_COUNT_NODE = "comment_count";
    private static final String COMMENT_KEY_PATH = "/" + COMMENTS_NODE + "/%s";
    private static final String COMMENT_BY_SENTENCE_KEY_PATH = "/" + COMMENTS_BY_SENTENCE_NODE + "/%s/%s/";
    private static final String COMMENT_COUNT_PATH = "/%s/commentsCount/";
    private static final String LIKES_PATH = "/" + LIKES_NODE + "/%s/";
    private static final String COMMENT_BY_SENTENCE_COUNT_LIKES_PATH = "/" + COMMENTS_BY_SENTENCE_NODE + "/%s/%s/likesCount/";
    private static final String COMMENT_COUNT_LIKES_PATH = "/" + COMMENTS_NODE + "/%s/likesCount/";
    private static final int EMPTY_LIST_VALUE = 0;
    private static final String COMMENTS_COUNT_FIELD = "commentsCount";
    private static final String CONTENT_FIELD = "content";
    private final InternetConnectionService internetConnectionService;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference commentsBySentenceReference =
            firebaseDatabase.getReference(COMMENTS_BY_SENTENCE_NODE);
    private DatabaseReference commentsReference =
            firebaseDatabase.getReference(COMMENTS_NODE);
    private DatabaseReference likesReference =
            firebaseDatabase.getReference(LIKES_NODE);
    private DatabaseReference commentCountReference = firebaseDatabase.getReference(COMMENTS_COUNT_NODE);

    @Inject
    CommentsRepositoryImpl(InternetConnectionService internetConnectionService) {
        this.internetConnectionService = internetConnectionService;
        commentsBySentenceReference.keepSynced(true);
        likesReference.keepSynced(true);
    }

    @Override
    public Single<String> insert(Comment comment) {
        return Single.create(subscriber -> {
            if (!internetConnectionService.isInternetOn()) {
                subscriber.onError(new NoInternetConnectionException());
                return;
            }
            String commentKey = Objects.requireNonNull(commentsBySentenceReference.push().getKey());
            databaseReference.updateChildren(generateUpdateMap(commentKey, comment))
                    .addOnSuccessListener(aVoid -> {
                        commentsBySentenceReference
                                .child(comment.getSentenceId())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        commentCountReference
                                                .child(String.format(COMMENT_COUNT_PATH, comment.getSentenceId()))
                                                .setValue(dataSnapshot.getChildrenCount());
                                        subscriber.onSuccess(commentKey);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    })
                    .addOnFailureListener(e -> subscriber.onError(new RuntimeException(e.getMessage())));
        });
    }

    private Map<String, Object> generateUpdateMap(String commentKey, Comment comment) {
        Map<String, Object> updateMap = new HashMap<>();
        String commentWithKeyPath = String.format(COMMENT_BY_SENTENCE_KEY_PATH, comment.getSentenceId(), commentKey);
        updateMap.put(commentWithKeyPath, comment);
        String commentPath = String.format(COMMENT_KEY_PATH, commentKey);
        updateMap.put(commentPath, comment);
        String commentLikesWithKeyPath = String.format(LIKES_PATH, commentKey);
        updateMap.put(commentLikesWithKeyPath, comment.getLikes() == null ? new Likes() : comment.getLikes());
        return updateMap;
    }

    private Map<String, Object> generateDeleteUpdateMap(String commentKey, Comment comment) {
        Map<String, Object> updateMap = generateUpdateMap(commentKey, comment);
        resetAllMapValuesToNull(updateMap);
        return updateMap;
    }

    private void resetAllMapValuesToNull(Map<String, Object> map) {
        for (Map.Entry<String, Object> item : map.entrySet()) {
            item.setValue(null);
        }
    }

    @Override
    public Single<List<Comment>> getCommentsBySentenceId(String sentenceId) {
        Single<List<Comment>> result = Single.create(subscriber -> {
            if (!internetConnectionService.isInternetOn()) {
                subscriber.onError(new NoInternetConnectionException());
                return;
            }
            commentsBySentenceReference
                    .child(sentenceId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                ArrayList<Comment> result = new ArrayList<>();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Comment comment = Objects.requireNonNull(snapshot.getValue(Comment.class));
                                    comment.setId(snapshot.getKey());
                                    result.add(comment);
                                }
                                subscriber.onSuccess(result);
                            } else {
                                subscriber
                                        .onError(new RuntimeException(
                                                //TODO
                                        ));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            subscriber
                                    .onError(new RuntimeException(databaseError.getMessage()));
                        }
                    });
        });
        return result
                .toObservable()
                .flatMapIterable(comments -> comments)
                .flatMap(comment ->
                                 getLikes(comment.getId())
                                         .toObservable()
                                         .map(likes -> {
                                             comment.setLikes(likes);
                                             return comment;
                                         }))
                .toList();
    }

    @Override
    public Single<Comment> getComment(String commentId) {
        Single<Comment> result = Single.create(subscriber -> {
            commentsReference
                    .child(commentId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Comment comment = dataSnapshot.getValue(Comment.class);
                                comment.setId(dataSnapshot.getKey());
                                subscriber.onSuccess(comment);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            subscriber.onError(databaseError.toException());
                        }
                    });
        });
        return result
                .flatMap(comment -> getLikes(comment.getId())
                        .map(likes -> {
                            comment.setLikes(likes);
                            return comment;
                        })
                );
    }

    @Override
    public Single<Comment> like(Comment comment, String userUid) {
        return saveLikeOrDislike(true, comment, userUid);
    }

    @Override
    public Single<Comment> dislike(Comment comment, String userUid) {
        return saveLikeOrDislike(false, comment, userUid);
    }

    private Single<Comment> saveLikeOrDislike(boolean isLike, Comment comment, String userUid) {
        return Single.create(subscriber -> {
            if (!internetConnectionService.isInternetOn()) {
                subscriber.onError(new NoInternetConnectionException());
                return;
            }
            likesReference
                    .child(comment.getId())
                    .runTransaction(new Transaction.Handler() {
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                            Likes likes = mutableData.getValue(Likes.class);
                            if (likes == null) {
                                return Transaction.success(mutableData);
                            }
                            Likes result = isLike ? calculateLikes(likes, userUid) : calculateDislikes(likes, userUid);
                            comment.setLikes(result);
                            mutableData.setValue(result);
                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                int count = Objects.requireNonNull(dataSnapshot.getValue(Likes.class)).getCount();
                                Map<String, Object> updateMap = new HashMap<>();
                                String commentBySentenceLikesCountPath = String.format(COMMENT_BY_SENTENCE_COUNT_LIKES_PATH, comment.getSentenceId(), comment.getId());
                                updateMap.put(commentBySentenceLikesCountPath, count);
                                String commentLikesCountPath = String.format(COMMENT_COUNT_LIKES_PATH, comment.getId());
                                updateMap.put(commentLikesCountPath, count);
                                databaseReference
                                        .updateChildren(updateMap)
                                        .addOnSuccessListener(aVoid -> subscriber.onSuccess(comment))
                                        .addOnFailureListener(subscriber::onError);
                            } else {
                                subscriber.onError(databaseError.toException());
                            }
                        }
                    });
        });
    }

    private Likes calculateDislikes(Likes likes, String userUid) {
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
        return likes;
    }

    private Likes calculateLikes(Likes likes, String userUid) {
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
        return likes;
    }

    @Override
    public Single<Likes> getLikes(String commentId) {
        return Single.create(subscriber -> {
            likesReference
                    .child(commentId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                subscriber.onSuccess(dataSnapshot.getValue(Likes.class));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            subscriber.onError(databaseError.toException());
                        }
                    });
        });
    }

    @Override
    public Completable update(Comment comment) {
        return Completable.create(subscriber ->
                                          commentsBySentenceReference
                                                  .child(comment.getId())
                                                  .child(CONTENT_FIELD)
                                                  .setValue(comment.getContent())
                                                  .addOnSuccessListener(aVoid -> subscriber.onComplete())
                                                  .addOnFailureListener(subscriber::onError)
        );
    }

    @Override
    public Completable remove(Comment comment) {
        return Completable.create(subscriber -> {
            String commentKey = comment.getId();
            databaseReference.updateChildren(generateDeleteUpdateMap(commentKey, comment))
                    .addOnSuccessListener(aVoid -> {
                        commentsBySentenceReference
                                .child(comment.getSentenceId())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        commentCountReference
                                                .child(String.format(COMMENT_COUNT_PATH, comment.getSentenceId()))
                                                .setValue(dataSnapshot.getChildrenCount());
                                        subscriber.onComplete();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    })
                    .addOnFailureListener(e -> subscriber.onError(new RuntimeException(e.getMessage())));
        });
    }

    @Override
    public Single<Integer> getCommentsCount(String commentId) {
        return Single.create(subscriber -> {
            commentCountReference
                    .child(commentId)
                    .child(COMMENTS_COUNT_FIELD)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                subscriber.onSuccess(dataSnapshot.getValue(Integer.class));
                            } else {
                                subscriber.onSuccess(EMPTY_LIST_VALUE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            subscriber.onError(databaseError.toException());
                        }
                    });
        });
    }
}
