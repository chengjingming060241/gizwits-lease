package com.gizwits.lease.util;

import com.gizwits.boot.utils.DateKit;
import org.apache.commons.lang.StringUtils;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Mahone Wu on 2015/9/6.
 */
public class DateUtil {

    public final static String dateFormat = "yyyy-MM-dd";
    public final static String dateNumberFormat = "yyyyMMdd";
    public final static String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    public final static String dateTimeNumberFormat = "yyyyMMddHHmmss";
    public final static String timeFormat = "HH:mm:ss";
    public final static String timeNumberFormat = "HHmmss";


    private static Calendar forGetField = Calendar.getInstance();

    public static final int STRIP_LEADING_ZERO_FIELD_STYLE = 0x01;

    public static final int STRIP_TAILING_ZERO_FIELD_STYLE = 0x02;

    public static final int RECURSIVE_STRIP_FLAG = 0x04;

    public static final int RECURSIVE_STRIP_LEADING_ZERO_FIELD_STYLE = STRIP_LEADING_ZERO_FIELD_STYLE | RECURSIVE_STRIP_FLAG; // 0x01 | 0x04

    public static final int DEFAULT_STYLE = 0x00;


    public static String getDateStr(Date date) {
        return toStr(date, DateUtil.dateFormat);
    }

    public static String toStr(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 获取某一天的开始时间
     *
     * @param startTime
     * @return
     */
    public static Date getDayStartTime(Date startTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date start = calendar.getTime();
        return start;

    }

    /**
     * 获取的时间为：当前时间的字段加上diff，diff可以是正数和负数，如果diff为0，表示获取当前时间。
     * getDate(Calendar.MINUTE, -10)表示获取十分钟前的时间
     *
     * @param field
     * @param diff
     * @return
     */
    public static Date getDate(int field, int diff) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(field, diff);
        return calendar.getTime();
    }

    public static int getField(Date time, int field) {
        forGetField.setTime(time);
        return forGetField.get(field);
    }

    public static Date setField(Date time, int field, int val) {
        Calendar forRet = Calendar.getInstance();
        forRet.setTime(time);
        forRet.set(field, val);
        return forRet.getTime();
    }

    /**
     * 获取某一天的结束时间
     *
     * @param endTime
     * @return
     */
    public static Date getDayEndTime(Date endTime) {
        Calendar calendar = Calendar.getInstance();
        if (null == endTime) {
            endTime = new Date();
        }
        calendar.setTime(endTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date start = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.SECOND, -1);

        Date end = calendar.getTime();
        return end;
    }

    public static Date String2Date(String time, String pattern) {
        if (time == null) return null;
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        Date times = null;
        try {
            times = sf.parse(time);
        } catch (ParseException e) {

        }
        return times;
    }

    /**
     * 计算c1与c2相差多少天
     *
     * @param c1
     * @param c2
     * @return 同一天返回0，隔天返回1，以此类推
     */
    public static int dayDiff(Calendar c1, Calendar c2) {
        Date d1 = getDayStartTime(c1.getTime());
        Date d2 = getDayStartTime(c2.getTime());
        return dayDiff(d1, d2);
    }

    public static int dayDiff(Date d1, Date d2) {
        long sum;
        long dt1 = d1.getTime();
        long dt2 = d2.getTime();
        long diff = Math.abs(dt2 - dt1);
        sum = diff / (1000 * 24 * 60 * 60);
        return (int) sum;
    }

    public static Date String2DateByDateTimePattern(String time) {
        return String2Date(time, "yyyy-MM-dd HH:mm:ss");
    }

    //将字符串转化为时间
    public static boolean StringToDate(String time, Date startTime) {
        DateFormat df1 = DateFormat.getDateInstance(2, Locale.CHINESE);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df1.format(startTime));
        String st = df1.format(startTime) + " " + time.toString();
        Date times = null;
        try {
            times = sf.parse(st);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (times.equals(startTime)) {
            return true;
        } else {
            return times.before(startTime);
        }
    }

