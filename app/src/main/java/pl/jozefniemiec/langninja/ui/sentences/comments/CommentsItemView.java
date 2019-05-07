package pl.jozefniemiec.langninja.ui.sentences.comments;

public interface CommentsItemView {

    void indicateNegativeNumber();

    void indicatePositiveNumber();

    void selectVoteUpButton(boolean state);

    void selectVoteDownButton(boolean state);

    void showVoteUpProgress();

    void hideVoteUpProgress();

    void showVoteDownProgress();

    void hideVoteDownProgress();

    void showReplaysList();

    void hideReplaysList();

    void resetViewState();

    void changeViewToEdit();

    void changeViewToNormal();
}
