package com.zzflybird.service;

import com.spring.Component;
import com.spring.Scope;

/**
 * @Auther: zzflybird
 * @Date: 2022/5/26 - 05 - 26 - 20:15
 * @Description: com.zzflybird.service
 * @version: 1.0
 */
@Component(value = "userService")
//@Scope(value = "prototype") // 没有Scope注解，默认单例的
//@Scope(value = "singleton")
public class UserService {

    public void test()
    {
        System.out.println("test");
    }
}
