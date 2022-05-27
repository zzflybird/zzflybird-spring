package com.spring;

/**
 * @Auther: zzflybird
 * @Date: 2022/5/26 - 05 - 26 - 20:17
 * @Description: com.spring
 * @version: 1.0
 */

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * spring的容器类
 */
public class ZzflybirdApplicationContext {

    private Class configClass;

    /**
     * beanName ----> (BeanDefinition)
     * beanDefinitionMap 中 保存了所有的 Bean
     */
    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    /**
     * 单例池：用来存单例对象，单例Bean的, 方便getBean()时获取单例Bean；
     * beanName----->singletonBean
     */
    private Map<String, Object> singletonObjectsMap = new HashMap<>();


    public ZzflybirdApplicationContext(Class<?> configClass) {
        this.configClass = configClass;

        // 1. 扫描 设置的包下的所有类
        scan(configClass);
        
        // 2. 创建单例Bean：遍历 beanDefinitionMap
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();

            if (beanDefinition.getScope().equals("singleton"))
            {
                // 容器初始化时，直接创建单例bean, 创建后的单例Bean需要保存下来
                Object bean = createBean(beanName, beanDefinition);
                // 保存到单例池singletonObjects中，方便后面getBean()时直接从单例池中获取
                singletonObjectsMap.put(beanName, bean);
            }
        }
    }

    /**
     * 创建Bean: 通过反射，根据类的构造器来创建Bean，类可以通过getType()方法得到
     * @param beanName
     * @param beanDefinition
     * @return
     */
    public Object createBean(String beanName, BeanDefinition beanDefinition)
    {
        Class clazz = beanDefinition.getType();

        Object instance = null;
        try {
            // 使用类的无参的构造方法，创建实例对象
            instance = clazz.getConstructor().newInstance();
            return instance;

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return instance;
    }


    private void scan(Class<?> configClass) {
        // 1、判断configClass这个类上是否有@ComponentScan注解
        if (configClass.isAnnotationPresent(ComponentScan.class))
        {
            ComponentScan componentScan = configClass.getAnnotation(ComponentScan.class);
            String value = componentScan.value();
//            System.out.println(value);//com.zzflybird.service

            // 有了包的路径，就去找这个路径下的所有的类名
            // 注意：是要找classes下的class文件
            String path = value.replace(".", "/");
//            System.out.println(path);

            ClassLoader classLoader = ZzflybirdApplicationContext.class.getClassLoader();
            // 找到具有给定名称的资源。资源是一些数据（图像、音频、文本等），可以被类代码访问，其方式与代码的位置无关。
            // 资源的名称是一个用'/'分隔的路径名，用来识别资源。
            URL resource = classLoader.getResource(path);
            // file:/F:/xxx/xxx/xxx/zzflybird-spring/target/classes/com/zzflybird/service
//            System.out.println(resource);
            // /F:/xxx/xxx/xxx/zzflybird-spring/target/classes/com/zzflybird/service
            assert resource != null;
//            System.out.println(resource.getPath());

            // 找到资源路径后，打开
            File file = new File(resource.getFile());

            // 判断file是否是一个目录，如果是一个目录就获得目录下的所有文件
            if (file.isDirectory())
            {
                for (File f :  file.listFiles()) {
//                    System.out.println(f.getAbsolutePath());
                    // F:\xxx\xxx\xxx\zzflybird-spring\target\classes\com\zzflybird\service\UserService.class
                    String absolutePath = f.getAbsolutePath();
                    absolutePath= absolutePath.substring(absolutePath.indexOf("com"), absolutePath.indexOf(".class"));
//                    System.out.println(absolutePath);// com\zzflybird\service\UserService
                    absolutePath = absolutePath.replace("\\", ".");
//                    System.out.println(absolutePath); // com.zzflybird.service.UserService

                    try {
                        // 使用类加载器加载类，通过全限定类名
                        Class<?> clazz = classLoader.loadClass(absolutePath);
//                        System.out.println("扫描到的全限定类名:"+clazz);

                        // 判断加载的类上是否有@Component注解
                        if (clazz.isAnnotationPresent(Component.class))
                        {
                            // 得到Component注解的value，如果为空，则直接设置beanName
                            Component componentAnnotation = clazz.getAnnotation(Component.class);
                            String beanName = componentAnnotation.value();
//                            if ("".equals(beanName))
//                            {
//                                beanName =
//                            }

                            // 有Component注解，说明是一个Bean
//                            System.out.println("有Component注解："+clazz);
                            // 创建 beanDefinition 对象
                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setType(clazz); // 设置 beanDefinition 的类型

                            // 当前仅知道该类上有Component注解，但是不知道是单例的还是原型的, 所以要继续判断Scope
                            if (clazz.isAnnotationPresent(Scope.class))
                            {
                                // 如果有scope注解，先把Scope注解的值保存到beanDefinition中，无论其value为单例还是原型prototype
                                Scope scopeAnnotation = clazz.getAnnotation(Scope.class);
                                String scopeValue = scopeAnnotation.value();
                                System.out.println(scopeValue);
                                beanDefinition.setScope(scopeValue); // 设置 beanDefinition 的作用域, 保存Scope注解的值
                            } else
                            {
                                // 没有scope注解，bean就是单例的
                                beanDefinition.setScope("singleton");
                            }

                            // 不是直接创建bean， 而是把 beanName--->beanDefinition 保存在 beanDefinitionMap 中
                            beanDefinitionMap.put(beanName, beanDefinition);

                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 通过传入的字符串beanName，获得Bean对象
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) throws Exception {
        // beanName ----> UserService
        // 如果这里再根据传入的 beanName，再去解析，得到类对象，就太麻烦了，因为这些在扫描的时候已经做过了
        // 所以构建 beanDefinition 类

        // 如果beanName不在 beanDefinitionMap 中，说明没有这个Bean，表示beanName是错误的
        if (!beanDefinitionMap.containsKey(beanName))
        {
            // 没有 Bean， 抛异常
            throw new Exception("没有这个Bean");
        }

        // 如果有 这个beanName对应的Bean，通过 beanName 得到 BeanDefinition
//        System.out.println(beanDefinitionMap.get(beanName));
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);

        // 判断 beanDefinition 是单例的，还是原型的
        if (beanDefinition.getScope().equals("singleton")) {
            // 是单例的, 单例Bean在spring启动时就创建好了，直接从单例池singletonObjectsMap中获取单例Bean
            Object singletonBean = singletonObjectsMap.get(beanName);
            return singletonBean;
        } else
        {
             // 是原型的，每次都直接创建Bean
            Object prototypeBean = createBean(beanName, beanDefinition);
            return prototypeBean;
        }
    }
}
