package pl.jozefniemiec.langninja.ui.sentences.card;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;
import pl.jozefniemiec.langninja.R;

import static pl.jozefniemiec.langninja.ui.base.Constants.LANGUAGE_CODE_KEY;
import static pl.jozefniemiec.langninja.ui.base.Constants.SENTENCE_KEY;

public class SentenceCardFragment extends DaggerFragment
        implements
        SentenceCardContract.View,
        ViewPager.OnPageChangeListener {

    private static final String TAG = SentenceCardFragment.class.getSimpleName();

    @Inject
    SentencesPageAdapter languagePageAdapter;

    @Inject
    SentenceCardContract.Presenter presenter;

    @BindView(R.id.language_card_view_pager)
    ViewPager viewPager;

    private Unbinder unbinder;
    private OnSentenceCardFragmentInteractionListener listener;
    private String languageCode;
    private String sentence;

    public static SentenceCardFragment newInstance(String languageCode, String sentence) {
        SentenceCardFragment fragment = new SentenceCardFragment();
        Bundle args = new Bundle();
        args.putString(LANGUAGE_CODE_KEY, languageCode);
        if (sentence != null) {
            args.putString(SENTENCE_KEY, sentence);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSentenceCardFragmentInteractionListener) {
            listener = (OnSentenceCardFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSentenceCardFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(LANGUAGE_CODE_KEY)) {
            languageCode = getArguments().getString(LANGUAGE_CODE_KEY);
        } else {
            throw new RuntimeException(requireContext().toString()
                    + " must pass valid language code");
        }
        sentence = getArguments().getString(SENTENCE_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        android.view.View view = inflater.inflate(R.layout.activity_sentence_card, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.loadData(languageCode, sentence);
        Log.d(TAG, "onViewCreated: " + sentence);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        presenter.pageChanged(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void showData() {
        viewPager.setAdapter(languagePageAdapter);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNumbering(int currentPage, int pageCount) {
        listener.onPageChange(currentPage, pageCount);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        presenter.onViewDestroy();
        super.onDestroyView();
    }

    public String getCurrentSentence() {
        return presenter.getCurrentSentence();
    }
}