package pl.jozefniemiec.langninja.ui.main.send;

import android.net.Uri;

public interface UserSentenceItemView {

    void setFlag(Uri uri);

    void setSentence(String sentence);

    void setAuthor(String name);
}
