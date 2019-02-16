package pl.jozefniemiec.langninja.di.sentences.comments;

import dagger.Binds;
import dagger.Module;
import pl.jozefniemiec.langninja.ui.sentences.comments.CommentsFragment;
import pl.jozefniemiec.langninja.ui.sentences.comments.CommentsFragmentContract;
import pl.jozefniemiec.langninja.ui.sentences.comments.CommentsFragmentPresenter;

@Module
abstract class CommentsFragmentModule {

    @Binds
    @CommentsFragmentScope
    abstract CommentsFragmentContract.View bindCommentsFragmentView(CommentsFragment fragment);

    @Binds
    @CommentsFragmentScope
    abstract CommentsFragmentContract.Presenter bindCommentsFragmentPresenter(CommentsFragmentPresenter presenter);
}
