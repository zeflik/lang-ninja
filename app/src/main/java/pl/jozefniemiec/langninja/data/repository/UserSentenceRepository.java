package pl.jozefniemiec.langninja.data.repository;

import io.reactivex.Completable;
import io.reactivex.Observable;
import pl.jozefniemiec.langninja.data.repository.firebase.model.Likes;
import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;

public interface UserSentenceRepository {

    void insert(UserSentence userSentence);

    Observable<UserSentence> getSentences();

    Observable<UserSentence> getSentencesByLanguage(String languageCode);

    Observable<UserSentence> getUserSentences(String uid);

    Observable<UserSentence> getUserSentencesByLanguage(String uid, String languageCode);

    Observable<UserSentence> getSentence(String sentenceKey);

    Completable like(String sentenceKey, String languageCode, String userUid);

    Completable dislike(String sentenceKey, String languageCode, String userUid);

    Observable<Likes> getLikes(String key);

    void dispose();
}
