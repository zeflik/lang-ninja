package pl.jozefniemiec.langninja.data.repository;

import java.util.List;

import pl.jozefniemiec.langninja.data.repository.model.Sentence;

public interface SentenceRepository {

    List<Sentence> getLanguageSentences(String langCode);

    void insertAll(Sentence... sentences);

    void update(Sentence sentence);

    void delete(Sentence sentence);

    void close();
}
