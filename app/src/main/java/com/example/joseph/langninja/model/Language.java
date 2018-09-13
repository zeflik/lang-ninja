package com.example.joseph.langninja.model;

import android.support.annotation.NonNull;

public class Language {

    @NonNull
    private String code;

    public Language(@NonNull String code) {
        this.code = code;
    }

    @NonNull
    public String getCode() {
        return code;
    }

    public void setCode(@NonNull String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Language{" +
                "code='" + code + '\'' +
                '}';
    }
}
