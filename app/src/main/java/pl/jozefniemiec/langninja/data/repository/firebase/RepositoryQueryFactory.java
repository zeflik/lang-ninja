package pl.jozefniemiec.langninja.data.repository.firebase;

import java.util.List;

import io.reactivex.Single;
import pl.jozefniemiec.langninja.data.repository.UserSentenceRepository;
import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;

public class RepositoryQueryFactory {

    public static Single<List<UserSentence>> composeQuery(
            UserSentenceRepository repository,
            SearchStrategy strategy,
            String uid,
            String languageKey) {

        switch (strategy) {
            case USER:
                return repository.getUserSentencesByLanguage(uid, languageKey);
            default:
                return repository.getSentencesByLanguage(languageKey);
        }
    }
}
