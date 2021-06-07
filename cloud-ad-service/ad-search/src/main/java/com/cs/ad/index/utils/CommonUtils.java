package com.cs.ad.index.utils;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author fucker
 *
 */
public class CommonUtils {
    /**如果传递进来的map中不存在第一个参数K Key，
     * 则利用Supplier<V> factory返回新的对象 <K,V> V
     *
     * 以倒排索引的增加来解释一下流程：
     *
     * map.computeIfAbsent 计算如果不存在，
     * 该方法对map进行了添加，但返回值却为map中的value,而非整个map。
     * 而整个value正好就是并发跳表Set，然后Set赋值给IdS
     * map对应着索引map，整个过程中索引中的值发生了添加，没有放在返回值中
     * factory对应着并发跳表Set，get了set的值，即null
     *
     * 整理一下：
     * 传入key，索引map，并发跳表set
     * 校验key在map中是否存在
     * 不存在则将 <key,set>插入map，此处set=[]，即null
     * 最后返回map的value，即set
     * */
    public static <K,V> V getOrCreate(K key, Map<K, V> map,
                                      Supplier<V> factory){
        System.out.println("工具类最初的map:"+map);
        System.out.println("工具类的factory："+factory.get());
        System.out.println("返回值的map："+map.computeIfAbsent(key,k -> factory.get()));
        System.out.println("工具类添加后的map："+map);


        return map.computeIfAbsent(key,k -> factory.get());
    }
}
