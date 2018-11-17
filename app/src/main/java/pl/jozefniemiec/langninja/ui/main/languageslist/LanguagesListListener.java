package pl.jozefniemiec.langninja.ui.main.languageslist;

import pl.jozefniemiec.langninja.data.repository.model.Language;

public interface LanguagesListListener {

    void onLanguagesListResult(Language language);
}
