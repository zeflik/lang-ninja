package pl.jozefniemiec.langninja.data.repository.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import pl.jozefniemiec.langninja.data.repository.UserRepository;
import pl.jozefniemiec.langninja.data.repository.firebase.model.User;

public class UserRepositoryImpl implements UserRepository {

    @Inject
    UserRepositoryImpl() {
    }

    private DatabaseReference usersDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");

    @Override
    public Completable insert(User user) {
        return Completable.create(subscriber -> {
            DatabaseReference userReference = usersDatabaseReference.child(user.getUid());
            Map<String, Object> taskMap = new HashMap<>();
            taskMap.put("name", user.getName());
            taskMap.put("email", user.getEmail());
            if (user.getPhoto() != null) {
                taskMap.put("photo", user.getPhoto());
            }
            userReference.updateChildren(taskMap)
                    .addOnCompleteListener(task -> subscriber.onComplete())
                    .addOnFailureListener(subscriber::onError);
        });
    }

    @Override
    public Single<User> getUser(String uid) {
        return Single
                .create(subscriber ->
                        usersDatabaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    subscriber.onSuccess(dataSnapshot.getValue(User.class));
                                } else {
                                    subscriber.onError(new RuntimeException("User " + uid + " data does not exist"));

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
