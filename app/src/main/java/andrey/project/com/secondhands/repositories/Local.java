package andrey.project.com.secondhands.repositories;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by procreationsmac on 09/07/2018.
 */

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface Local {
}
