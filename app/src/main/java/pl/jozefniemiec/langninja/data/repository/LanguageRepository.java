package pl.jozefniemiec.langninja.data.repository;

import java.util.List;

import pl.jozefniemiec.langninja.data.repository.model.Language;

public interface LanguageRepository {

    List<Language> getAll();

    void insertAll(Language... languages);

    void close();
}
