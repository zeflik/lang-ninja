package pl.jozefniemiec.langninja.data.repository.model;

import android.support.annotation.NonNull;

public class SentenceCandidate extends Sentence {

    private boolean accepted;
    private String reason;

    public SentenceCandidate() {
    }

    public SentenceCandidate(@NonNull String sentence, @NonNull String languageCode) {
        super(sentence, languageCode);
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "Sentence{" +
                "id=" + getId() +
                "sentence=" + getSentence() +
                "accepted=" + accepted +
                ", reason='" + reason + '\'' +
                '}';
    }
}
