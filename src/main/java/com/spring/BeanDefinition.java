package com.spring;

/**
 * @Auther: zzflybird
 * @Date: 2022/5/26 - 05 - 26 - 22:00
 * @Description: com.spring
 * @version: 1.0
 */

/**
 * BeanDefinition : Bean的定义
 */
public class BeanDefinition {

    /**
     * bean的类型
     */
    private Class type;

    /**
     * bean的作用域: 单例 / 原型
     */
    private String scope;

    /**
     * 是否是懒加载
     */
    private boolean isLazy;


    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isLazy() {
        return isLazy;
    }

    public void setLazy(boolean lazy) {
        isLazy = lazy;
    }
}
