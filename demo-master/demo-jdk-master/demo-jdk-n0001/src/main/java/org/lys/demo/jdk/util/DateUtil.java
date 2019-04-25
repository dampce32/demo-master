package org.lys.demo.jdk.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	/**
	 * 将时间转换为时间戳
	 * @createTime: 2019年4月21日 上午9:09:46
	 * @author: lin.yisong
	 * @param s
	 * @return
	 * @throws ParseException
	 */
    public static String dateToStamp(String s,String pattern) throws ParseException{
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }
    
    /* 
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s,String pattern){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
}
