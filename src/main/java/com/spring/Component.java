package com.spring;

/**
 * @Auther: zzflybird
 * @Date: 2022/5/26 - 05 - 26 - 20:13
 * @Description: com.spring
 * @version: 1.0
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Component注解，用于声明bean
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {

    String value() default "";

}
