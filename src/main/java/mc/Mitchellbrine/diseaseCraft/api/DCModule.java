package mc.Mitchellbrine.diseaseCraft.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Mitchellbrine on 2015.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DCModule {

	String id();

	String modid() default "";

	String dcVersion();

	boolean canBeDisabled() default false;

	boolean isEnabled() default true;

}
