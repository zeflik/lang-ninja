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
import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;
import pl.jozefniemiec.langninja.service.InternetConnectionService;

public class CommentsRepositoryImpl implements CommentsRepository {

    private static final String TAG = CommentsRepositoryImpl.class.getSimpleName();
    private static final String COMMENTS_NODE = "comments";
    private static final String LIKES_NODE = "comment_likes";
    private static final String COMMENT_BY_KEY_PATH = "/" + COMMENTS_NODE + "/%s/%s/";
    private static final String COMMENT_LIKES_PATH = "/" + LIKES_NODE + "/%s/";
    private static final String COMMENT_COUNT_LIKES_PATH = "/" + COMMENTS_NODE + "/%s/%s/likesCount/";
    private final InternetConnectionService internetConnectionService;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference commentsReference =
            firebaseDatabase.getReference(COMMENTS_NODE);
    private DatabaseReference likesReference =
            firebaseDatabase.getReference(LIKES_NODE);

    @Inject
    CommentsRepositoryImpl(InternetConnectionService internetConnectionService) {
        this.internetConnectionService = internetConnectionService;
        commentsReference.keepSynced(true);
        likesReference.keepSynced(true);
    }

    @Override
    public Single<String> insert(Comment comment) {
        return Single.create(subscriber -> {
            if (!internetConnectionService.isInternetOn()) {
                subscriber.onError(new NoInternetConnectionException());
                return;
            }
            String commentKey = Objects.requireNonNull(commentsReference.push().getKey());
            Map<String, Object> updateMap = new HashMap<>();
            String commentWithKeyPath = String.format(COMMENT_BY_KEY_PATH, comment.getSentenceId(), commentKey);
            updateMap.put(commentWithKeyPath, comment);
            String commentLikesWithKeyPath = String.format(COMMENT_LIKES_PATH, commentKey);
            updateMap.put(commentLikesWithKeyPath, new Likes());
            databaseReference.updateChildren(updateMap)
                    .addOnSuccessListener(aVoid -> subscriber.onSuccess(commentKey))
                    .addOnFailureListener(e -> subscriber.onError(new RuntimeException(e.getMessage())));
        });
    }


    @Override
    public Single<List<Comment>> getCommentsBySentenceId(String sentenceId) {
        return Single.create(subscriber -> {
            if (!internetConnectionService.isInternetOn()) {
                subscriber.onError(new NoInternetConnectionException());
                return;
            }
            commentsReference
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
    }

    @Override
    public Single<UserSentence> getComment(String commentId) {
        return null;
    }

    @Override
    public Completable like(String sentenceKey, String commentKey, String userUid) {
        return saveLikeOrDislike(true, sentenceKey, commentKey, userUid);
    }

    @Override
    public Completable dislike(String sentenceKey, String commentKey, String userUid) {
        return saveLikeOrDislike(false, sentenceKey, commentKey, userUid);
    }

    private Completable saveLikeOrDislike(boolean isLike, String sentenceKey, String commentKey, String userUid) {
        return Completable.create(subscriber -> {
            if (!internetConnectionService.isInternetOn()) {
                subscriber.onError(new NoInternetConnectionException());
                return;
            }
            likesReference
                    .child(commentKey)
                    .runTransaction(new Transaction.Handler() {
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                            Likes likes = mutableData.getValue(Likes.class);
                            if (likes == null) {
                                return Transaction.success(mutableData);
                            }
                            Likes result = isLike ? calculateLikes(likes, userUid) : calculateDislikes(likes, userUid);
                            mutableData.setValue(result);
                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b,
                                               DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                int count = Objects.requireNonNull(dataSnapshot.getValue(Likes.class)).getCount();
                                Map<String, Object> updateMap = new HashMap<>();
                                String sentenceCountPath = String.format(COMMENT_COUNT_LIKES_PATH, sentenceKey, commentKey);
                                updateMap.put(sentenceCountPath, count);
                                databaseReference
                                        .updateChildren(updateMap)
                                        .addOnSuccessListener(aVoid -> subscriber.onComplete())
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
    public Single<List<Likes>> getLikes(String commentId) {
        return Single.create(subscriber -> {
            likesReference
                    .child(commentId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                List<Likes> likesList = new ArrayList<>();
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    likesList.add(childSnapshot.getValue(Likes.class));
                                }
                                subscriber.onSuccess(likesList);
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
        return null;
    }

    @Override
    public Completable remove(String commentId) {
        return null;
    }
}
