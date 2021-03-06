package pl.jozefniemiec.langninja.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import pl.jozefniemiec.langninja.di.creator.SentenceCreatorModule;
import pl.jozefniemiec.langninja.di.creator.SentenceCreatorScope;
import pl.jozefniemiec.langninja.di.editor.SentenceEditorModule;
import pl.jozefniemiec.langninja.di.editor.SentenceEditorScope;
import pl.jozefniemiec.langninja.di.login.LoginActivityModule;
import pl.jozefniemiec.langninja.di.login.LoginActivityScope;
import pl.jozefniemiec.langninja.di.main.MainActivityModule;
import pl.jozefniemiec.langninja.di.main.MainActivityScope;
import pl.jozefniemiec.langninja.di.main.community.CommunityFragmentProvider;
import pl.jozefniemiec.langninja.di.main.languages.LanguagesFragmentProvider;
import pl.jozefniemiec.langninja.di.profile.UserProfileActivityModule;
import pl.jozefniemiec.langninja.di.profile.UserProfileActivityScope;
import pl.jozefniemiec.langninja.di.reader.ReaderFragmentProvider;
import pl.jozefniemiec.langninja.di.sentences.SentenceCardViewerActivityModule;
import pl.jozefniemiec.langninja.di.sentences.SentenceCardViewerActivityScope;
import pl.jozefniemiec.langninja.di.sentences.card.SentenceCardProvider;
import pl.jozefniemiec.langninja.di.sentences.comments.CommentsFragmentProvider;
import pl.jozefniemiec.langninja.di.sentences.community.CommunityCardProvider;
import pl.jozefniemiec.langninja.di.speech.SpeechRecognizerFragmentModule;
import pl.jozefniemiec.langninja.di.speech.SpeechRecognizerFragmentScope;
import pl.jozefniemiec.langninja.ui.creator.SentenceCreator;
import pl.jozefniemiec.langninja.ui.editor.SentenceEditor;
import pl.jozefniemiec.langninja.ui.login.LoginActivity;
import pl.jozefniemiec.langninja.ui.main.MainActivity;
import pl.jozefniemiec.langninja.ui.profile.UserProfileActivity;
import pl.jozefniemiec.langninja.ui.sentences.SentenceCardViewerActivity;
import pl.jozefniemiec.langninja.ui.speech.SpeechRecognizerFragment;

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = {
            MainActivityModule.class,
            LanguagesFragmentProvider.class,
            CommunityFragmentProvider.class
    })
    @MainActivityScope
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector(modules = {
            SentenceCardViewerActivityModule.class,
            SentenceCardProvider.class,
            ReaderFragmentProvider.class,
            CommunityCardProvider.class,
            CommentsFragmentProvider.class
    })
    @SentenceCardViewerActivityScope
    abstract SentenceCardViewerActivity bindSentenceViewer();

    @ContributesAndroidInjector(modules = {
            SentenceCreatorModule.class,
            LanguagesFragmentProvider.class})
    @SentenceCreatorScope
    abstract SentenceCreator bindSentenceCreator();

    @ContributesAndroidInjector(modules = SpeechRecognizerFragmentModule.class)
    @SpeechRecognizerFragmentScope
    abstract SpeechRecognizerFragment bindSpeechRecognizerFragment();

    @ContributesAndroidInjector(modules = LoginActivityModule.class)
    @LoginActivityScope
    abstract LoginActivity bindLoginActivity();

    @ContributesAndroidInjector(modules = UserProfileActivityModule.class)
    @UserProfileActivityScope
    abstract UserProfileActivity bindUserProfileActivity();

    @ContributesAndroidInjector(modules = SentenceEditorModule.class)
    @SentenceEditorScope
    abstract SentenceEditor bindSentenceEditorActivity();
}
