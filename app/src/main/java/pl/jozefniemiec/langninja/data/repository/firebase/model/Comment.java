package pl.jozefniemiec.langninja.data.repository.firebase.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.ServerValue;

import java.util.Objects;

public class Comment implements Parcelable {

    private String id;
    private String sentenceId;
    private String content;
    private Object dateEdited = ServerValue.TIMESTAMP;
    private Object dateCreated = ServerValue.TIMESTAMP;
    private Author author;
    private int repliesCount;
    private int likesCount;
    private Likes likes;

    public Comment() {
    }

    private Comment(Parcel in) {
        id = in.readString();
        sentenceId = in.readString();
        content = in.readString();
        repliesCount = in.readInt();
        likesCount = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(sentenceId);
        dest.writeString(content);
        dest.writeInt(repliesCount);
        dest.writeInt(likesCount);
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public Likes getLikes() {
        return likes;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public void setLikes(Likes likes) {
        this.likes = likes;
    }

    public Comment(String sentenceId, String content, Author author) {
        this.sentenceId = sentenceId;
        this.content = content;
        this.author = author;
    }

    public String getId() {
        return id;
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
        return "\nComment{" +
                "\n id='" + id + '\'' +
                "\n sentenceId='" + sentenceId + '\'' +
                "\n content='" + content + '\'' +
                "\n dateEdited=" + dateEdited +
                "\n dateCreated=" + dateCreated +
                "\n author=" + author +
                "\n repliesCount=" + repliesCount +
                "\n likes=" + likes +
                "\n}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sentenceId, content, dateEdited, dateCreated, author, repliesCount, likesCount, likes);
    }
}
