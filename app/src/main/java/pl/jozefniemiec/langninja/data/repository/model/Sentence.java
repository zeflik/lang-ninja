package pl.jozefniemiec.langninja.data.repository.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Sentence {

    @PrimaryKey
    @NonNull
    private String sentence;

    @NonNull
    private String languageCode;

    private int difficulty;

    @Ignore
    public Sentence() {
    }

    @Ignore
    public Sentence(@NonNull String sentence, @NonNull String languageCode) {
        this.sentence = sentence;
        this.languageCode = languageCode;
    }

    public Sentence(@NonNull String sentence, @NonNull String languageCode, int difficulty) {
        this.sentence = sentence;
        this.languageCode = languageCode;
        this.difficulty = difficulty;
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

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public String toString() {
        return "Sentence{" +
                "sentence='" + sentence + '\'' +
                ", languageCode='" + languageCode + '\'' +
                ", difficulty=" + difficulty +
                '}';
    }
}
