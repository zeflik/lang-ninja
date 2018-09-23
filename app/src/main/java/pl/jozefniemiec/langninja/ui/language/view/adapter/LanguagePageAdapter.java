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
import pl.jozefniemiec.langninja.resources.AndroidResourceManager;

public class LanguagePageAdapter extends PagerAdapter {

    private final Context context;

    @BindView(R.id.languagePageSentence)
    TextView sentence;
    @BindView(R.id.ivFlagOnCard)
    ImageView flag;

    public LanguagePageAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.language_page, container, false);
        ButterKnife.bind(this, layout);
        sentence.setText("sentence");
        int id = new AndroidResourceManager(context.getResources()).getFlagId("de");
        flag.setImageResource(id);
        container.addView(layout);
        return layout;
    }

    @Override
    public int getCount() {
        return 3;
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
}