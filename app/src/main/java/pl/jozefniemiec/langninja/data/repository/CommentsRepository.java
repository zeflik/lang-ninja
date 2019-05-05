package pl.jozefniemiec.langninja.data.repository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import pl.jozefniemiec.langninja.data.repository.firebase.model.Comment;
import pl.jozefniemiec.langninja.data.repository.firebase.model.Likes;

public interface CommentsRepository {

    Single<String> insert(Comment comment);

    Single<List<Comment>> getCommentsBySentenceId(String sentenceId);

    Single<Comment> getComment(String commentId);

    Single<Comment> like(Comment comment, String userUid);

    Single<Comment> dislike(Comment comment, String userUid);

    Single<Likes> getLikes(String commentId);

    Completable update(Comment comment);

    Completable remove(Comment comment);

    Single<Integer> getCommentsCount(String commentId);
}
