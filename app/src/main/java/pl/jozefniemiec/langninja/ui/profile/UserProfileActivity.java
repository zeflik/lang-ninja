package pl.jozefniemiec.langninja.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.Serializable;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.ui.base.BaseSecuredActivity;
import pl.jozefniemiec.langninja.utils.Utility;
import pl.jozefniemiec.langninja.utils.picasso.CircleTransform;

public class UserProfileActivity extends BaseSecuredActivity implements UserProfileContract.View {

    private static final int SELECT_PICTURE = 2;
    private static final String TAG = UserProfileActivity.class.getSimpleName();
    private Uri imageHolderUri;

    @Inject
    UserProfileContract.Presenter presenter;

    @Inject
    Picasso picasso;

    @BindView(R.id.userProfilePhotoCaption)
    TextView userProfilePhotoCaption;

    @BindView(R.id.userProfilePhoto)
    ImageButton userProfilePhoto;

    @BindView(R.id.userProfileName)
    EditText userProfileName;

    @BindView(R.id.userProfileImageProgressBar)
    ProgressBar imageProgressBar;

    @OnClick({R.id.userProfilePhoto, R.id.userProfilePhotoCaption})
    void userProfilePhotoClicked() {
        presenter.onUserProfilePhotoClicked();
    }

    @OnClick(R.id.userProfileSaveButton)
    void saveUserProfile() {
        presenter.onSaveUserProfileButtonClicked(userProfileName.getText().toString(), imageHolderUri);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadData(userProfileName.getText().toString(), imageHolderUri);
    }

    @Override
    public void setUserProfileName(String name) {
        userProfileName.setText(name);
    }

    @Override
    public void setPhoto(Uri uri) {
        imageProgressBar.setVisibility(View.VISIBLE);
        imageHolderUri = uri;
        picasso
                .load(imageHolderUri)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .fit()
                .transform(new CircleTransform())
                .into(userProfilePhoto, new Callback() {
                    @Override
                    public void onSuccess() {
                        hideProgress();
                    }

                    @Override
                    public void onError() {
                        if (Utility.isNetworkAvailable(UserProfileActivity.this)) {
                            picasso
                                    .load(imageHolderUri)
                                    .fit()
                                    .transform(new CircleTransform())
                                    .into(userProfilePhoto, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            hideProgress();
                                        }

                                        @Override
                                        public void onError() {
                                            hideProgress();
                                            showErrorMessage("Nieokreślony błąd");
                                        }
                                    });
                        } else {
                            showNeedInternetDialog();
                        }

                    }
                });
    }

    @Override
    public void showProgress() {
        imageProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        imageProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public void findPhotoInGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
            Uri imageUri = Objects.requireNonNull(data.getData());

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageHolderUri = result.getUri();
                setPhoto(imageHolderUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.e(TAG, "Crop image error: ");
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (imageHolderUri != null)
            savedInstanceState.putSerializable("Selected image uri", imageHolderUri.toString());

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("Selected image uri")) {
            Serializable selectedImageUri =
                    Objects.requireNonNull(savedInstanceState.getSerializable("Selected image uri"));
            imageHolderUri = Uri.parse(selectedImageUri.toString());
        }
    }

    @Override
    public void showNeedInternetDialog() {
        Utility.showNeedInternetDialog(this);
    }
}
