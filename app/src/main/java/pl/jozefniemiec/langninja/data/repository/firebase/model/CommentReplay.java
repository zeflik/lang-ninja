package pl.jozefniemiec.langninja.data.repository.firebase.model;

import com.google.firebase.database.ServerValue;

public class CommentReplay {

    private String id;
    private String commentId;
    private String content;
    private Object dateEdited = ServerValue.TIMESTAMP;
    private Object dateCreated = ServerValue.TIMESTAMP;
    private Author author;
    private int likesCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
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
}
