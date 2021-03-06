package pl.jozefniemiec.langninja.data.repository.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import pl.jozefniemiec.langninja.data.repository.model.Language;

@Dao
public interface LanguageDao {

    @Query("SELECT * FROM Language")
    List<Language> getAll();

    @Query("SELECT * FROM Language " +
            "WHERE Language.code = :languageCode")
    Language getLanguage(String languageCode);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> insertAll(Language... languages);

    @Update
    int update(Language language);

    @Delete
    void delete(Language language);
}
