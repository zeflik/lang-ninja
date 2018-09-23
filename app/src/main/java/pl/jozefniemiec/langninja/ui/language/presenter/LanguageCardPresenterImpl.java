package pl.jozefniemiec.langninja.ui.language.presenter;

import pl.jozefniemiec.langninja.ui.language.view.LanguageCardView;

public class LanguageCardPresenterImpl implements LanguageCardPresenter {

    private final LanguageCardView view;

    public LanguageCardPresenterImpl(LanguageCardView view) {
        this.view = view;
    }

    @Override
    public void loadData() {
        view.showData();
    }
}
