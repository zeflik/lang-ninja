package pl.jozefniemiec.langninja.data.utils;

import pl.jozefniemiec.langninja.data.repository.model.Language;
import pl.jozefniemiec.langninja.data.repository.model.Sentence;

public class InitialData {

    private static final String LANGUAGE_CODE_PL = "pl_PL";
    private static final String LANGUAGE_CODE_DE = "de";
    private static final String LANGUAGE_CODE_ENGB = "en_GB";

    public static Language[] populateLanguages() {
        return new Language[]{
                new Language(LANGUAGE_CODE_PL, "Polski"),
                new Language(LANGUAGE_CODE_ENGB, "English"),
                new Language(LANGUAGE_CODE_DE, "Deutsch")
        };
    }

    public static Sentence[] populateSentences() {
        return new Sentence[]{
                new Sentence("Ala ma kota", LANGUAGE_CODE_PL, 1),
                new Sentence("Ala ma psa", LANGUAGE_CODE_PL, 2),
                new Sentence("Ala ma mrówkę", LANGUAGE_CODE_PL, 3),
                new Sentence("Ala has a dog", LANGUAGE_CODE_ENGB, 1),
                new Sentence("Ala has a cat", LANGUAGE_CODE_ENGB, 2),
                new Sentence("Ala hat einen Hund", LANGUAGE_CODE_DE, 1),
                new Sentence("Ala hat eine Katze", LANGUAGE_CODE_DE, 2),
        };
    }
}
