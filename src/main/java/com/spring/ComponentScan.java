package com.spring;

/**
 * @Auther: zzflybird
 * @Date: 2022/5/26 - 05 - 26 - 20:21
 * @Description: com.spring
 * @version: 1.0
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ComponentScan注解，用于扫描设置的包或类下的所有的bean
 * 该注解的声明周期：（因为需要使用反射获取该注解
 * RetentionPolicy.RUNTIME:在运行时有效(即运行时保留),当运行 Java程序时，
 * JVM会保留注解，加载在内存中了，那么程序可以通过反射获取该注解。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ComponentScan {

    String value() default "";

}
