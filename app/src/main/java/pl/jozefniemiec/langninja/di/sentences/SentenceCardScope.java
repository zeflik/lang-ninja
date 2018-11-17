package pl.jozefniemiec.langninja.di.sentences;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

@Scope
@Retention(RetentionPolicy.CLASS)
public @interface SentenceCardScope {
}
