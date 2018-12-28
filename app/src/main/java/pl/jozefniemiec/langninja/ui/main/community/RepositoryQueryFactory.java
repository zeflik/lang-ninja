package pl.jozefniemiec.langninja.ui.main.community;

import io.reactivex.Observable;
import pl.jozefniemiec.langninja.data.repository.UserSentenceRepository;
import pl.jozefniemiec.langninja.data.repository.firebase.SearchStrategy;
import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;

class RepositoryQueryFactory {

    static Observable<UserSentence> composeQuery(
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
