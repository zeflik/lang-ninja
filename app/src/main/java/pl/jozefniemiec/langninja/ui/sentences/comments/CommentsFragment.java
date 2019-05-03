package pl.jozefniemiec.langninja.ui.sentences.comments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.DaggerDialogFragment;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.data.repository.firebase.model.Comment;
import pl.jozefniemiec.langninja.utils.Utility;
import pl.jozefniemiec.langninja.utils.picasso.CircleTransform;

import static pl.jozefniemiec.langninja.ui.base.Constants.SENTENCE_ID_KEY;


public class CommentsFragment extends DaggerDialogFragment implements CommentsFragmentContract.View {

    private String sentenceId;
    private Unbinder unbinder;

    @Inject
    CommentsFragmentContract.Presenter presenter;

    @Inject
    CommentsListAdapter adapter;

    @Inject
    Picasso picasso;

    @BindView(R.id.commentsRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.commentContentEditText)
    EditText commentContentEditText;

    @BindView(R.id.commentTextInputGroup)
    Group inputGroup;

    @BindView(R.id.commentInputPhotoImageView)
    ImageView commentInputPhotoImageView;

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
        presenter.onViewCreated(sentenceId);
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
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void initializeRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showData(List<Comment> comments) {
        Log.d("XXXXXXXXXXXXXX", comments.toString());
        adapter.setComments(comments);
    }

    @Override
    public void clearInputField() {
        commentContentEditText.getText().clear();
    }

    @Override
    public void showNewItem(Comment comment) {
        presenter.onViewCreated(sentenceId);
        //adapter.addItem(comment);
        // recyclerView.smoothScrollToPosition(adapter.getItemCount()-1);
    }

    @Override
    public void showInputPanel() {
        inputGroup.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideInputPanel() {
        inputGroup.setVisibility(View.GONE);
    }

    @Override
    public void showUserPhoto(String authorPhotoUri) {
        picasso
                .load(authorPhotoUri)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .fit()
                .transform(new CircleTransform())
                .into(commentInputPhotoImageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        if (Utility.isNetworkAvailable(requireContext())) {
                            picasso
                                    .load(authorPhotoUri)
                                    .fit()
                                    .transform(new CircleTransform())
                                    .into(commentInputPhotoImageView, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError() {
                                        }
                                    });
                        }
                    }
                });
    }

    @Override
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(commentContentEditText.getWindowToken(), 0);
    }
}
