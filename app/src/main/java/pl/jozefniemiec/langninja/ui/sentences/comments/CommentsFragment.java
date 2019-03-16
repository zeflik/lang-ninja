package pl.jozefniemiec.langninja.ui.sentences.comments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.DaggerDialogFragment;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.data.repository.firebase.model.Comment;

import static pl.jozefniemiec.langninja.ui.base.Constants.SENTENCE_ID_KEY;


public class CommentsFragment extends DaggerDialogFragment implements CommentsFragmentContract.View {

    private String sentenceId;
    private Unbinder unbinder;

    @Inject
    CommentsFragmentContract.Presenter presenter;

    @Inject
    CommentsListAdapter adapter;

    @BindView(R.id.commentsRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.commentContentEditText)
    EditText commentContentEditText;

    @OnClick(R.id.commentCreateButton)
    void onCommentCreateButtonClicked() {
        presenter.onCreateCommentClicked(sentenceId, commentContentEditText.getText().toString());
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeRecyclerView();
        presenter.onViewCreated();
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

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void notifyDataChanged() {

    }

    @Override
    public void showNeedInternetInfo() {

    }

    @Override
    public void showErrorMessage(String message) {

    }

    private void initializeRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showData(List<Comment> comments) {
        adapter.setComments(comments);
    }
}
