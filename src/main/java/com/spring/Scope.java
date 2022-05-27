package com.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Auther: zzflybird
 * @Date: 2022/5/26 - 05 - 26 - 21:47
 * @Description: com.spring
 * @version: 1.0
 */

/**
 * 标注是单例的，还是原型的
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Scope {
    String value() default "";
}
