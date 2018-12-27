package pl.jozefniemiec.langninja.ui.main.community;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pl.jozefniemiec.langninja.data.repository.UserSentenceRepository;
import pl.jozefniemiec.langninja.di.main.send.SendFragmentScope;

@SendFragmentScope
public class SendPresenter implements SendFragmentContract.Presenter {

    private static final String TAG = SendPresenter.class.getSimpleName();

    private final SendFragmentContract.View view;
    private final UserSentenceRepository repository;

    @Inject
    SendPresenter(SendFragmentContract.View view, UserSentenceRepository repository) {
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
    public void loadData(String userUid) {
        repository
                .getPublicSentences()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::addData);
    }
}
