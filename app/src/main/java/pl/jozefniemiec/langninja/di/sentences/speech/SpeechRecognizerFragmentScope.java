package pl.jozefniemiec.langninja.di.sentences.speech;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

@Scope
@Retention(RetentionPolicy.CLASS)
public @interface SpeechRecognizerFragmentScope {
}
