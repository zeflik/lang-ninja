package pl.jozefniemiec.langninja.data.repository.firebase.model;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import pl.jozefniemiec.langninja.data.repository.model.Sentence;

public class UserSentence extends Sentence {

    private String key;
    private String createdBy;
    private int publicVisibility;
    private String dateEdited;
    private Object dateCreated;
    private int likes;
    private int comments;

    public UserSentence() {
    }

    public UserSentence(@NonNull String sentence, @NonNull String languageCode, String createdBy) {
        super(sentence, languageCode);
        this.createdBy = createdBy;
        this.dateCreated = ServerValue.TIMESTAMP;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public int getPublicVisibility() {
        return publicVisibility;
    }

    public void setPublicVisibility(int publicVisibility) {
        this.publicVisibility = publicVisibility;
    }

    @Override
    public int getLikes() {
        return likes;
    }

    @Override
    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getDateEdited() {
        return dateEdited;
    }

    public void setDateEdited(String dateEdited) {
        this.dateEdited = dateEdited;
    }

    public Object getDateCreated() {
        return dateCreated;
    }

    @Exclude
    public long getDateCreatedLong() {
        return (long) dateCreated;
    }

    public void setDateCreated(Object dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        return "UserSentence{" +
                "key='" + key + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", publicVisibility=" + publicVisibility +
                ", dateEdited='" + dateEdited + '\'' +
                ", dateCreated=" + dateCreated +
                ", likes=" + likes +
                ", comments=" + comments +
                '}';
    }
}
