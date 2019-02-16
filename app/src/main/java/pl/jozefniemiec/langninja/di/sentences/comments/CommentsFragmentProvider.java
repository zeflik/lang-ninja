package pl.jozefniemiec.langninja.di.sentences.comments;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.jozefniemiec.langninja.ui.sentences.comments.CommentsFragment;

@Module
public abstract class CommentsFragmentProvider {

    @ContributesAndroidInjector(modules = CommentsFragmentModule.class)
    @CommentsFragmentScope
    abstract CommentsFragment bindCommentsFragment();
}
