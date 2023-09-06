package com.example.compressimage.util.ano;

import java.lang.annotation.*;

/**
 * @author: 雪竹
 * @description: TODO
 * @dateTime: 2023/9/5 15:32
 **/
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CompressAnnotation {
    String finalSize() default "200";

    String scale() default "0.9";

    String defaultQuantity() default  "0.9";

    String quantityGap() default  "0.05";
}
