package pl.jozefniemiec.langninja.ui.sentences.community;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.data.repository.UserSentenceRepository;

public class CommunityFeedbackPresenter implements CommunityFeedbackContract.Presenter {

    private final CommunityFeedbackContract.View view;
    private final UserSentenceRepository userSentenceRepository;

    @Inject
    CommunityFeedbackPresenter(CommunityFeedbackContract.View view,
                               UserSentenceRepository userSentenceRepository) {
        this.view = view;
        this.userSentenceRepository = userSentenceRepository;
    }

    @Override
    public void loadData(String sentenceKey) {
        userSentenceRepository
                .getSentence(sentenceKey)
                .subscribe(view::showData);
    }
}
