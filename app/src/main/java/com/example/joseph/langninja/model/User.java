package com.example.joseph.langninja.model;

import android.support.annotation.NonNull;

public class User {

    public static final String DEFAULT_VALUE = "FA3mrwuXRfXbkgBIA1C0uE3BJxE2";
    public static final String WP_VALUE = "FA3mrwuXRfXbkgBIA1C0uE3BJxE2";
    public static final String KEY = "userId";


    @NonNull
    private String uid;
    private String name;
    private int points;
    private int level;
    private int currentDifficultyLevel;
    private String currentCategory;
    private String currentLanguage;
    private long currentTaskId;

    public User(@NonNull String uid,
                String name,
                int points,
                int level,
                int currentDifficultyLevel,
                String currentCategory,
                String currentLanguage) {
        this.uid = uid;
        this.name = name;
        this.points = points;
        this.level = level;
        this.currentDifficultyLevel = currentDifficultyLevel;
        this.currentCategory = currentCategory;
        this.currentLanguage = currentLanguage;
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCurrentDifficultyLevel() {
        return currentDifficultyLevel;
    }

    public void setCurrentDifficultyLevel(int currentDifficultyLevel) {
        this.currentDifficultyLevel = currentDifficultyLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrentCategory() {
        return currentCategory;
    }

    public void setCurrentCategory(String currentCategory) {
        this.currentCategory = currentCategory;
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public void setCurrentLanguage(String currentLanguage) {
        this.currentLanguage = currentLanguage;
    }

    public long getCurrentTaskId() {
        return currentTaskId;
    }

    public void setCurrentTaskId(long currentTaskId) {
        this.currentTaskId = currentTaskId;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", points=" + points +
                ", level=" + level +
                ", currentDifficultyLevel=" + currentDifficultyLevel +
                ", currentCategory='" + currentCategory + '\'' +
                ", currentLanguage='" + currentLanguage + '\'' +
                ", currentTaskId='" + currentTaskId + '\'' +
                '}';
    }
}
