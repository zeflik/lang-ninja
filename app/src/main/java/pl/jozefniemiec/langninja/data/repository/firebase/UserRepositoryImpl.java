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
import pl.jozefniemiec.langninja.data.repository.UserRepository;
import pl.jozefniemiec.langninja.data.repository.firebase.model.User;

public class UserRepositoryImpl implements UserRepository {

    private static final String MESSAGE_USER_NOT_EXIST = "User %s data does not exist";

    @Inject
    UserRepositoryImpl() {
    }

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference usersDatabaseReference = databaseReference.child("users");

    @Override
    public Completable insert(User user) {
        return Completable.create(subscriber ->
                databaseReference
                        .child("user_public_sentences")
                        .child(user.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map<String, Object> taskMap = new HashMap<>();
                                taskMap.put("/users/" + user.getUid() + "/name/", user.getName());
                                taskMap.put("/users/" + user.getUid() + "/email/", user.getEmail());
                                if (user.getPhoto() != null) {
                                    taskMap.put("/users/" + user.getUid() + "/photo/", user.getPhoto());
                                }
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    taskMap.put("/public_sentences/" + snapshot.getKey() + "/author/name/", user.getName());
                                    if (user.getPhoto() != null) {
                                        taskMap.put("/public_sentences/" + snapshot.getKey() + "/author/photo/", user.getPhoto());
                                    }
                                }
                                databaseReference.updateChildren(taskMap)
                                        .addOnCompleteListener(task -> subscriber.onComplete())
                                        .addOnFailureListener(subscriber::onError);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        }));
    }

    @Override
    public Single<User> getUser(String uid) {
        return Single.create(subscriber ->
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
                                            .onError(new RuntimeException(String.format(MESSAGE_USER_NOT_EXIST, uid)));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                subscriber.onError(new RuntimeException(databaseError.getMessage()));
                            }
                        })
        );
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(User user) {

    }
}
