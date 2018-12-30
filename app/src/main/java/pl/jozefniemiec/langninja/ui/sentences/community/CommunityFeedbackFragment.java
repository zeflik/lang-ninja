package pl.jozefniemiec.langninja.ui.sentences.community;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.data.repository.firebase.UserSentenceRepositoryImpl;
import pl.jozefniemiec.langninja.data.repository.firebase.model.UserSentence;

public class CommunityFeedbackFragment extends Fragment implements CommunityFeedbackContract.View {

    private static final String ARG_PARAM1 = "param1";
    private String sentenceKey;
    private OnFragmentInteractionListener mListener;
    private Unbinder unbinder;
    private CommunityFeedbackContract.Presenter presenter;

    public CommunityFeedbackFragment() {
    }

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

    public static CommunityFeedbackFragment newInstance(String param1) {
        CommunityFeedbackFragment fragment = new CommunityFeedbackFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sentenceKey = getArguments().getString(ARG_PARAM1);
        }
        presenter = new CommunityFeedbackPresenter(this, new UserSentenceRepositoryImpl());
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
        presenter.loadData(sentenceKey);
    }

    @Override
    public void showData(UserSentence userSentence) {
        communityFeedbackAuthorTextView.setText(userSentence.getAuthor().getName());
        communityFeedbackCommentsCountTextView.setText("12");
        communityFeedbackThumbsCountTextView.setText("102");
        communityFeedbackDateTextView.setText("now");
        Picasso
                .with(requireContext())
                .load(userSentence.getAuthor().getPhoto())
                .into(communityFeedbackAuthorImageView);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
