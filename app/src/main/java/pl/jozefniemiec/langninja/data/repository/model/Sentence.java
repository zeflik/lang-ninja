package pl.jozefniemiec.langninja.data.repository.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.firebase.database.annotations.NotNull;

@Entity
public class Sentence {

    @PrimaryKey
    @NonNull
    private String id;

    @NonNull
    private String sentence;

    @NonNull
    private String languageCode;

    private int likes;

    @Ignore
    public Sentence() {
    }

    @Ignore
    public Sentence(@NonNull String sentence, @NonNull String languageCode) {
        this.sentence = sentence;
        this.languageCode = languageCode;
    }

    public Sentence(@NonNull String sentence, @NonNull String languageCode, int likes) {
        this.sentence = sentence;
        this.languageCode = languageCode;
        this.likes = likes;
    }

    @NotNull
    public String getId() {
        return id;
    }

    @NonNull
    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    public String getSentence() {
        return sentence;
    }

    public void setSentence(@NonNull String sentence) {
        this.sentence = sentence;
    }

    @NonNull
    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(@NonNull String languageCode) {
        this.languageCode = languageCode;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "Sentence{" +
                "id='" + id + '\'' +
                ", sentence='" + sentence + '\'' +
                ", languageCode='" + languageCode + '\'' +
                ", likes=" + likes +
                '}';
    }
}
