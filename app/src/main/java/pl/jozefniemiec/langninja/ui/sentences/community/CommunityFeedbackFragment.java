package pl.jozefniemiec.langninja.ui.sentences.community;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.data.repository.firebase.FirebaseRealtimeDatabaseService;

public class CommunityFeedbackFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private String sentenceKey;

    private OnFragmentInteractionListener mListener;

    public CommunityFeedbackFragment() {
    }


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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_community_feedback, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();
        TextView textView = requireActivity().findViewById(R.id.sentenceRowSentenceTextView);
        new FirebaseRealtimeDatabaseService().getSentence(sentenceKey).subscribe(userSentence -> textView.setText(userSentence.getSentence()));
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
}
