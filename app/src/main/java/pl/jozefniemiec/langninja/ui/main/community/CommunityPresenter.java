package pl.jozefniemiec.langninja.ui.main.community;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.jozefniemiec.langninja.data.repository.UserSentenceRepository;
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

    @Inject
    CommunityPresenter(CommunityFragmentContract.View view, UserSentenceRepository repository, FirebaseAuth auth) {
        this.view = view;
        this.repository = repository;
        this.auth = auth;
    }

    @Override
    public void onOptionSelected(Language language, int option) {
        if (subscription != null) {
            subscription.dispose();
        }
        view.clearData();
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
}
