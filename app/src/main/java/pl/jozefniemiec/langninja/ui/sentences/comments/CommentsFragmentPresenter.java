package pl.jozefniemiec.langninja.ui.sentences.comments;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pl.jozefniemiec.langninja.data.repository.CommentsRepository;
import pl.jozefniemiec.langninja.data.repository.UserRepository;
import pl.jozefniemiec.langninja.data.repository.firebase.model.Author;
import pl.jozefniemiec.langninja.data.repository.firebase.model.Comment;
import pl.jozefniemiec.langninja.di.sentences.comments.CommentsFragmentScope;
import pl.jozefniemiec.langninja.service.AuthService;
import pl.jozefniemiec.langninja.utils.Utility;

@CommentsFragmentScope
public class CommentsFragmentPresenter implements CommentsFragmentContract.Presenter {

    private final CommentsFragmentContract.View view;
    private final CommentsRepository commentsRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    @Inject
    CommentsFragmentPresenter(CommentsFragmentContract.View view,
                              CommentsRepository commentsRepository,
                              UserRepository userRepository,
                              AuthService authService) {
        this.view = view;
        this.commentsRepository = commentsRepository;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @Override
    public void onViewCreated() {
        ArrayList<Comment> comments = new ArrayList<>();
        comments.add(new Comment("xxxx", "abc", new Author("asdf", "name", "photo")));

        comments.add(new Comment("xxxx", "abc", new Author("asdf", "name", "photo")));
        comments.add(new Comment("xxxx", "abc", new Author("asdf", "name", "photo")));
        comments.add(new Comment("xxxx", "abc", new Author("asdf", "name", "photo")));
        comments.add(new Comment("xxxx", "abc", new Author("asdf", "name", "photo")));
        comments.add(new Comment("xxxx", "abc", new Author("asdf", "name", "photo")));
        comments.add(new Comment("xxxx", "abc", new Author("asdf", "name", "photo")));
        comments.add(new Comment("xxxx", "abc", new Author("asdf", "name", "photo")));
        comments.add(new Comment("xxxx", "abc", new Author("asdf", "name", "photo")));

        view.showData(comments);
    }

    @Override
    public void onCreateCommentClicked(String sentenceId, String commentText) {
        if (Utility.validateCommentText(commentText)) {
            userRepository
                    .getUser(authService.getCurrentUserUid())
                    .subscribeOn(Schedulers.io())
                    .map(user -> new Author(user.getUid(), user.getName(), user.getPhoto()))
                    .map(author -> new Comment(sentenceId, commentText, author))
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> view.showProgress())
                    .doFinally(view::hideProgress)
                    .subscribe(
                            comment -> {
                                commentsRepository
                                        .insert(comment)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .doOnSubscribe(disposable -> view.showProgress())
                                        .doFinally(view::hideProgress)
                                        .subscribe(
                                                id -> view.notifyDataChanged(),
                                                error -> view.showNeedInternetInfo()
                                        );
                            }//TODO - refactor
                            , error -> {
                                view.showNeedInternetInfo();
                            }
                    );
        } else {
            view.showErrorMessage("Niewłaściwy format.");
        }
    }
}
