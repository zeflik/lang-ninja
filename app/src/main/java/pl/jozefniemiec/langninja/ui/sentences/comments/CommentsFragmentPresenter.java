package pl.jozefniemiec.langninja.ui.sentences.comments;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.di.sentences.comments.CommentsFragmentScope;

@CommentsFragmentScope
public class CommentsFragmentPresenter implements CommentsFragmentContract.Presenter {

    private final CommentsFragmentContract.View view;

    @Inject
    CommentsFragmentPresenter(CommentsFragmentContract.View view) {
        this.view = view;
    }

    @Override
    public void onBackgroundClicked() {
        view.close();
    }
}