    //由于在开始时间哪里如果时间相等需要返回的是true,但是在结束时间这里如果时间相等要返回false,所以这里就分开写了
    //将字符串转化为时间
    public static boolean StringToDate2(String time, Date orderStartTime) {
        Date compareTime = null;
        if ("00:00:00".equals(time)) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(orderStartTime);
            cal.add(Calendar.DAY_OF_YEAR, 1);
            compareTime = cal.getTime();
        } else {
            compareTime = orderStartTime;
        }
        DateFormat df1 = DateFormat.getDateInstance(2, Locale.CHINESE);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df1.format(orderStartTime));
        String st = df1.format(compareTime) + " " + time.toString();
        Date times = null;
        try {
            times = sf.parse(st);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (times.equals(orderStartTime)) {
            System.out.println("有没有进入：" + times.equals(orderStartTime));
            return false;
        } else {
            return times.before(orderStartTime);
        }
    }

    public static String getTimestampString(Date date) {
        String str = "";
        if (null == date) {
            str = "";
        } else {
            str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
        }
        return str;
    }

    public static String getTimestamp(Date date, int i) {
        String s = "";
        if (1 == i) {
            s = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
        } else if (2 == i) {
            s = (new SimpleDateFormat("MM/dd")).format(date);
        } else if (3 == i) {
            s = (new SimpleDateFormat("yyyyMMddHHmmss")).format(date);
        }
        return s;
    }

    public static String getTimestampStringMintue(Date date) {
        String s = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).format(date);
        return s;
    }

    /**
     * 增加天数
     *
     * @param date
     * @param day
     * @return
     */
    public static Date addDay(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, day);
        return cal.getTime();
    }

    public static Date addMonth(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, day);
        return cal.getTime();
    }


    /**
     * @param time
     * @param day
     * @param startTime 传入的起始时间
     * @return
     * @descroption:这里进行把Time转换为Date
     */
    public static Date TimeToDate(Time time, int day, Date startTime) {
        DateFormat df1 = DateFormat.getDateInstance(2, Locale.CHINESE);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();//要看看传入的值是否是从今天算起
        cal.setTime(startTime);
        cal.add(Calendar.DAY_OF_MONTH, day);

        String date = df1.format(cal.getTime()) + " " + time.toString();
        Date times = null;
        try {
            times = sf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }

    /**
     * @param day
     * @return
     * @description：这里是日期的加减（月·）
     */
    public static Map<Object, Object> DateWeek(int day, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, day);
        int week = cal.get(Calendar.DAY_OF_WEEK);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date weekDay = cal.getTime();
        Map<Object, Object> map = new HashMap<Object, Object>(2);
        map.put("week", week);
        map.put("weekDay", weekDay);
        return map;
    }

    /**
     * @param date
     * @param minute
     * @return
     * @description:这里是把时间加减（加分钟）
     */
    public static Date DateMinuteModified(Date date, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minute);
        return cal.getTime();
    }

    /**
     * @param date
     * @param second
     * @return
     * @description:这里是把时间加减（加秒）
     */
    public static Date DateSecondModified(Date date, int second) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, second);
        return cal.getTime();
    }

    /**
     * 加小时
     *
     * @param date
     * @param hour
     * @return
     */
    public static Date DateHoursModified(Date date, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);
        return cal.getTime();
    }

    /**
     * 加tian
     *
     * @param date
     * @param day
     * @return
     */
    public static Date DateDayModified(Date date, int day) {
        int hour = day * 24;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);
        return cal.getTime();
    }

    public static Date getHalfTime(Date d) {
        String d1 = getTimestampString(d);
        DateFormat df1 = DateFormat.getDateInstance(2, Locale.CHINESE);
        String date = df1.format(d) + " " + "00:00:00";
        Date returnDate = null;
        try {

            Date startTime = getSdf("yyyy-MM-dd HH:mm:ss").parse(date);
            Date endTime = DateMinuteModified(startTime, 30);

            while (!(CompareDate(d, startTime) && CompareEndDate(d, endTime))) {
                if (CompareDate(startTime, d) && CompareEndDate(endTime, d)) {
                    returnDate = endTime;
                    break;
                }
//					System.out.println(DateUtils.CompareDate(startTime,d));
//					System.out.println(DateUtils.CompareEndDate(endTime,d));
                startTime = endTime;
                endTime = DateMinuteModified(startTime, 30);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnDate;
    }


    /**
     * @param time
     * @param startTime
     * @return
     * @desription：时间的比较；
     */
    public static boolean CompareDate(Date time, Date startTime) {
        if (time.equals(startTime)) {
            return true;
        } else {
            return time.before(startTime);
        }
    }

    /**
     * @param time
     * @param startTime
     * @return
     */
    public static boolean CompareEndDate(Date time, Date startTime) {
        if (time.equals(startTime)) {
            return true;
        } else {
            return time.after(startTime);
        }
    }

    private static ThreadLocal<Map<String, DateFormat>> threadDateFormat = new ThreadLocal() {
        @Override
        protected Object initialValue() {
            return new HashMap<>();
        }
    };

    /**
     * 获取与线程绑定的DateFormat来解决SimpleDateFormat不是线程安全
     *
     * @param pattern
     * @return
     */
    public static DateFormat getDateFormat(String pattern) {
        Map<String, DateFormat> dateFormatMap = threadDateFormat.get();
        DateFormat df = dateFormatMap.get(pattern);
        if (df != null) return df;
        df = new SimpleDateFormat(pattern);
        dateFormatMap.put(pattern, df);

        return df;
    }


    // 下面的方式不行
    /**
     * 锁对象
     */
    private static final Object lockObj = new Object();
    /**
     * 存放不同的日期模板格式的sdf的Map
     */
    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

    /**
     * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
     *
     * @param pattern
     * @return
     */
    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);

        // 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
        if (tl == null) {
            synchronized (lockObj) {
                tl = sdfMap.get(pattern);
                if (tl == null) {
                    // 只有Map中还没有这个pattern的sdf才会生成新的sdf并放入map
                    //System.out.println("put new sdf of pattern " + pattern + " to map");

                    // 这里是关键,使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
                    tl = new ThreadLocal<SimpleDateFormat>() {

                        @Override
                        protected SimpleDateFormat initialValue() {

                            return new SimpleDateFormat(pattern);
                        }
                    };
                    sdfMap.put(pattern, tl);
                }
            }
        }

        return tl.get();
    }


    /**
     * @param time
     * @param day
     * @param kaishiTime
     * @return
     */
    public static Date StringTimeToDate(String time, int day, Date kaishiTime) {
        DateFormat df1 = DateFormat.getDateInstance(2, Locale.CHINESE);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();//要看看传入的值是否是从今天算起
        cal.setTime(kaishiTime);
        cal.add(Calendar.DAY_OF_MONTH, day);

        String date = df1.format(cal.getTime()) + " " + time.toString();
        Date times = null;
        try {
            times = sf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }

    /**
     * @param time
     * @param changeTime
     * @return
     */
    public static Date dateTodate(Date time, Date changeTime) {
        DateFormat df1 = DateFormat.getDateInstance(2, Locale.CHINESE);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();//要看看传入的值是否是从今天算起
        cal.setTime(time);
        Date times = null;
        try {
            String date = df1.format(cal.getTime()) + " " + sf.format(changeTime).substring(11, 19);
            times = sf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }

    /**
     * @param startTime
     * @param endTime
     * @return
     * @description:计算两个时间的时间差，转换为小数，比如2.5小时
     */
    public static double DateSubtraction(Date startTime, Date endTime) {
        double result = (endTime.getTime() - startTime.getTime()) / (60000 * 60.0);
        return result;
    }


    /**
     * 计算分钟
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static double calcuteMin(Date startTime, Date endTime) {
        if (null == startTime || null == endTime) {
            return 0;
        }
        long st = startTime.getTime();
        long end = endTime.getTime();
        double min = (end - st) / (1000 * 60.0);
        System.out.println(min);
        return min;
    }

    /**
     * 具体时间的分钟加减
     *
     * @param date
     * @param minute
     * @return
     */
    public static Date HourModified(Date date, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minute);
        return cal.getTime();
    }


    /**
     * 格式花时间
     *
     * @param time
     * @param pattern
     * @return
     */
    public static Date parse(String time, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
            return sdf.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String date2HumanText(Date date, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 这个纯属测试用的时候拼凑时间用的
     *
     * @param dateString
     * @param type
     * @return
     */
    public static Date yeatMonthDay(String dateString, Integer type) {
        DateFormat df1 = DateFormat.getDateInstance(2, Locale.CHINESE);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String st = "2015-01-01 00:00:00";
        if (1 == type) {
            st = dateString + " " + "00:00:00";
        } else if (2 == type) {
            st = dateString + " " + "23:59:59";
        }
        Date date = null;
        try {
            date = sf.parse(st);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    public static long diffTimeToLong(Date startTime, Date endTime) {
        if (null == startTime || null == endTime) {
            return 0;
        }
        long diff = endTime.getTime() - startTime.getTime();
        return diff;
    }


    public static String diffLongString(long diff) {
        String time = "";
        if (0 == diff) {
            return "00:00:00";
        }
        int hours = (int) diff / (1000 * 60 * 60);
        int min = (int) (diff - hours * 60 * 60 * 1000) / (1000 * 60);
        int seconds = (int) (diff - hours * 60 * 60 * 1000) % (1000 * 60) / 1000;

        if (0 == min) {
            time = "00:" + "00:" + addZero(seconds, 2, "before");
        } else if (hours == 0) {
            time = "00:" + addZero(min, 2, "before") + ":" + addZero(seconds, 2, "before");
        } else {
            time = hours + ":" + addZero(min, 2, "before") + ":" + addZero(seconds, 2, "before");
        }
        return time;
    }

    public static String addZero(Object str, int length, String ba) {
        String result = "";
        if ("before".equals(ba)) {//字符串前面补0
            if (str.toString().length() < length) {
                for (int i = 0; i <= (length - str.toString().length()); i++) {
                    str = "0" + str;
                }
            } else {
                return str.toString();
            }
        } else {//字符串后面补0

            if (str.toString().length() <= length) {
                for (int i = 0; i <= (length - str.toString().length()); i++) {
                    str = str + "0";
                }
            } else {
                return str.toString();
            }
        }
        return str.toString();
    }

    /**
     * @param startTime
     * @param endTime
     * @return
     */
    public static int getDayByTwoDate(Date startTime, Date endTime) {
        if (null == startTime || null == endTime) {
            return 0;
        }
        long start = startTime.getTime();
        long end = endTime.getTime();
        long diff = end - start;
        long longDay = diff / (60 * 60 * 24 * 1000);
        int day = Integer.parseInt((longDay + "")) + 1;
        return day;
    }

    public static Date min(Date d1, Date d2) {
        long compare = d1.getTime() - d2.getTime();
        if (compare > 0) return d2;
        return d1;
    }

    public static Date max(Date d1, Date d2) {
        long compare = d1.getTime() - d2.getTime();
        if (compare > 0) return d1;
        return d2;
    }

    /**
     * 将秒转换为人类可读的时间，比如
     * 183秒转换后是"3分3秒"。
     * 可以使用%02s这样的格式，%可作为%的转义符号
     *
     * @param second 需要转换的秒数
     * @param format 类型与String.format，支持的占位符有%s（秒），%m（分），%h（小时）
     * @param style
     * @return
     */
    public static String second2Human(int second, String format, int style) {
        int s = 0, m = 0, h = 0, tmp = 0;

        tmp = second;
        s = tmp % 60;

        tmp /= 60;
        m = tmp % 60;

        tmp /= 60;
        h = tmp;

        StringBuilder content = new StringBuilder();
        StringBuilder buffer = new StringBuilder();
        boolean flag = false;
        int value = -1;
        char pre = '\0';

        List<String> partitionContent = new LinkedList<>();
        List<Integer> partitionValue = new LinkedList<>();

        for (int i = 0; i <= format.length(); i++) {
            if (i == format.length()) {
                // 已经结束
                content.append(buffer);
                addNewPartition(partitionContent, partitionValue, content.toString(), value);
                break;
            }
            char c = format.charAt(i);
            buffer.append(c);
            if (c == '%') {
                if (flag) {
                    if (pre == c) {
                        content.append(c);
                        flag = false;
                        buffer.setLength(0);
                        pre = c;
                        continue;
                    } else {
                        // %xxx%
                        buffer.setLength(buffer.length() - 1);
                        content.append(buffer);
                        buffer.setLength(0);
                        buffer.append(c);
                        flag = true;
                        pre = c;
                        continue;
                    }
                } else {
                    flag = true;
                    if (buffer.length() > 0) {
                        content.append(buffer.substring(0, buffer.length() - 1));
                        buffer.setLength(0);
                        buffer.append(c);
                    }
                    pre = c;
                    continue;
                }
            }
            if (flag && (c == 's' || c == 'm' || c == 'h')) {
                addNewPartition(partitionContent, partitionValue, content.toString(), value);
                value = -1;
                content.setLength(0);
            }
            if (c == 's' && flag) {
                value = parseSecond2HumanField(c, s, m, h, content, buffer);
                buffer.setLength(0);
                flag = !flag;
            } else if (c == 'm' && flag) {
                value = parseSecond2HumanField(c, s, m, h, content, buffer);
                buffer.setLength(0);
                flag = !flag;
            } else if (c == 'h' && flag) {
                value = parseSecond2HumanField(c, s, m, h, content, buffer);
                buffer.setLength(0);
                flag = !flag;
            }
            pre = c;
        }

        return applyStyleForSecond2Human(partitionContent, partitionValue, style);
    }

    private static void addNewPartition(List<String> partitionContent, List<Integer> partitionValue, String content, int value) {
        if (content.length() == 0 && value == -1) return;
        partitionContent.add(content);
        partitionValue.add(value);
    }

    private static String applyStyleForSecond2Human(List<String> partitionContent, List<Integer> partitionValue, int style) {
        StringBuilder text = new StringBuilder();
        int size = Math.max(partitionContent.size(), partitionValue.size());
        String contents[] = new String[size];
        Integer values[] = new Integer[size];

        // 复制
        partitionContent.toArray(contents);
        partitionValue.toArray(values);

        // 不够填充
        for (int i = partitionContent.size(); i < size; i++) {
            contents[i] = "";
        }

        for (int i = partitionValue.size(); i < size; i++) {
            values[i] = -1;
        }

        int leadingZeroIndex = -1;
        int tailingZeroIndex = -1;

        for (int i = 0; i < size; i++) {
            if (style == DEFAULT_STYLE) {
                text.append(contents[i]);
                continue;
            }
            boolean ignore = false;

            if (values[i].intValue() > 0)
                leadingZeroIndex = -2;
            else if (leadingZeroIndex > -2 && values[i].intValue() == 0)
                leadingZeroIndex++;

            for (int j = size - 1; j >= i; j--) {
                if (values[j].intValue() > 0)
                    tailingZeroIndex = -1;
                else if (values[j].intValue() == 0)
                    tailingZeroIndex++;
            }

            if ((style & STRIP_LEADING_ZERO_FIELD_STYLE) > 0
                    && (
                    leadingZeroIndex == 0
                            || (leadingZeroIndex > 0 && (style & RECURSIVE_STRIP_FLAG) > 0)
            ))
                ignore = true;
            if ((style & STRIP_TAILING_ZERO_FIELD_STYLE) > 0
                    && (
                    tailingZeroIndex == 0
                            || (tailingZeroIndex > 0 && (style & RECURSIVE_STRIP_FLAG) > 0)
            ))
                ignore = true;

            if (!ignore) {
                text.append(contents[i]);
            }
        }

        if (text.length() == 0) {
            // 没有发现任何有效数据
            for (int i = size - 1; i >= 0; i--) {
                if (values[i] >= 0) {
                    text.append(contents[i]);
                    break;
                }
            }
        }

        return text.toString();
    }

    private static int parseSecond2HumanField(char field, int s, int m, int h, StringBuilder appender, StringBuilder format) {
        String ft = format.substring(1, format.length() - 1);
        String pattern = "%" + ft + "d";
        int v = -1;
        switch (field) {
            case 's':
                if (ft.equals("") || StringUtils.isNumeric(ft)) {
                    v = s;
                }
                break;
            case 'm':
                if (ft.equals("") || StringUtils.isNumeric(ft)) {
                    v = m;
                }
                break;
            case 'h':
                if (ft.equals("") || StringUtils.isNumeric(ft)) {
                    v = h;
                }
                break;
        }
        if (v != -1)
            appender.append(String.format(pattern, v));
        else
            appender.append(format.toString());

        return v;
    }

    public static List<Date> getDateSequence(Date from, Date to) {
        if (from.after(to)) {
            throw new IllegalArgumentException("[from] can't be after [to]");
        }
        int dis = dayDiff(DateKit.getDayStartTime(from), DateKit.getDayEndTime(to));
        List<Date> dates = new ArrayList<>(dis + 1);
        for (int i = 0; i <= dis; i++) {
            if (i == 0) {
                dates.add(from);
            } else {
                dates.add(DateUtil.addDay(from, i));
            }
        }
        return dates;
    }

    public static int monthDiff(Date from, Date to) {
        if (from.after(to)) {
            Date swap = from;
            from = to;
            to = swap;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(from);

        int fy = cal.get(Calendar.YEAR); // year
        int fm = cal.get(Calendar.MONTH); // month

        cal.setTime(to);

        int ty = cal.get(Calendar.YEAR); // year
        int tm = cal.get(Calendar.MONTH); // month

        // 2016 12
        // 2017 01
        if (tm < fm) { // fm > tm 表示结束时间的月份小于开始时间，需要向结束时间的年份借一个年
            tm += 12; // 借一年就是12个月
            ty--; // 年份被借一个月后年份减一
        }
        int dm = tm - fm;
        int dy = ty - fy;
        return dy * 12 + dm;
    }

    public static List<Date> getMonthSequence(Date from, Date to) {
        int dis = monthDiff(from, to);
        List<Date> dates = new ArrayList<>(dis + 1);
        for (int i = 0; i <= dis; i++) {
            if (i == 0) {
                dates.add(from);
            } else {
                dates.add(DateUtil.addMonth(from, i));
            }
        }
        return dates;
    }

    public static void main(String args[]) {
//        System.out.println(getTimestampString(null));
//        System.out.println(StringTimeToDate("12:30:00",0,new Date()));

//        System.out.println(parse("12:30","HH:mm").getTime());
//        System.out.println(new Time(parse("12:30","HH:mm").getTime()));

//        System.out.println(DateHoursModified(new Date(),1));

//        Date startTime = DateMinuteModified(new Date(),10);
//        Date endTime = DateMinuteModified(new Date(),12);
//
//        Date startTime1 = DateSecondModified(new Date(), 10);
//        Date endTime1 = DateSecondModified(new Date(),12);
//
//        long diff1 = diffTimeToLong(startTime, endTime);
//        long diff2 = diffTimeToLong(startTime1,endTime1);
//        long sum = diff1 + diff2;
//        System.out.println(diffLongString(diff1));
//        System.out.println(diffLongString(diff2));
//        System.out.println(diffLongString(sum));

//        System.out.println(DateDayModified(new Date(), 2));
//        Date startDate = yeatMonthDay("2015-10-30", 1);
//        Date endtDate = yeatMonthDay("2015-10-30", 1);
//        long day = getDayByTwoDate(startDate, endtDate);
//        System.out.println("day--->"+day);
//
//
//        Date date = new Date();
//        Date end = DateSecondModified(date, 50);
//       double min =  DateKit.calcuteMin(date, end);
//        double aa = 50 / 60.0 * min;
//        System.out.println(aa);
//
//        System.out.println(parse("12:30","HH:ss"));
//
//
//        System.out.println(addZero("3", 3, "before"));

        System.out.println(addDay(new Date(), -7));

        System.out.println(DateUtil.getDayStartTime(new Date()));

        System.out.println(DateUtil.getDayEndTime(new Date()));

    }

}
