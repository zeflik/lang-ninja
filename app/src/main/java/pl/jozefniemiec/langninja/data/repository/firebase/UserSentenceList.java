package pl.jozefniemiec.langninja.data.repository.firebase;

import java.util.List;

import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;

public class UserSentenceList {


    private List<UserSentence> sentences;

    public UserSentenceList() {
    }

    public List<UserSentence> getSentences() {
        return sentences;
    }

    public void setSentences(List<UserSentence> sentences) {
        this.sentences = sentences;
    }
}
