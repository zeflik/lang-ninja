package pl.jozefniemiec.langninja.ui.main.send;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.data.repository.model.SentenceCandidate;
import pl.jozefniemiec.langninja.ui.main.send.creator.SentenceCreator;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class SendFragment extends Fragment implements SendFragmentView {

    private static final String TAG = SendFragment.class.getSimpleName();
    private static final String ANONYMOUS = "anonymous";
    private static final int RC_SIGN_IN = 1;
    public static final String USER_UID = "User UID";

    @BindView(R.id.floatingActionButtonAddSentence)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.sendSentencesRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.loginPage)
    ConstraintLayout loginPage;

    @BindView(R.id.loginButton)
    Button loginButton;

    @BindView(R.id.logoffButton)
    Button logoffButton;

    private Unbinder unbinder;
    private FirebaseRecyclerAdapter<SentenceCandidate, SentenceRowHolder> adapter;
    private SendPresenter presenter;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private DatabaseReference dbSentencesRef = FirebaseDatabase.getInstance().getReference("sentence");

    public static SendFragment newInstance() {
        SendFragment fragment = new SendFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter = new SendPresenterImpl(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        floatingActionButton.setOnClickListener(v -> openNewSentencePage());
        auth = FirebaseAuth.getInstance();
        loginButton.setOnClickListener(v -> presenter.loginButtonClicked());
        authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                presenter.onLoginSucceed(auth.getCurrentUser().getDisplayName());
            } else {
                presenter.onLoginFailed();
            }
        };
        auth.addAuthStateListener(authStateListener);
        logoffButton.setOnClickListener(v -> auth.signOut());
    }

    public void showData() {
        FirebaseRecyclerOptions<SentenceCandidate> recyclerOptions = new FirebaseRecyclerOptions.Builder<SentenceCandidate>()
                .setQuery(dbSentencesRef.child(auth.getUid()), SentenceCandidate.class)
                .build();
        adapter = new FirebaseSendAdapter(this, recyclerOptions);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public void listenForNewData() {
        if (adapter != null) {
            adapter.startListening();
        }
    }

    public void stopListenForNewData() {
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    public void showLoginInfo() {
        floatingActionButton.setVisibility(View.GONE);
        loginPage.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoginInfo() {
        floatingActionButton.setVisibility(View.VISIBLE);
        loginPage.setVisibility(View.GONE);
    }

    @Override
    public void showLoginPage() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.EmailBuilder().build(),
                                new AuthUI.IdpConfig.GoogleBuilder().build()
                        ))
                        .build(),
                RC_SIGN_IN);
    }

    private void openNewSentencePage() {
        Intent intent = new Intent(requireContext(), SentenceCreator.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        auth.addAuthStateListener(authStateListener);
        presenter.onViewVisible();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }
        presenter.onViewInvisible();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                presenter.onLoginSucceed(Objects.requireNonNull(auth.getCurrentUser().getDisplayName()));
            } else if (resultCode == RESULT_CANCELED) {
                presenter.onLoginFailed();
            }
        }
    }
}