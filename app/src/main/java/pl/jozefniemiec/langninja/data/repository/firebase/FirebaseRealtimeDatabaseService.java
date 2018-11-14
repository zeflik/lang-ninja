package pl.jozefniemiec.langninja.data.repository.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.data.repository.SentenceCandidateRepository;
import pl.jozefniemiec.langninja.data.repository.model.SentenceCandidate;

public class FirebaseRealtimeDatabaseService implements SentenceCandidateRepository {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("sentence");

    @Inject
    FirebaseRealtimeDatabaseService() {
    }

    @Override
    public void insertByUserUid(String userUid, SentenceCandidate sentenceCandidate) {
        databaseReference.child(userUid).push().setValue(sentenceCandidate);
    }
}
