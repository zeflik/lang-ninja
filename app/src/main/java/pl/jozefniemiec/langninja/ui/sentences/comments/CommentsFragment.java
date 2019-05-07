package pl.jozefniemiec.langninja.ui.sentences.comments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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


public class CommentsFragment extends DaggerDialogFragment implements CommentsFragmentContract.View, CommentsViewHolder.ViewHolderClicks {

    public static final String TAG = CommentsFragment.class.getSimpleName();

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

    private String sentenceId;
    private Unbinder unbinder;
    private InputMethodManager imm;

    public static CommentsFragment newInstance(String sentenceId) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putString(SENTENCE_ID_KEY, sentenceId);
        fragment.setArguments(args);
        fragment.setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_DeviceDefault_Dialog);
        return fragment;
    }

    @OnClick(R.id.commentCreateButton)
    void onCommentCreateButtonClicked() {
        presenter.onCreateCommentClicked(sentenceId, commentContentEditText.getText().toString());
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
        imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
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

    private void initializeRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showData(List<Comment> comments) {
        adapter.setComments(comments);
    }

    @Override
    public void clearInputField() {
        commentContentEditText.getText().clear();
    }

    @Override
    public void addComment(Comment comment) {
        adapter.addItem(comment);
        recyclerView.smoothScrollToPosition(adapter.getItemPosition(comment));
    }

    @Override
    public void replaceComment(Comment newComment, Comment comment) {
        adapter.updateItem(newComment, comment);
    }

    @Override
    public void editComment(Comment comment) {
        int position = adapter.getItemPosition(comment);
        CommentsViewHolder viewHolder = (CommentsViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        viewHolder.changeViewToEdit();
        viewHolder.setCommentExitText(adapter.getItemAtPosition(position).getContent());
        viewHolder.commentExitText.post(() -> {
            focusOnEditText(viewHolder.commentExitText);
            showKeyboardForced();
        });
    }

    private void focusOnEditText(EditText commentExitText) {
        commentExitText.setFocusableInTouchMode(true);
        commentExitText.requestFocus();
        commentExitText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                commentExitText.setFocusableInTouchMode(false);
                imm.hideSoftInputFromWindow(commentExitText.getWindowToken(), 0);
            }
        });
    }

    @Override
    public void collapseCommentEdit(int position) {
        CommentsViewHolder viewHolder = (CommentsViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        viewHolder.changeViewToNormal();
    }

    @Override
    public void removeComment(Comment comment) {
        adapter.removeItem(comment);
    }

    @Override
    public void onVoteUpButtonClicked(CommentsItemView holder, int position) {
        presenter.onVoteUpButtonClicked(holder, adapter.getItemAtPosition(position));
    }

    @Override
    public void onVoteDownButtonClicked(CommentsItemView holder, int position) {
        presenter.onVoteDownButtonClicked(holder, adapter.getItemAtPosition(position));
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
    public void showLoggedUserPhoto(String authorPhotoUri) {
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
    public void showSentenceOptionsDialog(String[] menuOptions, Comment comment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(comment.getContent());
        builder.setItems(menuOptions, (dialog, item) -> presenter.onCommentOptionSelected(item, comment));
        builder.show();
    }

    @Override
    public void showInappropriateContentDialog(String[] reasons, Comment comment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(
                String.format(
                        getString(R.string.alert_title_send_comment_feedback),
                        comment.getContent()
                ));
        builder.setItems(reasons, (dialog, item) -> presenter.onCommentOptionSelected(item, comment));
        builder.show();
    }

    @Override
    public void showRemoveCommentAlert(Comment comment) {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.alert_title_removing)
                .setMessage(R.string.alert_message_removing_comment)
                .setPositiveButton(R.string.button_ok, (dialog, whichButton) -> presenter.onRemoveButtonClicked(comment))
                .setNegativeButton(R.string.button_cancel, (dialog, whichButton) -> dialog.dismiss())
                .show();
    }

    @Override
    public void onEditCancelButtonClicked(int position) {
        presenter.onCancelButtonClicked(position);
    }

    @Override
    public void onBackgroundClicked(int position) {
        presenter.onCommentClicked(adapter.getItemAtPosition(position));
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

    private void showKeyboardForced() {
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public void hideKeyboard() {
        imm.hideSoftInputFromWindow(commentContentEditText.getWindowToken(), 0);
    }
}
