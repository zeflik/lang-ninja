package pl.jozefniemiec.langninja.data.repository.firebase.model;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class UserSentence {

    private String id;
    private String sentence;
    private String languageCode;
    private Object dateEdited = ServerValue.TIMESTAMP;
    private Object dateCreated = ServerValue.TIMESTAMP;
    private int likesCount;
    private Map<String, Integer> comments = new HashMap<>();
    private int commentsCount;
    private Author author;

    public UserSentence() {
    }

    public UserSentence(String id, String sentence, String languageCode, Author author) {
        this.id = id;
        this.sentence = sentence;
        this.languageCode = languageCode;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public Object getDateEdited() {
        return dateEdited;
    }

    public void setDateEdited(Object dateEdited) {
        this.dateEdited = dateEdited;
    }

    public Object getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Object dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Map<String, Integer> getComments() {
        return comments;
    }

    public void setComments(Map<String, Integer> comments) {
        this.comments = comments;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    @Override
    public String toString() {
        return "UserSentence{" +
                "id='" + id + '\'' +
                ", sentence='" + sentence + '\'' +
                ", languageCode='" + languageCode + '\'' +
                ", dateEdited=" + dateEdited +
                ", dateCreated=" + dateCreated +
                ", likesCount=" + likesCount +
                ", comments=" + comments +
                ", commentsCount=" + commentsCount +
                ", author=" + author +
                '}';
    }
}
