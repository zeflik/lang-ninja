package pl.jozefniemiec.langninja.activities.language.presenter;

import pl.jozefniemiec.langninja.activities.language.view.LanguageCardView;

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
