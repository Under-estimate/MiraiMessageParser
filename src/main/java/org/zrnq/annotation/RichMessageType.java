package org.zrnq.annotation;

import net.mamoe.mirai.message.data.RichMessage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a specified type of rich message.
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RichMessageType {
    /**
     * The name of this rich message type.
     * */
    String typeName() default "unknown";
    /**
     * The full name of this rich message type.
     * Including its name and names of all its super types, separated by slash(/).
     * */
    String fullName() default "unknown";
    /**
     * A value for distinguishing the child types of one rich message type.
     * @see org.zrnq.data.RichMessageTemplate#getClassifyingFeatureValue(RichMessage)
     * */
    String[] acceptedFeatureValues() default "none";
}
