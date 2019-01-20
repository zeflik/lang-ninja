package pl.jozefniemiec.langninja.di.editor;

import dagger.Binds;
import dagger.Module;
import pl.jozefniemiec.langninja.ui.editor.SentenceEditor;
import pl.jozefniemiec.langninja.ui.editor.SentenceEditorContract;
import pl.jozefniemiec.langninja.ui.editor.SentenceEditorPresenter;

@Module
public abstract class SentenceEditorModule {

    @Binds
    @SentenceEditorScope
    abstract SentenceEditorContract.Presenter bindPresenter(SentenceEditorPresenter presenter);

    @Binds
    @SentenceEditorScope
    abstract SentenceEditorContract.View bindView(SentenceEditor view);
}


