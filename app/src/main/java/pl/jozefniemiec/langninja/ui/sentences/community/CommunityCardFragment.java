package pl.jozefniemiec.langninja.ui.sentences.community;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;
import pl.jozefniemiec.langninja.utils.DateUtils;
import pl.jozefniemiec.langninja.utils.Utility;
import pl.jozefniemiec.langninja.utils.picasso.CircleTransform;

public class CommunityCardFragment extends DaggerFragment implements CommunityCardContract.View {

    private static final String TAG = CommunityCardFragment.class.getSimpleName();
    private static final String SENTENCE_KEY = "sentence_key";
    private String sentenceKey;
    private UserSentence userSentence;
    private Unbinder unbinder;

    @Inject
    CommunityCardContract.Presenter presenter;

    @Inject
    Picasso picasso;

    @BindView(R.id.communityFeedbackAuthorTextView)
    TextView communityFeedbackAuthorTextView;

    @BindView(R.id.communityFeedbackAuthorImageView)
    ImageView communityFeedbackAuthorImageView;

    @BindView(R.id.communityFeedbackDateTextView)
    TextView communityFeedbackDateTextView;

    @BindView(R.id.communityFeedbackCommentsCountTextView)
    TextView communityFeedbackCommentsCountTextView;

    @BindView(R.id.communityFeedbackThumbsCountTextView)
    TextView communityFeedbackThumbsCountTextView;

    @BindView(R.id.communityFeedbackThumbUpImageButton)
    ImageButton communityFeedbackThumbUpImageView;

    @OnClick(R.id.communityFeedbackThumbUpImageButton)
    void dislike() {
        presenter.onLikeButtonClicked(userSentence);
    }

    @BindView(R.id.communityFeedbackThumbDownImageView)
    ImageButton communityFeedbackThumbDownImageView;

    @OnClick(R.id.communityFeedbackThumbDownImageView)
    void like() {
        presenter.onDislikeButtonClicked(userSentence);
    }

    public CommunityCardFragment() {
    }

    public static CommunityCardFragment newInstance(String param1) {
        CommunityCardFragment fragment = new CommunityCardFragment();
        Bundle args = new Bundle();
        args.putString(SENTENCE_KEY, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sentenceKey = getArguments().getString(SENTENCE_KEY);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community_feedback, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void showData(UserSentence userSentence) {
        this.userSentence = userSentence;
        communityFeedbackAuthorTextView.setText(userSentence.getAuthor().getName());
        communityFeedbackCommentsCountTextView.setText("12");
        communityFeedbackThumbsCountTextView.setText(String.valueOf(userSentence.getLikesCount()));
        Long timestamp = (Long) userSentence.getDateEdited();
        communityFeedbackDateTextView.setText(DateUtils.generateTimePeriodDescription(timestamp, requireContext()));
        picasso
                .load(userSentence.getAuthor().getPhoto())
                .fit()
                .transform(new CircleTransform())
                .into(communityFeedbackAuthorImageView);
    }

    @Override
    public void highlightLikeButton() {
        Utility.highlightButton(requireActivity(), communityFeedbackThumbUpImageView);
    }

    @Override
    public void highlightDislikeButton() {
        Utility.highlightButton(requireActivity(), communityFeedbackThumbDownImageView);
    }

    @Override
    public void unHighlightLikeButton() {
        Utility.unHighlightButton(requireActivity(), communityFeedbackThumbUpImageView);
    }

    @Override
    public void unHighlightDislikeButton() {
        Utility.unHighlightButton(requireActivity(), communityFeedbackThumbDownImageView);
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSignInDialog() {
        Utility.signInRequiredDialog(requireContext());
    }

    @Override
    public void showNeedInternetDialog() {
        Utility.showNeedInternetDialog(requireContext());
    }

    @Override
    public void showLikesCount(String value) {
        communityFeedbackThumbsCountTextView.setText(value);
    }

    @Override
    public void notifyDataChanged() {
        Utility.sendBroadcastUserSentencesChanged(requireContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadData(sentenceKey);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onViewClose();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
