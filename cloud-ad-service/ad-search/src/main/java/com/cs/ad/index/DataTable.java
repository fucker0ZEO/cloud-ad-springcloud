package com.cs.ad.index;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fucker
 * DataTable 索引服务的缓存/目录工具类，避免多次写注入bean
 *
 * PriorityOrdered 为初始化的bean优先级排序
 * 在spring中想获取什么spring相关信息就要实现一个aware
 * 这里实现的就是ApplicationContextAware
 * ApplicationContext代表应用程序上下文
 * 通过应用程序上下文就可以得到spring帮忙初始化的各个bean
 */
@Component
public class DataTable implements ApplicationContextAware, PriorityOrdered {

    /**定义applicationContext*/
    private static ApplicationContext applicationContext;


    /**定义map保存索引对象
     * key为class，代表索引对象*/
    public static final Map<Class,Object> dataTableMap =
            new ConcurrentHashMap<>();

    /**setApplicationContext
     *完成的ApplicationContext初始化，将其注入
     * */
    @Override
    public void setApplicationContext(
            ApplicationContext applicationContext) throws BeansException {

        /*类对象，进行初始化*/
        DataTable.applicationContext = applicationContext;
    }

    @Override
    public int getOrder() {
        /*定义DataTable这个 bean为最高初始化的优先级
        * HIGHEST_PRECEDENCE 是一个常量
        * 常量的值越小，优先级越高*/
        return PriorityOrdered.HIGHEST_PRECEDENCE;
    }

    /**通过bean的名字获取到传入的bean*/
    @SuppressWarnings("all")
    private static <T> T bean(String beanName){
        return (T) applicationContext.getBean(beanName);
    }

    /**通过Class的类型获取到传入的bean*/
    @SuppressWarnings("all")
    private static <T> T bean(Class clazz){
        return (T) applicationContext.getBean(clazz);
    }

    /**提供方法给想要获取索引服务的服务类一个获取服务/缓存的方法
     * 根据索引的类型，获取对应的索引对象，即并发map
     * */
    @SuppressWarnings("all")
    public static <T> T of(Class<T> clazz){
        /*自定义的map保存索引对象
        * 通过get方法传入索引对象的类型来获取对应的索引对象*/
        T instance = (T) dataTableMap.get(clazz);
        /*map最初是空的hashMap，因此可能获取不到值*/

        /*如果存在*/
        if (null  != instance){
            /*存在则返回索引对象*/
            return instance;
        }
        /*不存在则put值到map中
        * put的key为索引对象的类型
        * put的value为前面定义的bean(clazz)方法，该方法的返回值为索引对象*/
        dataTableMap.put(clazz,bean(clazz));
        return (T) dataTableMap.get(clazz);
    }


    /*of 方法是其该类的核心方法
    传入类型
    根据类型到本来中new的dataTableMap中去寻找对应的对象
    如果没有找到，则使用put方法将类型，和bean(clazz)填充到map中
    【这里的bean方法会调用
    applicationContext.getBean(clazz)
    spring的应用程序上下文中必然有着对应的bean，即类型对应的对象
    】
    最后再调用一次dataTableMap的get方法，这个时候已经有对象了，可以成功返回对应的索引对象
    */


}
