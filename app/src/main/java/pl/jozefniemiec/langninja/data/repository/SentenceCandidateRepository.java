package pl.jozefniemiec.langninja.data.repository;

import pl.jozefniemiec.langninja.data.repository.model.UserSentence;

public interface SentenceCandidateRepository {

    void insertByUserUid(String userUid, UserSentence sentenceCandidate);

    void insert(UserSentence sentenceCandidate);
}
