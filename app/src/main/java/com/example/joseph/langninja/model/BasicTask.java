package com.example.joseph.langninja.model;

public class BasicTask {

    private int id;
    private String categoryName;
    private String sentence;
    private int maxTime;
    private boolean isValid;
    private String languageCode;
    private int difficulty;

    public BasicTask(String categoryName, String sentence, int maxTime, boolean isValid, String languageCode, int difficulty) {
        this.categoryName = categoryName;
        this.sentence = sentence;
        this.maxTime = maxTime;
        this.isValid = isValid;
        this.languageCode = languageCode;
        this.difficulty = difficulty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String category) {
        this.categoryName = category;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
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
        return "BasicTask{" +
                "id=" + id +
                ", categoryName='" + categoryName + '\'' +
                ", sentence='" + sentence + '\'' +
                ", maxTime=" + maxTime +
                ", isValid=" + isValid +
                ", languageCode='" + languageCode + '\'' +
                ", difficulty=" + difficulty +
                '}';
    }
}
