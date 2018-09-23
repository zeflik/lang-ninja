package pl.jozefniemiec.langninja.repository;

import java.util.List;

import pl.jozefniemiec.langninja.model.Sentence;

public interface SentenceRepository {

    List<Sentence> getLanguageSentences(String langCode);

    void insertAll(Sentence... sentences);
}
