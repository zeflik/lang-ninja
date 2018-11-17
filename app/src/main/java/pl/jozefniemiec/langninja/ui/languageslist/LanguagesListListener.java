package pl.jozefniemiec.langninja.ui.languageslist;

import pl.jozefniemiec.langninja.data.repository.model.Language;

public interface LanguagesListListener {

    void onLanguagesListResult(Language language);
}
