package com.wyu.hadoopapi.car;

/**
 * Created by newuser on 2018/7/13.
 */
public class TimeUtils {
    private TimeUtils(){}

    /**
     *
     * @param strTime
     * @return
     */
    public static String getTime(String strTime){
        int index = strTime.indexOf(":");
        return strTime.substring(0,index);
    }
}
