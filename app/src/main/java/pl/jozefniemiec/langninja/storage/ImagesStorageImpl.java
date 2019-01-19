package pl.jozefniemiec.langninja.storage;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import javax.inject.Inject;

import io.reactivex.Single;
import pl.jozefniemiec.langninja.data.repository.NoInternetConnectionException;
import pl.jozefniemiec.langninja.service.InternetConnectionService;

public class ImagesStorageImpl implements ImagesStorage {

    private static final String TAG = ImagesStorageImpl.class.getSimpleName();
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private final InternetConnectionService internetConnectionService;

    @Inject
    ImagesStorageImpl(InternetConnectionService internetConnectionService) {
        this.internetConnectionService = internetConnectionService;
    }

    @Override
    public Single<Uri> uploadProfilePhoto(String userUid, Uri uri) {
        return Single.create(subscriber -> {
            if (internetConnectionService.isInternetOn()) {
                StorageReference profilePhotoReference = storageReference
                        .child("User_Profile_Photos").child(userUid).child("profile_photo.jpg");
                profilePhotoReference.putFile(uri)
                        .continueWithTask(task -> profilePhotoReference.getDownloadUrl())
                        .addOnCompleteListener(uriTask -> subscriber.onSuccess(uriTask.getResult()))
                        .addOnFailureListener(subscriber::onError);
            } else {
                subscriber.onError(new NoInternetConnectionException());
            }
        });
    }
}
