package pl.jozefniemiec.langninja.di.profile;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

@Scope
@Retention(RetentionPolicy.CLASS)
public @interface UserProfileActivityScope {
}
