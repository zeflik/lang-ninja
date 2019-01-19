package pl.jozefniemiec.langninja.data.repository.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import pl.jozefniemiec.langninja.data.repository.NoInternetConnectionException;
import pl.jozefniemiec.langninja.data.repository.UserRepository;
import pl.jozefniemiec.langninja.data.repository.firebase.model.User;
import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;
import pl.jozefniemiec.langninja.service.InternetConnectionService;

public class UserRepositoryImpl implements UserRepository {

    private static final String TAG = UserRepositoryImpl.class.getSimpleName();
    private static final String MESSAGE_USER_NOT_EXIST = "User %s data does not exist";
    private static final String PUBLIC_SENTENCES_PATH = "/public_sentences/";
    private static final String AUTHOR_UID_CHILD = "author/uid";
    private static final String USER_EMAIL_PATH = "/users/%s/email/";
    private static final String USER_NAME_PATH = "/users/%s/name/";
    private static final String PUBLIC_SENTENCES_AUTHOR_NAME_PATH = "/public_sentences/%s/author/name/";
    private static final String PUBLIC_SENTENCES_BY_LANG_AUTHOR_NAME_PATH = "/public_sentences_by_lang/%s/%s/author/name/";
    private static final String USER_PHOTO_PATH = "/users/%s/photo/";
    private static final String PUBLIC_SENTENCES_AUTHOR_PHOTO_PATH = "/public_sentences/%s/author/photo/";
    private static final String PUBLIC_SENTENCES_BY_AUTHOR_USER_PHOTO_PATH = "/public_sentences_by_lang/%s/%s/author/photo/";

    private final InternetConnectionService internetConnectionService;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference usersDatabaseReference = databaseReference.child("users");

    @Inject
    UserRepositoryImpl(InternetConnectionService internetConnectionService) {
        this.internetConnectionService = internetConnectionService;
    }

    @Override
    public Completable insert(User user) {
        return Completable.create(
                subscriber -> {
                    if (!internetConnectionService.isInternetOn()) {
                        subscriber.onError(new NoInternetConnectionException());
                        return;
                    }
                    databaseReference
                            .child(PUBLIC_SENTENCES_PATH)
                            .orderByChild(AUTHOR_UID_CHILD)
                            .equalTo(user.getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Map<String, Object> updateMap = new HashMap<>();
                                    String userNamePath = String.format(USER_NAME_PATH, user.getUid());
                                    updateMap.put(userNamePath, user.getName());
                                    String userEmailPath = String.format(USER_EMAIL_PATH, user.getUid());
                                    updateMap.put(userEmailPath, user.getEmail());
                                    if (user.getPhoto() != null) {
                                        String userPhotoPath = String.format(USER_PHOTO_PATH, user.getUid());
                                        updateMap.put(userPhotoPath, user.getPhoto());
                                    }
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        String sentenceNamePath =
                                                String.format(PUBLIC_SENTENCES_AUTHOR_NAME_PATH, snapshot.getKey());
                                        updateMap.put(sentenceNamePath, user.getName());
                                        String languageCode =
                                                Objects.requireNonNull(snapshot.getValue(UserSentence.class)).getLanguageCode();
                                        String sentenceByLangNamePath =
                                                String.format(PUBLIC_SENTENCES_BY_LANG_AUTHOR_NAME_PATH, languageCode, snapshot.getKey());
                                        updateMap.put(sentenceByLangNamePath, user.getName());
                                        if (user.getPhoto() != null) {
                                            String sentenceUserPhotoPath =
                                                    String.format(PUBLIC_SENTENCES_AUTHOR_PHOTO_PATH, snapshot.getKey());
                                            updateMap.put(sentenceUserPhotoPath, user.getPhoto());
                                            String sentenceByLangUserPhotoPath =
                                                    String.format(PUBLIC_SENTENCES_BY_AUTHOR_USER_PHOTO_PATH,
                                                                  languageCode, snapshot.getKey());
                                            updateMap.put(sentenceByLangUserPhotoPath, user.getPhoto());
                                        }
                                    }
                                    databaseReference
                                            .updateChildren(updateMap)
                                            .addOnSuccessListener(aVoid -> subscriber.onComplete())
                                            .addOnFailureListener(subscriber::onError);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    /**/
                                }
                            });
                }
        );
    }


    @Override
    public Single<User> getUser(String uid) {
        return Single.create(
                subscriber -> {
                    usersDatabaseReference
                            .child(uid)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        User user = dataSnapshot.getValue(User.class);
                                        Objects.requireNonNull(user).setUid(uid);
                                        subscriber
                                                .onSuccess(user);
                                    } else {
                                        subscriber
                                                .onError(new RuntimeException(
                                                        String.format(MESSAGE_USER_NOT_EXIST, uid)
                                                ));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    subscriber
                                            .onError(new RuntimeException(databaseError.getMessage()));
                                }
                            });
                }
        );
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(User user) {

    }
}