package com.zzflybird;

import com.spring.ZzflybirdApplicationContext;
import com.zzflybird.service.UserService;

/**
 * @Auther: zzflybird
 * @Date: 2022/5/26 - 05 - 26 - 20:14
 * @Description: com.zzflybird
 * @version: 1.0
 */
public class Test {
    public static void main(String[] args) throws Exception {

        // 1 扫描---》2 创建单例Bean
        ZzflybirdApplicationContext context = new ZzflybirdApplicationContext(AppConfig.class);

        UserService userService = (UserService) context.getBean("userService");
        System.out.println(context.getBean("userService"));
        System.out.println(context.getBean("userService"));
        System.out.println(context.getBean("userService"));
        userService.test();
    }
}
