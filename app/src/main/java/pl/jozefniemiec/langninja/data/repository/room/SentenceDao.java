package pl.jozefniemiec.langninja.data.repository.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import pl.jozefniemiec.langninja.data.repository.model.Sentence;

@Dao
public interface SentenceDao {

    @Query("SELECT * FROM Sentence")
    List<Sentence> getAll();

    @Query("SELECT * FROM Sentence " +
            "WHERE Sentence.languageCode = :languageCode")
    List<Sentence> getSentences(String languageCode);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> insertAll(Sentence... sentences);

    @Update
    int update(Sentence sentence);

    @Delete
    void delete(Sentence sentence);
}
