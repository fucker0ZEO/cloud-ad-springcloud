package com.cs.ad.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author fucker
 *
 */
@Slf4j
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

    /**连接符拼接*/
    public static String stringConcat(String... args) {
        /*StringBuilder实现字符串拼接*/
        StringBuilder result = new StringBuilder();
        /*args使用for循环添加到result中*/
        for (String arg : args){
            result.append(arg);
            /*加上关键的连字符*/
            result.append("-");
        }
        /*每次for循环末尾都会多一个连字符，因此需要删除*/
        result.deleteCharAt(result.length() -1);

        return result.toString();
    }

    /**由Binlog打印出的Date为JSON格式，需要转换为Date类型对象
     * 但转换有坑
     *需要解析打印出的Date
     *
     * 工具类解析打印出的Date
     * */
    public static Date parseStringDate(String dateString){

        try {
            //        设置解析格式 DateFormat,传入对应的格式
            DateFormat dateFormat = new SimpleDateFormat(
                    "EEE MMM dd HH:mm:ss zzz yyyy",
//                设置常量信息，US表示美国
                    Locale.US
            );
//            传入需要解析的字符串，并返回结果日期。
//            因为国内是东8区，因此需要对解析后的时间额外－8小时，
//            使用addHours完成加减
            return DateUtils.addHours(
//                    解析时间字符串
                    dateFormat.parse(dateString),
//                    加上-8，即为-8小时
                    -8
            );
        } catch (ParseException e) {
            log.error("parseStringDate error: {}", dateString);
            return null;
        }
    }
}
