package pl.jozefniemiec.langninja.ui.main.community;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pl.jozefniemiec.langninja.data.repository.NoInternetConnectionException;
import pl.jozefniemiec.langninja.data.repository.UserSentenceRepository;
import pl.jozefniemiec.langninja.data.repository.firebase.RepositoryQueryFactory;
import pl.jozefniemiec.langninja.data.repository.firebase.SearchStrategy;
import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;
import pl.jozefniemiec.langninja.data.repository.model.Language;
import pl.jozefniemiec.langninja.di.main.community.CommunityFragmentScope;
import pl.jozefniemiec.langninja.service.InternetConnectionService;

import static pl.jozefniemiec.langninja.ui.base.Constants.DEFAULT_USER_UID;

@CommunityFragmentScope
public class CommunityPresenter implements CommunityFragmentContract.Presenter {

    private static final String TAG = CommunityPresenter.class.getSimpleName();

    private final CommunityFragmentContract.View view;
    private final UserSentenceRepository repository;
    private final FirebaseAuth auth;
    private final InternetConnectionService internetConnectionService;
    private String[] inappropriateContentOptions = {"Propagowanie nienawiści", "Niecenzuralne wyrazy", "Inne"};
    private String[] menuOptions = {"Edytuj", "Usuń"};

    @Inject
    CommunityPresenter(CommunityFragmentContract.View view, UserSentenceRepository repository, FirebaseAuth auth, InternetConnectionService internetConnectionService) {
        this.view = view;
        this.repository = repository;
        this.auth = auth;
        this.internetConnectionService = internetConnectionService;
    }

    @Override
    public void pullData(Language language, int option) {
        if (internetConnectionService.isInternetOn()) {
            view.hideNeedInternetInfo();
            view.clearData();
            view.showProgress();
            SearchStrategy searchStrategy = SearchStrategy.valueOf(option);
            String uid = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : DEFAULT_USER_UID;
            String languageCode = language.getCode();
            RepositoryQueryFactory.composeQuery(repository, searchStrategy, uid, languageCode)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally(view::hideProgress)
                    .subscribe(view::addData);
        } else {
            view.clearData();
            view.showNeedInternetInfo();
        }

    }

    @Override
    public void onViewCreated() {
        view.registerOnDataChangeListener();
    }

    @Override
    public void onDestroyView() {
        view.clearData();
        view.unregisterOnDataChangeListener();
    }

    @Override
    public void onShowButtonClicked(UserSentence userSentence) {
        view.showSentenceDetails(userSentence);
    }

    @Override
    public void onCreateSentenceButtonClicked(Language language) {
        view.openNewSentencePage(language.getCode());
    }

    @Override
    public void onItemLongButtonClicked(UserSentence userSentence) {
        String sentenceAuthorUid = userSentence.getAuthor().getUid();
        if (auth.getCurrentUser() != null && auth.getCurrentUser().getUid().equals(sentenceAuthorUid)) {
            view.showSentenceOptionsDialog(menuOptions, userSentence);
        } else {
            view.showInappropriateContentDialog(inappropriateContentOptions, userSentence);
        }
    }

    @Override
    public void onInappropriateContentSelected(int reasonIndex, UserSentence userSentence) {
        String inappropriateContentOption = inappropriateContentOptions[reasonIndex];
        Log.d(TAG, "onInappropriateContentSelected: " + inappropriateContentOption);
    }

    @Override
    public void removeSentence(UserSentence userSentence) {
        repository
                .remove(userSentence)
                .doOnSubscribe(observable -> view.showProgress())
                .doFinally(view::hideProgress)
                .subscribe(
                        () -> view.removeItem(userSentence),
                        error -> {
                            if (error instanceof NoInternetConnectionException) {
                                view.showErrorMessage("Brak połaczenia.");
                            }
                        });
    }

    @Override
    public void onSentenceOptionSelected(int optionIndex, UserSentence userSentence) {
        switch (optionIndex) {
            case 0:
                view.showSentenceDetails(userSentence);
                break;
            case 1:
                view.showRemoveSentenceAlert(userSentence);
                break;
        }
    }
}
