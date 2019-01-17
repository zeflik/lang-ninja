package pl.jozefniemiec.langninja.ui.profile;

import android.net.Uri;

import pl.jozefniemiec.langninja.ui.base.BaseContract;

public interface UserProfileContract extends BaseContract {

    interface View extends BaseContract.View {

        void findPhotoInGallery();

        void setUserProfileName(String name);

        void setPhoto(Uri imageHolderUri);

        void showProgress();

        void hideProgress();

        void notifyDataChanged();

        void close();

        void showNeedInternetDialog();
    }

    interface Presenter {

        void loadData(String userNameField, Uri imageHolderUri);

        void onUserProfilePhotoClicked();

        void onSaveUserProfileButtonClicked(String userNameField, Uri imageHolderUri);
    }
}
