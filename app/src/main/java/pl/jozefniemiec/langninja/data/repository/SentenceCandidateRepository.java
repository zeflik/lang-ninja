package pl.jozefniemiec.langninja.data.repository;

import pl.jozefniemiec.langninja.data.repository.model.SentenceCandidate;

public interface SentenceCandidateRepository {

    void insertByUserUid(String userUid, SentenceCandidate sentenceCandidate);
}
