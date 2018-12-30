package pl.jozefniemiec.langninja.data.repository;

import io.reactivex.Observable;
import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;

public interface UserSentenceRepository {

    void insert(UserSentence userSentence);

    Observable<UserSentence> getSentences();

    Observable<UserSentence> getSentencesByLanguage(String languageCode);

    Observable<UserSentence> getUserSentences(String uid);

    Observable<UserSentence> getUserSentencesByLanguage(String uid, String languageCode);

    Observable<UserSentence> getSentence(String sentenceKey);

    void dispose();
}
