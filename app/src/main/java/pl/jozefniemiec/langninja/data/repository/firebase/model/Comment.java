package pl.jozefniemiec.langninja.data.repository.firebase.model;

public class Comment {

    private String content;
    private Object creationDate;
    private Object modificationDate;
    private String userUid;
    private String username;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Object creationDate) {
        this.creationDate = creationDate;
    }

    public Object getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Object modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "content='" + content + '\'' +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                ", userUid='" + userUid + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
