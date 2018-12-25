package com.xlm.example.bpp;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValueAppend {
    String value();

    boolean prefix() default true;
}
