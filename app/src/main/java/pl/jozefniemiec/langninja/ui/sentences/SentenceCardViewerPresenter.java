package pl.jozefniemiec.langninja.ui.sentences;

import java.util.List;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.di.sentences.SentenceCardViewerActivityScope;
import pl.jozefniemiec.langninja.utils.Utility;

@SentenceCardViewerActivityScope
public class SentenceCardViewerPresenter implements SentenceCardViewerContract.Presenter {

    private final SentenceCardViewerContract.View view;

    @Inject
    SentenceCardViewerPresenter(SentenceCardViewerContract.View view) {
        this.view = view;
    }

    @Override
    public void onViewCreated() {

    }

    @Override
    public void onTextSpokenResult(List<String> spokenTextsList) {
        String spokenTextFirstMatch = spokenTextsList.get(0);
        if (spokenTextFirstMatch.equalsIgnoreCase(Utility.removePunctuationMarks(view.getCurrentSentence()))) {
            view.showCorrectSpokenText(spokenTextFirstMatch);
        } else {
            view.showWrongSpokenText(spokenTextFirstMatch);
        }
    }

    @Override
    public void onSentencePageChanged(int position, int pageCount) {
        view.hideSpokenText();
        view.updateNumbering(position, pageCount);
    }
}
