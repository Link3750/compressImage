package com.util.ano;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: 雪竹
 * @description: TODO
 * @dateTime: 2023/9/5 15:32
 **/
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CompressAnnotation {

    String finalLong() default "";

    String finalWidth() default "";

    String finalSize() default "200";
}
