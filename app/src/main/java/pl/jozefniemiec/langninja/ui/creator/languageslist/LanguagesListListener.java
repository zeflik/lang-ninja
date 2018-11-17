package pl.jozefniemiec.langninja.ui.creator.languageslist;

import pl.jozefniemiec.langninja.data.repository.model.Language;

public interface LanguagesListListener {

    void onLanguagesListResult(Language language);
}
