package pl.jozefniemiec.langninja.data.repository.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Language {

    @PrimaryKey
    @NonNull
    private String code;
    @NonNull
    private String nativeName;

    public Language(@NonNull String code, @NonNull String nativeName) {
        this.code = code;
        this.nativeName = nativeName;
    }

    @NonNull
    public String getCode() {
        return code;
    }

    public void setCode(@NonNull String code) {
        this.code = code;
    }

    @NonNull
    public String getNativeName() {
        return nativeName;
    }

    public void setNativeName(@NonNull String nativeName) {
        this.nativeName = nativeName;
    }

    @Override
    public String toString() {
        return "Language{" +
                "code='" + code + '\'' +
                ", nativeName='" + nativeName + '\'' +
                '}';
    }
}
