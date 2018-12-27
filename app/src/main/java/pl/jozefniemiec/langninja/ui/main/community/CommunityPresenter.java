package pl.jozefniemiec.langninja.ui.main.community;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pl.jozefniemiec.langninja.data.repository.UserSentenceRepository;
import pl.jozefniemiec.langninja.data.repository.model.Language;
import pl.jozefniemiec.langninja.di.main.community.CommunityFragmentScope;

@CommunityFragmentScope
public class CommunityPresenter implements CommunityFragmentContract.Presenter {

    private static final String TAG = CommunityPresenter.class.getSimpleName();

    private final CommunityFragmentContract.View view;
    private final UserSentenceRepository repository;

    @Inject
    CommunityPresenter(CommunityFragmentContract.View view, UserSentenceRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void onViewVisible() {
    }

    @Override
    public void onViewInvisible() {
    }

    @Override
    public void onOptionSelected(Language language, int option) {
        repository
                .getPublicSentences()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::addData);
    }
}
