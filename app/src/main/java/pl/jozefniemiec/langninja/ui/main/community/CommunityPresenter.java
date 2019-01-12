package pl.jozefniemiec.langninja.ui.main.community;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.jozefniemiec.langninja.data.repository.UserSentenceRepository;
import pl.jozefniemiec.langninja.data.repository.firebase.RepositoryQueryFactory;
import pl.jozefniemiec.langninja.data.repository.firebase.SearchStrategy;
import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;
import pl.jozefniemiec.langninja.data.repository.model.Language;
import pl.jozefniemiec.langninja.di.main.community.CommunityFragmentScope;

import static pl.jozefniemiec.langninja.ui.base.Constants.DEFAULT_USER_UID;

@CommunityFragmentScope
public class CommunityPresenter implements CommunityFragmentContract.Presenter {

    private static final String TAG = CommunityPresenter.class.getSimpleName();

    private final CommunityFragmentContract.View view;
    private final UserSentenceRepository repository;
    private final FirebaseAuth auth;
    private Disposable subscription;
    private String[] inappropriateContentOptions = {"Propagowanie nienawiści", "Niecenzuralne wyrazy", "Inne"};
    private String[] menuOptions = {"Edytuj", "Usuń"};

    @Inject
    CommunityPresenter(CommunityFragmentContract.View view, UserSentenceRepository repository, FirebaseAuth auth) {
        this.view = view;
        this.repository = repository;
        this.auth = auth;
    }

    @Override
    public void onOptionSelected(Language language, int option) {
        cleanup();
        SearchStrategy searchStrategy = SearchStrategy.valueOf(option);
        String uid = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : DEFAULT_USER_UID;
        String languageCode = language.getCode();
        Observable<UserSentence> userSentenceObservable =
                RepositoryQueryFactory.composeQuery(repository, searchStrategy, uid, languageCode);
        subscription = userSentenceObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::addData);
    }

    @Override
    public void onDestroyView() {
        cleanup();
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
    public void onSentenceOptionSelected(int optionIndex, UserSentence userSentence) {
        switch (optionIndex) {
            case 0:
                view.showSentenceDetails(userSentence);
            case 1:
                Log.d(TAG, "onSentenceOptionSelected: removing sentence: " + userSentence.getSentence());
        }
    }

    private void cleanup() {
        if (subscription != null) {
            repository.dispose();
            subscription.dispose();
        }
        view.clearData();
    }
}
