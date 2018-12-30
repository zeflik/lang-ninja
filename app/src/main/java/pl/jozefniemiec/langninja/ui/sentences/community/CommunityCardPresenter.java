package pl.jozefniemiec.langninja.ui.sentences.community;

import javax.inject.Inject;

import pl.jozefniemiec.langninja.data.repository.UserSentenceRepository;

public class CommunityCardPresenter implements CommunityCardContract.Presenter {

    private final CommunityCardContract.View view;
    private final UserSentenceRepository userSentenceRepository;

    @Inject
    CommunityCardPresenter(CommunityCardContract.View view,
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
