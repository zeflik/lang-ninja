package pl.jozefniemiec.langninja.ui.sentences.card;

public interface SentenceCardContract {

    interface View {

        void showData();

//        void showNumbering(int currentPage, int pageCount);

//        void showCorrectSpokenText(String text);
//
//        void showWrongSpokenText(String text);
//
//        void hideSpokenText();

        void showErrorMessage(String message);

        void showNumbering(int currentPage, int pageCount);
    }

    interface Presenter {

        void loadData(String languageCode, String sentence);

        void loadPageDataAtPosition(int position, SentencesItemView itemView);

        int getPageCount();

        String getCurrentSentence();

        void pageChanged(int newPosition);

        void onViewDestroy();
    }
}
