package pl.jozefniemiec.langninja.data.repository.firebase.model;

public class User {

    private String uid;
    private String name;
    private String email;
    private String photo;

    public User() {
    }

    public User(String uid, String name, String email, String photo) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.photo = photo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
