package com.cs.ad.util;

import com.cs.ad.exception.AdException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * @author fucker
 * 常用工具类
 */
public class CommonUtils {

/**MD5生成token*/
    public static String md5(String value){
        return DigestUtils.md5Hex(value).toUpperCase();
    }
    /**自定义所需要的日期格式*/
    public static String[] parseCommonValues = {
            "yyyy-MM-dd","yyyy/MM/dd","yyyy.MM.dd"
    };
    /**String类型的日期格式转化*/
    public static Date parseStringDate(String dateString)
            throws AdException{
        try {
//            将传入的日期格式转化为定义的日期格式
            return DateUtils.parseDate(dateString,parseCommonValues);
        } catch (ParseException e) {
            throw new AdException(e.getMessage());
        }
    }
}
