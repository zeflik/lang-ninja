package pl.jozefniemiec.langninja.ui.creator;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pl.jozefniemiec.langninja.data.repository.LanguageRepository;
import pl.jozefniemiec.langninja.data.repository.UserRepository;
import pl.jozefniemiec.langninja.data.repository.UserSentenceRepository;
import pl.jozefniemiec.langninja.data.repository.firebase.model.Author;
import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;
import pl.jozefniemiec.langninja.data.repository.model.Language;
import pl.jozefniemiec.langninja.di.creator.SentenceCreatorScope;
import pl.jozefniemiec.langninja.service.AuthService;
import pl.jozefniemiec.langninja.utils.Utility;

@SentenceCreatorScope
public class SentenceCreatorPresenter implements SentenceCreatorContract.Presenter {

    private final static String TAG = SentenceCreatorPresenter.class.getSimpleName();
    private final SentenceCreatorContract.View view;
    private final AuthService authService;
    private final UserSentenceRepository userSentenceRepository;
    private final LanguageRepository languageRepository;
    private final UserRepository userRepository;

    @Inject
    SentenceCreatorPresenter(SentenceCreatorContract.View view,
                             AuthService authService,
                             UserSentenceRepository sentenceCandidateRepository,
                             LanguageRepository languageRepository,
                             UserRepository userRepository) {
        this.view = view;
        this.authService = authService;
        this.userSentenceRepository = sentenceCandidateRepository;
        this.languageRepository = languageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void onViewCreated(String languageCode) {
        List<Language> allLanguages = languageRepository.getAll();
        view.initializeSpinner(allLanguages);
        if (languageCode != null) {
            for (int i = 0; i < allLanguages.size(); i++) {
                if (allLanguages.get(i).getCode().equals(languageCode)) {
                    view.selectSpinnerAtPosition(i);
                    break;
                }
            }
        }
    }

    @Override
    public void onCreateButtonClicked(Language language, String sentence) {
        if (Utility.validateSentenceText(sentence)) {
            userRepository
                    .getUser(authService.getCurrentUserUid())
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(disposable -> view.showProgress())
                    .map(user -> new Author(user.getUid(), user.getName(), user.getPhoto()))
                    .map(author -> new UserSentence(null, sentence, language.getCode(), author))
                    .flatMap(userSentenceRepository::insert)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally(view::hideProgress)
                    .subscribe(id -> {
                                   view.notifyDataChanged();
                                   view.hideKeyboard();
                                   view.close();
                               },
                               error -> {
                                   view.showNeedInternetInfo();
                               });
        } else {
            view.showErrorMessage("Wprowadź tekst!");
        }
    }


    @Override
    public void onTestButtonClicked(Language language, String sentence) {
        if (Utility.validateSentenceText(sentence)) {
            view.showSentenceCard(language.getCode(), sentence);
        } else {
            view.showErrorMessage("Wprowadź tekst!");
        }
    }
}
