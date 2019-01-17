package pl.jozefniemiec.langninja.ui.profile;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pl.jozefniemiec.langninja.data.repository.UserRepository;
import pl.jozefniemiec.langninja.data.repository.firebase.model.User;
import pl.jozefniemiec.langninja.data.resources.ResourcesManager;
import pl.jozefniemiec.langninja.di.profile.UserProfileActivityScope;
import pl.jozefniemiec.langninja.service.AuthService;
import pl.jozefniemiec.langninja.storage.ImagesStorage;
import pl.jozefniemiec.langninja.storage.NoInternetException;

@UserProfileActivityScope
public class UserProfilePresenter implements UserProfileContract.Presenter {

    private static final String TAG = UserProfilePresenter.class.getSimpleName();
    private static final String UPLOAD_ERROR_MESSAGE = "Profile photo upload error %s";
    private static final String URI_LOCAL_FILE_SCHEME = "file";
    private final UserProfileContract.View view;
    private final AuthService auth;
    private final UserRepository userRepository;
    private final ImagesStorage imagesStorage;
    private final ResourcesManager resourcesManager;

    @Inject
    UserProfilePresenter(UserProfileContract.View view,
                         AuthService auth,
                         UserRepository userRepository,
                         ImagesStorage imagesStorage, ResourcesManager resourcesManager) {
        this.view = view;
        this.auth = auth;
        this.userRepository = userRepository;
        this.imagesStorage = imagesStorage;
        this.resourcesManager = resourcesManager;
    }

    @Override
    public void onUserProfilePhotoClicked() {
        view.findPhotoInGallery();
    }

    @Override
    public void loadData(String userNameField, Uri imageHolderUri) {
        if (TextUtils.isEmpty(userNameField) && auth.isSignedIn()) {
            view.showProgress();
            userRepository
                    .getUser(auth.getCurrentUserUid())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            user -> {
                                view.hideProgress();
                                view.setUserProfileName(user.getName());
                                if (user.getPhoto() != null) {
                                    view.setPhoto(Uri.parse(user.getPhoto()));
                                }
                            },
                            error -> {
                                view.hideProgress();
                                view.setUserProfileName(auth.getCurrentUserName());
                            });
        } else {
            if (imageHolderUri != null) {
                view.setPhoto(imageHolderUri);
            }
        }
    }

    @Override
    public void onSaveUserProfileButtonClicked(String userNameField, Uri imageUri) {
        if (imageUri != null
                && imageUri.getScheme() != null
                && imageUri.getScheme().equals(URI_LOCAL_FILE_SCHEME)) {
            view.showProgress();
            imagesStorage
                    .uploadProfilePhoto(auth.getCurrentUserUid(), imageUri)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            photoDownloadUri -> {
                                view.setPhoto(photoDownloadUri);
                                saveUser(
                                        userNameField,
                                        auth.getCurrentUserEmail(),
                                        photoDownloadUri.toString());
                            },
                            this::onError);
        } else {
            saveUser(userNameField, auth.getCurrentUserEmail(), null);
        }
    }

    private void saveUser(String name, String email, String photo) {
        User user = new User(auth.getCurrentUserUid(), name, email, photo);
        userRepository.insert(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(view::notifyDataChanged)
                .doOnComplete(view::close)
                .subscribe();
    }

    private void onError(Throwable throwable) {
        view.hideProgress();
        if (throwable instanceof NoInternetException) {
            view.showNeedInternetDialog();
        } else {
            view.showErrorMessage(resourcesManager.getUnknownErrorMessage());
            Log.e(TAG, String.format(UPLOAD_ERROR_MESSAGE, throwable.getMessage()));
        }
    }
}
