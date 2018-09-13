package com.example.joseph.langninja.model;

public class Preferences {

    public static final int GLOBAL = 0;

    public Preferences(int id, String currentUserUid, long currentTaskId) {
        this.id = id;
        this.currentUserUid = currentUserUid;
        this.currentTaskId = currentTaskId;
    }

    private int id;
    private String currentUserUid;
    private long currentTaskId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrentUserUid() {
        return currentUserUid;
    }

    public void setCurrentUserUid(String currentUserUid) {
        this.currentUserUid = currentUserUid;
    }

    public long getCurrentTaskId() {
        return currentTaskId;
    }

    public void setCurrentTaskId(long currentTaskId) {
        this.currentTaskId = currentTaskId;
    }

    @Override
    public String toString() {
        return "Preferences{" +
                "id=" + id +
                ", currentUserUid='" + currentUserUid + '\'' +
                ", currentTaskId=" + currentTaskId +
                '}';
    }
}
