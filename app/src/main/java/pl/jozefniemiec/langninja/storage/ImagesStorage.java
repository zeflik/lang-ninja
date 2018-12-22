package pl.jozefniemiec.langninja.storage;

import android.net.Uri;

import io.reactivex.Single;

public interface ImagesStorage {

    Single<Uri> uploadProfilePhoto(String userUid, Uri uri);
}
