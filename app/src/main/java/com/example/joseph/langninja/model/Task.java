package com.example.joseph.langninja.model;

public class Task {

    public static final int EASY = 0;
    public static final int MEDIUM = 1;
    public static final int HARD = 2;

    public static final long DEFAULT_VALUE = 0;
    public static final String KEY = "currentTask";

    private long id;
    private int stars;
    private boolean isCompleted;
    private int basicTaskId;
    private int difficultyLevel;
    private String userUid;

    public Task(int stars, boolean isCompleted, int basicTaskId, int difficultyLevel, String userUid) {
        this.stars = stars;
        this.isCompleted = isCompleted;
        this.basicTaskId = basicTaskId;
        this.difficultyLevel = difficultyLevel;
        this.userUid = userUid;
    }

    public Task(long id, int stars, boolean isCompleted, int basicTaskId, int difficultyLevel, String userUid) {
        this.id = id;
        this.stars = stars;
        this.isCompleted = isCompleted;
        this.basicTaskId = basicTaskId;
        this.difficultyLevel = difficultyLevel;
        this.userUid = userUid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public int getBasicTaskId() {
        return basicTaskId;
    }

    public void setBasicTaskId(int basicTaskId) {
        this.basicTaskId = basicTaskId;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", stars=" + stars +
                ", isCompleted=" + isCompleted +
                ", basicTaskId=" + basicTaskId +
                ", difficultyLevel=" + difficultyLevel +
                ", userUid='" + userUid + "\n" + '\'' +
                '}';
    }
}
