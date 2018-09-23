package pl.jozefniemiec.langninja.ui.language.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.jozefniemiec.langninja.R;
import pl.jozefniemiec.langninja.ui.language.presenter.SentenceCardPresenter;

public class SentencesPageAdapter extends PagerAdapter implements SentencesItemView {

    private final Context context;
    private final SentenceCardPresenter presenter;

    @BindView(R.id.languagePageSentence)
    TextView sentence;

    @BindView(R.id.ivFlagOnCard)
    ImageView flag;

    public SentencesPageAdapter(Context context, SentenceCardPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.language_page, container, false);
        ButterKnife.bind(this, layout);
        presenter.loadPageDataAtPosition(position, this);
        container.addView(layout);
        return layout;
    }

    @Override
    public int getCount() {
        return presenter.getPageCount();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public void setFlag(int id) {
        flag.setImageResource(id);
    }

    @Override
    public void setSentence(String sentence) {
        this.sentence.setText(sentence);
    }
}
