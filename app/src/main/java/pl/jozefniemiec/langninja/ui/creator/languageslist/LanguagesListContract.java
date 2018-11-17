package pl.jozefniemiec.langninja.ui.creator.languageslist;

import java.util.List;

import pl.jozefniemiec.langninja.data.repository.model.Language;

public interface LanguagesListContract {

    interface View {

        void showLanguages(List<Language> languages);
    }

    interface Presenter {

        void onViewCreated();
    }
}
