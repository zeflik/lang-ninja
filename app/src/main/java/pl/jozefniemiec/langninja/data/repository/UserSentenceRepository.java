package pl.jozefniemiec.langninja.data.repository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import pl.jozefniemiec.langninja.data.repository.firebase.model.Likes;
import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;

public interface UserSentenceRepository {

    Single<String> insert(UserSentence userSentence);

    Single<List<UserSentence>> getSentencesByLanguage(String languageCode);

    Single<List<UserSentence>> getUserSentencesByLanguage(String uid, String languageCode);

    Observable<UserSentence> getSentence(String sentenceKey);

    Single<UserSentence> getSentenceOnce(String sentenceId);

    Completable like(String sentenceKey, String languageCode, String userUid);

    Completable dislike(String sentenceKey, String languageCode, String userUid);

    Observable<Likes> getLikes(String key);

    Completable remove(UserSentence userSentence);

    void dispose();

    Completable update(UserSentence userSentence);
}
