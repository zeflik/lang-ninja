package pl.jozefniemiec.langninja.data.repository;

import io.reactivex.Completable;
import io.reactivex.Single;
import pl.jozefniemiec.langninja.data.repository.firebase.model.User;

public interface UserRepository {

    Completable insert(User user);

    Single<User> getUser(String uid);

    void update(User user);

    void delete(User user);
}
