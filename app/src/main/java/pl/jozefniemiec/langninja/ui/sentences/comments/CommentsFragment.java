package pl.jozefniemiec.langninja.ui.sentences.comments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.DaggerDialogFragment;
import pl.jozefniemiec.langninja.R;

import static pl.jozefniemiec.langninja.ui.base.Constants.SENTENCE_ID_KEY;


public class CommentsFragment extends DaggerDialogFragment implements CommentsFragmentContract.View {

    private String sentenceId;
    private Unbinder unbinder;

    @Inject
    CommentsFragmentContract.Presenter presenter;

    @OnClick(R.id.commentsLayoutBackgroundLayout)
    void onBackgroundClick() {
        presenter.onBackgroundClicked();
    }

    public static CommentsFragment newInstance(String sentenceId) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putString(SENTENCE_ID_KEY, sentenceId);
        fragment.setArguments(args);
        fragment.setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_DeviceDefault_Dialog);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            sentenceId = savedInstanceState.getString(SENTENCE_ID_KEY);
        } else {
            if (getArguments() != null && getArguments().containsKey(SENTENCE_ID_KEY)) {
                sentenceId = getArguments().getString(SENTENCE_ID_KEY);
            } else {
                throw new RuntimeException(requireActivity().toString()
                                                   + " must pass valid sentence Key");
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void close() {
        requireActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}
