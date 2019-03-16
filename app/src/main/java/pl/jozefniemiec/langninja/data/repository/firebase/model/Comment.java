package pl.jozefniemiec.langninja.data.repository.firebase.model;

import com.google.firebase.database.ServerValue;

public class Comment {

    private String id;
    private String sentenceId;
    private String content;
    private Object dateEdited = ServerValue.TIMESTAMP;
    private Object dateCreated = ServerValue.TIMESTAMP;
    private Author author;
    private int likesCount;
    private int repliesCount;

    public String getId() {
        return id;
    }

    public Comment(String sentenceId, String content, Author author) {
        this.sentenceId = sentenceId;
        this.content = content;
        this.author = author;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getRepliesCount() {
        return repliesCount;
    }

    public void setRepliesCount(int repliesCount) {
        this.repliesCount = repliesCount;
    }

    public String getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(String sentenceId) {
        this.sentenceId = sentenceId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", sentenceId='" + sentenceId + '\'' +
                ", content='" + content + '\'' +
                ", dateEdited=" + dateEdited +
                ", dateCreated=" + dateCreated +
                ", author=" + author +
                ", likesCount=" + likesCount +
                ", repliesCount=" + repliesCount +
                '}';
    }
}
