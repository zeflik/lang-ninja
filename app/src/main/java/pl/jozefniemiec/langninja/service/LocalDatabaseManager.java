package pl.jozefniemiec.langninja.service;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

import pl.jozefniemiec.langninja.data.repository.RoomSentenceRepository;
import pl.jozefniemiec.langninja.data.repository.SentenceRepository;
import pl.jozefniemiec.langninja.data.repository.model.Sentence;

public class LocalDatabaseManager {

    private static final String TAG = LocalDatabaseManager.class.getSimpleName();
    private final SentenceRepository sentenceRepository;

    public LocalDatabaseManager(Context context) {
        this.sentenceRepository = new RoomSentenceRepository(context);
    }

    public void syncData(DatabaseReference sentencesReference) {
        sentencesReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                AsyncTask.execute(() -> {
                    try {
                        Sentence sentence = dataSnapshot.getValue(Sentence.class);
                        sentence.setId(dataSnapshot.getKey());
                        sentenceRepository.insertAll(sentence);
                        Log.w(TAG, "Adding new sentence: " + sentence);
                    } catch (DatabaseException databaseException) {
                        Log.e(TAG, "Firebase: onChildAdded: " + databaseException.getMessage());
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                AsyncTask.execute(() -> {
                    try {
                        Sentence sentence = dataSnapshot.getValue(Sentence.class);
                        sentence.setId(dataSnapshot.getKey());
                        sentenceRepository.update(sentence);
                        Log.w(TAG, "Updating new sentence: " + sentence);
                    } catch (DatabaseException databaseException) {
                        Log.e(TAG, "Firebase: onChildChanged: " + databaseException.getMessage());
                    }
                });
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                AsyncTask.execute(() -> {
                    try {
                        Sentence sentence = dataSnapshot.getValue(Sentence.class);
                        sentence.setId(dataSnapshot.getKey());
                        sentenceRepository.delete(sentence);
                        Log.w(TAG, "Deleting sentence: " + sentence);
                    } catch (DatabaseException databaseException) {
                        Log.e(TAG, "Firebase: onChildRemoved: " + databaseException.getMessage());
                    }
                });
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }
}
