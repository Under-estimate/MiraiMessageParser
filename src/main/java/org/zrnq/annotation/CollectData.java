package org.zrnq.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a RichMessageType that needs to collect test resource.
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CollectData {
    /**
     * Disabled when publishing.
     * */
    boolean enabled() default true;
}
