package com.cs.ad.index.adUnit;

/**
 * @author fucker
 */
public class AdUnitConstants {

    /**
     * 定义具体支持的流量类型
     * 例如文本，视频，图片
     * 例如开屏
     * 常量值都使用2进制的编排，适合通过位运算加快检索速度
     * */
    public static class POSITION_TYPE{
        /**流量类型--开屏
         * 指开屏广告*/
        public static final int KAIPING =1;
        /**流量类型-- 贴片
         * 电影或者视频开始之前的贴片广告*/
        public static final int TIEPIAN =2;

        /**流量类型-- 中贴
         * 视频播放的中间显示的广告*/
        public static final int TIEPIAN_MIDDLE =4;

        /**流量类型-- 暂停贴
         * 视频播放中点击暂停会播放广告
         * B站和爱优腾最大的差别，应该就是没有贴片和暂停贴了
         * */
        public static final int TIEPIAN_PAUSE =8;

        /**流量类型-- 后贴
         * 视频播放完后，展示一些广告*/
        public static final int TIEPIAN_POST =16;


    }
}
