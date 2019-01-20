package pl.jozefniemiec.langninja.ui.editor;

public interface SentenceEditorContract {

    interface View {

        void showSentence(String sentence);

        void showFlag(int flagId);

        void closeAndReturnResult(String sentence);

        void close();

        void showProgress();

        void hideProgress();

        void showErrorMessage(String message);

        void showNeedInternetInfo();

        void notifyDataChanged();
    }

    interface Presenter {

        void loadData(String sentenceId);

        void onSaveButtonClicked(String text);
    }
}
