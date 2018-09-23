package pl.jozefniemiec.langninja.repository;

import java.util.List;

import pl.jozefniemiec.langninja.model.Language;

public interface LanguageRepository {

    List<Language> getAll();

    void insertAll(Language... languages);

    void close();
}
