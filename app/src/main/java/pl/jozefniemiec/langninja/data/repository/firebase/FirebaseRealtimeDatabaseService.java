package pl.jozefniemiec.langninja.data.repository.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.data.repository.SentenceCandidateRepository;
import pl.jozefniemiec.langninja.data.repository.model.UserSentence;

public class FirebaseRealtimeDatabaseService implements SentenceCandidateRepository {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("sentence");

    @Inject
    FirebaseRealtimeDatabaseService() {
    }

    @Override
    public void insertByUserUid(String userUid, UserSentence sentenceCandidate) {
        databaseReference.child(userUid).push().setValue(sentenceCandidate);
    }

    @Override
    public void insert(UserSentence sentenceCandidate) {
        databaseReference.push().setValue(sentenceCandidate);
    }
}
