package pl.jozefniemiec.langninja.data.repository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import pl.jozefniemiec.langninja.data.repository.firebase.model.Comment;
import pl.jozefniemiec.langninja.data.repository.firebase.model.Likes;
import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;

public interface CommentsRepository {

    Single<String> insert(Comment comment);

    Single<List<Comment>> getCommentsBySentenceId(String sentenceId);

    Single<UserSentence> getComment(String commentId);

    Completable like(String sentenceKey, String commentKey, String userUid);

    Completable dislike(String sentenceKey, String commentKey, String userUid);

    Single<List<Likes>> getLikes(String commentId);

    Completable update(Comment comment);

    Completable remove(String commentId);

    Single<Integer> getCommentsCount(String commentId);
}
