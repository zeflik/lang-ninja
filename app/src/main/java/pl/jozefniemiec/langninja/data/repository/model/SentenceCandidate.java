package pl.jozefniemiec.langninja.data.repository.model;

public class SentenceCandidate extends Sentence {

    private boolean accepted;
    private String reason;

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
