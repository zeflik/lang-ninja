package pl.jozefniemiec.langninja.data.repository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;

public interface UserSentenceRepository {

    void insertByUserUid(String userUid, UserSentence userSentence);

    void insert(UserSentence userSentence);

    void insertPublic(UserSentence userSentence);

    Observable<UserSentence> getUserSentences(String uid);

    Single<List<UserSentence>> getUserListSentences(String uid);

    Observable<UserSentence> getPublicSentences();
}
