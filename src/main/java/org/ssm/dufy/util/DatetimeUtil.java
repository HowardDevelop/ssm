package org.ssm.dufy.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * comment go to Window - Preferences - Java - Code Style - Code Templates
 */
public class DatetimeUtil {
    private static Log logger = LogFactory.getLog(DatetimeUtil.class);
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    public final static String DATE_PATTERN_YYYYMMDD = "yyyyMMdd";

    public final static String TIME_PATTERN = "HH:mm:ss";

    public final static String DATE_TIME_PATTERN = DATE_PATTERN + " " + TIME_PATTERN;
    public final static String ORACLE_TIME_PATTERN = "HH24:mi:ss";
    public final static String ORACLE_DATE_TIME_PATTERN = DATE_PATTERN + " " + ORACLE_TIME_PATTERN;

    private static final String DATE_SPLIT = "/";

    /**
     * Date to String
     */
    public static String formatDate(Date date, String formatStyle) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(formatStyle);
            String formatDate = sdf.format(date);
            return formatDate;
        } else {
            return "";
        }
    }

    /**
     * Date to Date
     */
    public static Date formatDate(String formatStyle, Date date) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(formatStyle);
            String formatDate = sdf.format(date);
            try {
                return sdf.parse(formatDate);
            } catch (ParseException e) {
                e.printStackTrace();
                return new Date();
            }
        } else {
            return new Date();
        }
    }

    public static final Date convertStringToDate(String pattern, Locale locale, TimeZone zone, String strDate)
            throws ParseException {
        if (strDate == null || "".equals(strDate))
            return new Date();
        if (locale == null)
            locale = Locale.getDefault();
        if (zone == null)
            zone = TimeZone.getDefault();
        SimpleDateFormat df = new SimpleDateFormat(pattern, locale);
        df.setTimeZone(zone);
        try {
            return df.parse(strDate);
        } catch (ParseException pe) {
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }
    }

    public static final Date convertStringToDate(String strDate) {
        if (strDate == null || "".equals(strDate))
            return new Date();
        Locale locale = Locale.CHINESE;
        try {
            return convertStringToDate(DATE_PATTERN, locale, null, strDate);
        } catch (Exception e) {
            return null;
        }
    }

    public static final Date convertStringToDate(String strDate, String sytle) {
        if (strDate == null || "".equals(strDate))
            return new Date();
        Locale locale = Locale.CHINESE;
        try {
            return convertStringToDate(sytle, locale, null, strDate);
        } catch (Exception e) {
            return null;
        }
    }

    public static final String convertDateToString(String pattern, Locale locale, TimeZone zone, Date aDate) {
        if (locale == null)
            locale = Locale.getDefault();
        if (zone == null)
            zone = TimeZone.getDefault();
        SimpleDateFormat df = new SimpleDateFormat(pattern, locale);
        df.setTimeZone(zone);
        try {
            return df.format(aDate);
        } catch (Exception e) {
            return "";
        }
    }

    public static final String convertDateToString(String pattern, Date aDate) {
        Locale locale = Locale.CHINESE;
        return convertDateToString(pattern, locale, null, aDate);
    }

    /**
     * �ṩyyyy-MM-dd���͵������ַ���ת��
     */
    public static final Date getBeginDate(String beginDate) {
        Locale locale = Locale.CHINESE;
        try {
            return convertStringToDate(DATE_TIME_PATTERN, locale, null, beginDate + " 00:00:00");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * �ṩyyyy-MM-dd���͵������ַ���ת�� ר���ṩWebҳ���������ת�� ������2006-07-27����ת��Ϊ2006-07-28
     * 00:00:00
     */
    public static final Date getEndDate(String endDate) {
        Locale locale = Locale.CHINESE;
        try {
            Date date = convertStringToDate(DATE_TIME_PATTERN, locale, null, endDate + " 00:00:00");
            return new Date(date.getTime() + 24 * 3600 * 1000);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * �ṩyyyy-MM-dd���͵������ַ���ת�� ר���ṩWebҳ��ǰһ������ת�� ������2006-07-27����ת��Ϊ2006-07-26
     * 00:00:00
     */
    public static final Date getPreDate(String endDate) {
        Locale locale = Locale.CHINESE;
        try {
            Date date = convertStringToDate(DATE_TIME_PATTERN, locale, null, endDate + " 00:00:00");
            return new Date(date.getTime() - 24 * 3600 * 1000);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * �ṩyyyy-MM-dd���͵������ַ���ת�� ר���ṩWebҳ��ǰN������ת�� ������2006-07-27����ת��Ϊ2006-07-26
     * 00:00:00
     */
    public static final Date getPreDate(String endDate, int day) {
        Locale locale = Locale.CHINESE;
        try {
            Date date = convertStringToDate(DATE_TIME_PATTERN, locale, null, endDate + " 00:00:00");
            return new Date(date.getTime() - day * 24 * 3600 * 1000);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * yyyy��mm��dd�� ����w
     */
    public static String getFullDateStr() {
        DateFormat format = DateFormat.getDateInstance(DateFormat.FULL, Locale.CHINESE);
        return format.format(new Date());
    }

    /**
     * ������������֮����������
     * 
     * @param date1
     * @param date2
     * @return
     */

    public static int diffdates(Date date1, Date date2) {
        int result = 0;
        if (date1 == null)
            return 0;
        if (date2 == null)
            return 0;

        GregorianCalendar gc1 = new GregorianCalendar();
        GregorianCalendar gc2 = new GregorianCalendar();

        gc1.setTime(date1);
        gc2.setTime(date2);
        result = getDays(gc1, gc2);

        return result;
    }

    public static int getDays(GregorianCalendar g1, GregorianCalendar g2) {
        int elapsed = 0;
        GregorianCalendar gc1, gc2;

        if (g2.after(g1)) {
            gc2 = (GregorianCalendar) g2.clone();
            gc1 = (GregorianCalendar) g1.clone();
        } else {
            gc2 = (GregorianCalendar) g1.clone();
            gc1 = (GregorianCalendar) g2.clone();
        }

        gc1.clear(Calendar.MILLISECOND);
        gc1.clear(Calendar.SECOND);
        gc1.clear(Calendar.MINUTE);
        gc1.clear(Calendar.HOUR_OF_DAY);

        gc2.clear(Calendar.MILLISECOND);
        gc2.clear(Calendar.SECOND);
        gc2.clear(Calendar.MINUTE);
        gc2.clear(Calendar.HOUR_OF_DAY);

        while (gc1.before(gc2)) {
            gc1.add(Calendar.DATE, 1);
            elapsed++;
        }
        return elapsed;
    }

    /**
     * ���ܣ�����ʾʱ����ַ����Ը�������ʽת��Ϊjava.util.Date����
     * �Ҷ��ڻ����ڸ�����ʱ�����Сʱ��formatStyle��formatStr��ʽ��ͬ��
     * 
     * @param:formatStyle Ҫ��ʽ������ʽ,��:yyyy-MM-dd
     *                        HH:mm:ss
     * @param:formatStr ��ת�����ַ���(��ʾ����ʱ��)
     * @param:hour ���ڻ����ڵ�Сʱ��(�����ɸ�)
     *                 ��λΪСʱ
     * @return java.util.Date
     */
    public static Date formartDate(String formatStyle, String formatStr, int hour) {
        SimpleDateFormat format = new SimpleDateFormat(formatStyle, Locale.CHINA);
        try {
            Date date = new Date();
            date.setTime(format.parse(formatStr).getTime() + hour * 60 * 60 * 1000);
            return date;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * ��ȡ����ʱ��
     */
    public static Date getNow() {
        return new Date(new Date().getTime());
    }

    /**
     * ��ȡǰһСʱ
     */
    public static Date getPreHour() {
        return new Date(new Date().getTime() - 3600 * 1000L);
    }

    /**
     * ��ȡ��һСʱ
     */
    public static Date getNextHour() {
        return new Date(new Date().getTime() + 3600 * 1000L);
    }

    /**
     * ��ȡ����
     */
    public static Date getYesterday() {
        return new Date(new Date().getTime() - 24 * 3600 * 1000L);
    }

    /**
     * ��ȡǰ��
     */
    public static Date getPreYesterday() {
        return new Date(new Date().getTime() - 48 * 3600 * 1000L);
    }

    /**
     * ��ȡ����
     */
    public static Date getYesterdayDate(Date day) {
        return new Date(day.getTime() - 24 * 3600 * 1000L);
    }

    /**
     * ��ȡ����
     */
    public static Date getTomorrowDate(Date day) {
        return new Date(day.getTime() + 24 * 3600 * 1000L);
    }

    /**
     * ��ȡ����
     */
    public static Date getLastWeek(Date day) {
        return new Date(day.getTime() - 7 * 24 * 3600 * 1000L);
    }

    /**
     * ��ȡ����
     */
    public static Date getNextWeek(Date day) {
        return new Date(day.getTime() + 7 * 24 * 3600 * 1000L);
    }

    /**
     * ��ȡ�ϸ���
     */
    public static Date getLastMonth() {
        return getLastMonth(new Date());
    }

    /**
     * ���ָ��ʱ���ĳ�µĵ�һ��
     * 
     * @param date
     * @return
     * 
     */
    public static Date getMonthFirstDayyyyyMMdd(Date date) {
        int[] dateArr = getDateArray(date);
        String year = String.valueOf(dateArr[0]);
        String month = String.valueOf(dateArr[1]);
        month = month.length() == 1 ? "0" + month : month;
        Date retDate = convertStringToDate(year + month + "01", DATE_PATTERN_YYYYMMDD);
        return retDate;
    }

    /**
     * ���ָ��ʱ���ĳ�µ����һ��
     * 
     * @param date
     * @return
     * 
     */
    public static Date getMonthLastDayyyyyMMdd(Date date) {
        int[] dateArr = getDateArray(date);
        int year = dateArr[0];
        int month = dateArr[1];
        int maxDayOfMonth = getMaxDayOfMonth(year, month);
        String monStr = String.valueOf(month);
        monStr = monStr.length() == 1 ? "0" + monStr : monStr;
        Date retDate = convertStringToDate(
                String.valueOf(year) + String.valueOf(monStr) + String.valueOf(maxDayOfMonth), DATE_PATTERN_YYYYMMDD);
        return retDate;
    }

    /**
     * ���ָ��ʱ���ĳ�µĵ�һ��
     * 
     * @param date
     * @return
     * 
     */
    public static String getMonthFirstDay() {
        return getMonthFirstDay(new Date());
    }

    /**
     * ���ָ��ʱ���ĳ�µ����һ��
     * 
     * @param date
     * @return
     * 
     */
    public static String getMonthLastDay() {
        return getMonthLastDay(new Date());
    }

    /**
     * ���ָ��ʱ���ĳ�µĵ�һ��
     * 
     * @param date
     * @return
     * 
     */
    public static String getMonthFirstDay(Date date) {
        int[] dateArr = getDateArray(date);
        String year = String.valueOf(dateArr[0]);
        String month = String.valueOf(dateArr[1]);
        month = month.length() == 1 ? "0" + month : month;
        return year + "-" + month + "-" + "01";
    }

    /**
     * ���ָ��ʱ���ĳ�µ����һ��
     * 
     * @param date
     * @return
     * 
     */
    public static String getMonthLastDay(Date date) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        String endDate = format.format(cal.getTime());
        return endDate;
    }

    /**
     * ��ȡ�ƶ�ʱ����ϸ���
     */
    public static Date getLastMonth(Date date) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.CHINA);
        cal.clear();
        cal.setTime(date);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
        cal.getTime();
        return cal.getTime();
    }

    /**
     * ��ȡ�ƶ�ʱ����¸���
     */
    public static Date getNextMonth(Date date) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.CHINA);
        cal.clear();
        cal.setTime(date);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
        cal.getTime();
        return cal.getTime();
    }

    /**
     * ��ȡָ��������и��µ��������
     * 
     * @param year
     *            ָ����
     * @param month
     *            ָ���� 1-12
     * @return �����������
     */
    public static int getMaxDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.CHINA);
        cal.clear();
        cal.set(year, month - 1, 1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * ����ָ������ݺ�ָ���ĵڶ�������ŵõ����ܵĵ�һ������һ������
     * 
     * @param year
     *            ָ�������,��2006
     * @param weekNo
     *            ָ������еĵڶ�����,��37
     * @return ���ܵ���ʼ���ں���ܵĽ�������<br>
     *         Date[0] ��ʼ����<br>
     *         Date[1] ��������
     */
    public static Date[] getGivenWeekDates(int year, int weekNo) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.CHINA);
        cal.clear();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
        Date begin = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, 6);
        Date end = cal.getTime();
        return new Date[] { begin, end };
    }

    /**
     * ����ָ�����ڻ�ȡ����һ���еĵڶ�����
     * 
     * @param date
     *            ָ��������,ΪnullĬ��Ϊ��ʱ����
     * @return ����ĵڶ��������,��37
     */
    public static int getWeekNo(Date date) {
        if (date == null)
            date = new Date();
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.CHINA);
        cal.clear();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * ��ȡ�ƶ�ʱ������
     * 
     * @param date
     *            �ƶ�ʱ��
     * @return ���
     */
    public static int getYear(Date date) {
        if (date == null)
            date = new Date();
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.CHINA);
        cal.clear();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * ��ȡ�ƶ�ʱ����·�
     * 
     * @param date
     *            �ƶ�ʱ��
     * @return ���
     */
    public static int getMonth(Date date) {
        if (date == null)
            date = new Date();
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.CHINA);
        cal.clear();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    /**
     * ��ȡ�ƶ�ʱ����·�
     * 
     * @param date
     *            �ƶ�ʱ��
     * @return ���
     */
    public static int getDay(Date date) {
        if (date == null)
            date = new Date();
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.CHINA);
        cal.clear();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * ��ʽ������
     * 
     * @param date
     *            ����ʽ��������
     * @param style
     *            ��ʾ����ʽ����yyyyMMdd
     */
    public static String fmtDate(Date date, String style) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(style);
        return dateFormat.format(date);
    }

    /**
     * �õ���ǰ����
     * 
     * @return int[] int[0] �� int[1] �� int[2] �� int[3] ʱ int[4] �� int[5] ��
     */
    public static int[] getCurrentDate() {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.CHINA);
        cal.setTime(new Date());
        int[] date = new int[6];
        date[0] = cal.get(Calendar.YEAR);
        date[1] = cal.get(Calendar.MONTH) + 1;
        date[2] = cal.get(Calendar.DATE);
        date[3] = cal.get(Calendar.HOUR_OF_DAY);
        date[4] = cal.get(Calendar.MINUTE);
        date[5] = cal.get(Calendar.SECOND);
        return date;
    }

    /**
     * �õ�ָ������
     * 
     * @return int[] int[0] �� int[1] �� int[2] �� int[3] ʱ int[4] �� int[5] ��
     * 
     */
    public static int[] getDateArray(Date date) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.CHINA);
        cal.setTime(date);
        int[] dateArr = new int[6];
        dateArr[0] = cal.get(Calendar.YEAR);
        dateArr[1] = cal.get(Calendar.MONTH) + 1;
        dateArr[2] = cal.get(Calendar.DATE);
        dateArr[3] = cal.get(Calendar.HOUR_OF_DAY);
        dateArr[4] = cal.get(Calendar.MINUTE);
        dateArr[5] = cal.get(Calendar.SECOND);
        return dateArr;
    }

    /**
     * �����ƶ�����ݺ��·ݣ��ٵõ������ڵ�ǰ�����»������µ�������ݺ��·�
     * 
     * @param year
     *            ָ������ݣ��� 2006
     * @param month
     *            �ƶ����·ݣ��� 6
     * @param monthSect
     *            �·ݵĲ�ֵ �磺����Ϊ2006��5�·ݣ�Ҫ�õ���4�£���monthSect = 4����ȷ���ڽ��Ϊ2006��9��
     *            �磺����Ϊ2006��5�·ݣ�Ҫ�õ�ǰ4�£���monthSect = -4����ȷ���ڽ��Ϊ2006��1��
     *            �磺monthSect = 0�����ʾΪyear��month��
     * @return int[] int[0] ��� int[1] �·�
     */
    public static int[] getLimitMonthDate(int year, int month, int monthSect) {
        year = year < 1 ? 1 : year;
        month = month > 12 ? 12 : month;
        month = month < 1 ? 1 : month;
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), new Locale("zh", "CN"));
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.add(Calendar.MONTH, monthSect);
        int[] yAndM = new int[2];
        yAndM[0] = cal.get(Calendar.YEAR);
        yAndM[1] = cal.get(Calendar.MONTH);
        if (yAndM[1] == 0) {
            yAndM[0] = yAndM[0] - 1;
            yAndM[1] = 12;
        }
        return yAndM;
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     * 
     * @return
     * @throws ParseException
     */
    public static String getDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_PATTERN);
        String fd = sdf.format(date);
        return fd;
    }

    /**
     * HH:mm:ss
     * 
     * @return
     * @throws ParseException
     */
    public static String getDateHHmmss() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_PATTERN);
        String fd = sdf.format(date);
        return fd;
    }

    /**
     * ʱ���
     * 
     * @return
     */
    public static String getTime() {
        Date date = new Date();
        return date.getTime() + "";
    }

    /**
     * yyyyMMddhhmmss
     * 
     * @return
     * @throws ParseException
     */
    public static String getDateyyyyMMddhhmmss() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fd = sdf.format(date);
        return fd;
    }

    /**
     * yyyyMMdd
     * 
     * @return
     * @throws ParseException
     */
    public static String getDateyyyyMMddNone() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN_YYYYMMDD);
        String fd = sdf.format(date);
        return fd;
    }

    /**
     * ���µ�һ��
     * 
     * @return
     */
    public static String getDayofMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN_YYYYMMDD);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, 1);
        String bdate = sdf.format(c.getTime());
        return bdate;
    }

    /**
     * yyyyMMddhhmmss
     * 
     * @return
     * @throws ParseException
     */
    public static String getDateHHmmssNone() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String fd = sdf.format(date);
        return fd;
    }

    /**
     * yyyy-MM-dd
     * 
     * @return
     * @throws ParseException
     */
    public static String getDateyyyyMMdd() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        String fd = sdf.format(date);
        return fd;
    }

    public static Date getDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_PATTERN);
        try {
            return format.parse(format.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getSimpleDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        try {
            return format.parse(format.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * �õ����µĵ�һ������һ����ַ���������
     * 
     * @param month
     *            ��ʽ 'yyyyMM'
     * @return
     * 
     */
    public static String[] getMonFirstLastDays(String month) {
        Date thisDate = convertStringToDate(month, "yyyyMM");
        Date firstDay = getMonthFirstDayyyyyMMdd(thisDate);
        Date lastDay = getMonthLastDayyyyyMMdd(thisDate);
        return new String[] { convertDateToString(DATE_PATTERN_YYYYMMDD, firstDay),
                convertDateToString(DATE_PATTERN_YYYYMMDD, lastDay) };
    }

    /**
     * ��ȡ����ʱ��ĵ��µ����� yyyymmdd author:Liu Liming
     * 
     * @param yyyymmdd
     * @return
     */
    public static String getFirstDate(String yyyymmdd) {
        try {
            Date date = DatetimeUtil.convertStringToDate(DATE_PATTERN_YYYYMMDD, null, null, yyyymmdd);
            Calendar curCal = Calendar.getInstance();
            curCal.setTime(date);
            curCal.set(Calendar.DATE, 1);
            return DatetimeUtil.convertDateToString(DATE_PATTERN_YYYYMMDD, curCal.getTime());
        } catch (ParseException e) {
            return "";
        }
    }

    /**
     * ��ȡ����ʱ��ĵ��µ����� yyyymmdd author:Liu Liming
     * 
     * @param yyyymmdd
     * @return yyyymmdd
     */
    public static String getLastDate(String yyyymmdd) {
        try {
            String sDate = getFirstDate(yyyymmdd);
            Date date = DatetimeUtil.convertStringToDate(DATE_PATTERN_YYYYMMDD, null, null, sDate);
            Calendar retVal = Calendar.getInstance();
            retVal.setTime(date);
            retVal.add(Calendar.MONTH, 1);
            retVal.add(Calendar.DATE, -1);
            return DatetimeUtil.convertDateToString(DATE_PATTERN_YYYYMMDD, retVal.getTime());

        } catch (ParseException e) {
            return "";
        }
    }

    /**
     * ��ȡNext��
     */
    public static String getNextDay(Object date, int amount) {
        SimpleDateFormat frm = new SimpleDateFormat(DATE_TIME_PATTERN);
        Calendar calendar = Calendar.getInstance();
        try {
            if (date instanceof String) {
                calendar.setTime(frm.parse(date.toString()));
            } else if (date instanceof Date) {
                calendar.setTime((Date) date);
            }
            calendar.add(Calendar.DATE, amount);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return frm.format(calendar.getTime());
    }

    /**
     * ��ȡNext��
     */
    public static String getNextDay(Object date, int amount, String pattern) {
        SimpleDateFormat frm = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        try {
            if (date instanceof String) {
                calendar.setTime(frm.parse(date.toString()));
            } else if (date instanceof Date) {
                calendar.setTime((Date) date);
            }
            calendar.add(Calendar.DATE, amount);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return frm.format(calendar.getTime());
    }

    /**
     * ��ȡNext MINUTE
     */
    public static String getNextMinute(Object date, int amount) {
        SimpleDateFormat frm = new SimpleDateFormat(DATE_TIME_PATTERN);
        Calendar calendar = Calendar.getInstance();
        try {
            if (date instanceof String) {
                calendar.setTime(frm.parse(date.toString()));
            } else if (date instanceof Date) {
                calendar.setTime((Date) date);
            }
            calendar.add(Calendar.MINUTE, amount);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return frm.format(calendar.getTime());
    }

    /**
     * ���ַ���20080808 ת���� 2008-08-08
     */
    public static String getDateForm(String date) {
        if (StringUtils.isBlank(date) || date.length() != 8) {
            return "0000-00-00";
        }
        return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
    }

    /**
     * @Description:�������date�ڸ���day���ֵ
     * @param date
     *            �Ϸ���java���ڸ�ʽ
     * @param day
     *            ���������
     * @param format
     *            ���ڸ�ʽ������ ��date��ʽһ��
     * @return
     */
    public static String calculateDate(String date, int day, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        try {
            Date d = df.parse(date);
            Calendar c = Calendar.getInstance();

            c.setTime(d);
            c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + day);

            return df.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * �ж��Ƿ�Ϊͬһ��
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDay(Date date1, Date date2) {
        boolean result = false;
        try {
            String date1Str = DatetimeUtil.convertDateToString(DATE_PATTERN_YYYYMMDD, date1);
            String date2Str = DatetimeUtil.convertDateToString(DATE_PATTERN_YYYYMMDD, date2);
            if (date1Str.equals(date2Str)) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // ���ӻ��������
    public static Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }

    /**
     * ����ת����string����ѧ
     * 
     * @param date
     * @param type
     * @return
     */
    public static String dateToString(Date date, String type) {
        if (date == null)
            return "";
        String str = null;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (type.equals("SHORT")) {
            // 07-1-18
            format = DateFormat.getDateInstance(DateFormat.SHORT);
            str = format.format(date);
        } else if (type.equals("MEDIUM")) {
            // 2007-1-18
            format = DateFormat.getDateInstance(DateFormat.MEDIUM);
            str = format.format(date);
        } else if (type.equals("FULL")) {
            // 2007��1��18�� ������
            format = DateFormat.getDateInstance(DateFormat.FULL);
            str = format.format(date);
        }
        return str;
    }

    /**
     * string to date
     * 
     * @param str
     * @return
     */
    public static Date stringToDate(String str) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            // Fri Feb 24 00:00:00 CST 2012
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 2012-02-24
        date = java.sql.Date.valueOf(str);

        return date;
    }

    /**
     * ����ʱ��תunixʱ���
     * 
     * @param where
     *            ����ǲ�ѯ���յ���Ϣ��¼��where �Ǵ��賿������23����
     * @param newDate
     *            1 ��ʾ�ǲ�ѯ���գ�������ǲ�ѯ��������
     * @return
     * @throws Exception
     */
    public static String getUnixTime(String where, String newDate) throws Exception {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(date);
        // �������1��ʾ�ǲ�ѯ���ռ�¼
        if (newDate.equals("1")) {
            newDate = today + " " + where;
        } else {// ����Ͳ�ѯҪ��ѯ���ڵĿͷ���¼
            newDate = newDate + " " + where;
        }
        long n = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(newDate).getTime();
        return String.valueOf(n).substring(0, 10);
    }

    /**
     * ����ʱ��תunixʱ���
     * 
     * @param where
     *            ����ǲ�ѯ���յ���Ϣ��¼��where �Ǵ��賿������23����
     * @param newDate
     *            1 ��ʾ�ǲ�ѯ���գ�������ǲ�ѯ��������
     * @return
     * @throws Exception
     */
    public static String getUnixTime2(String where, String date) throws Exception {
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = date + " " + where;
        long n = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(today).getTime();
        return String.valueOf(n).substring(0, 10);
    }

    /**
     * ��unixʱ���ת������ͨʱ��
     * 
     * @param timestampString
     * @return
     */
    public static String TimeStamp2Date(String timestampString) {
        // String times = Long.toString(timestampString);
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(timestamp));
        return date;
    }

    /**
     * ���ص�ǰʱ�� ��ʽ��yyyy-MM-dd hh:mm:ss
     * 
     * @return String
     */
    public static String fromDateH() {
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format1.format(new Date());
    }

    /**
     * ����ʱ���ʽת�����ַ���yyyy-MM-dd HH:mm:ss
     * 
     * @param dateDate
     * @return
     */
    public static String dateToStrLong(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * ���ص�ǰʱ�� ��ʽ��yyyy-MM-dd
     * 
     * @return String
     */
    public static String fromDateY() {
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        return format1.format(new Date());
    }

    /**
     * ��������������
     * 
     * @param birth
     *            yyyy-MM-dd
     * @return
     */
    public static Integer getAge(String birth) {
        Integer age = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String nowdate = sdf.format(new Date());
        String[] ageb = birth.split("-");// ��������
        String[] agen = nowdate.split("-");// ��ʱ
        Integer year = Integer.valueOf(agen[0]) - Integer.valueOf(ageb[0]);
        Integer mouth = Integer.valueOf(agen[1]) - Integer.valueOf(ageb[1]);
        Integer day = Integer.valueOf(agen[2]) - Integer.valueOf(ageb[2]);
        if (mouth < 0) {
            year--;
        } else if (mouth == 0 && day < 0) {
            year--;
        } else {
        }
        age = year;
        return age;
    }

    public static int getSysYear() {
        Calendar today = Calendar.getInstance();
        return today.get(Calendar.YEAR);
    }

    public static int getSysMonth() {
        Calendar today = Calendar.getInstance();
        return today.get(Calendar.MONTH) + 1;
    }

    public static int getSysDay() {
        Calendar today = Calendar.getInstance();
        return today.get(Calendar.DAY_OF_MONTH);
    }

    public static int getSysHour() {
        Calendar today = Calendar.getInstance();
        return today.get(Calendar.HOUR_OF_DAY);
    }

    public static int getSysMinute() {
        Calendar today = Calendar.getInstance();
        return today.get(Calendar.MINUTE);
    }

    public static int getSysSecond() {
        Calendar today = Calendar.getInstance();
        return today.get(Calendar.SECOND);
    }

    public static int getSysMillisecond() {
        Calendar today = Calendar.getInstance();
        return today.get(Calendar.MILLISECOND);
    }

    public static String getSysDate() {
        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH) + 1;
        int day = today.get(Calendar.DAY_OF_MONTH);
        StringBuilder rtnStr = new StringBuilder("");
        rtnStr.append(String.valueOf(year));
        rtnStr.append(DATE_SPLIT);
        rtnStr.append(StringUtil.leftPad(String.valueOf(month), 2, "0"));
        rtnStr.append(DATE_SPLIT);
        rtnStr.append(StringUtil.leftPad(String.valueOf(day), 2, "0"));
        return rtnStr.toString();
    }

    public static String getSysTime() {
        Calendar today = Calendar.getInstance();
        int hour = today.get(Calendar.HOUR_OF_DAY);
        int minute = today.get(Calendar.MINUTE);
        int second = today.get(Calendar.SECOND);
        StringBuilder rtnStr = new StringBuilder("");
        rtnStr.append(StringUtil.leftPad(String.valueOf(hour), 2, "0"));
        rtnStr.append(":");
        rtnStr.append(StringUtil.leftPad(String.valueOf(minute), 2, "0"));
        rtnStr.append(":");
        rtnStr.append(StringUtil.leftPad(String.valueOf(second), 2, "0"));
        return rtnStr.toString();
    }

    public static String getSysTimestamp() {
        return getSysDate() + " " + getSysTime() + "." + getSysMillisecond();
    }

    public static synchronized String getSysTimeStr() {
        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH) + 1;
        int day = today.get(Calendar.DAY_OF_MONTH);
        int hour = today.get(Calendar.HOUR_OF_DAY);
        int minute = today.get(Calendar.MINUTE);
        int second = today.get(Calendar.SECOND);
        int millisecond = today.get(Calendar.MILLISECOND);
        StringBuilder rtnStr = new StringBuilder("");
        rtnStr.append(String.valueOf(year));
        rtnStr.append(StringUtil.leftPad(String.valueOf(month), 2, "0"));
        rtnStr.append(StringUtil.leftPad(String.valueOf(day), 2, "0"));
        rtnStr.append("_");
        rtnStr.append(StringUtil.leftPad(String.valueOf(hour), 2, "0"));
        rtnStr.append(StringUtil.leftPad(String.valueOf(minute), 2, "0"));
        rtnStr.append(StringUtil.leftPad(String.valueOf(second), 2, "0"));
        rtnStr.append("_");
        rtnStr.append(String.valueOf(millisecond));
        return rtnStr.toString();
    }

    /**
     * �r�g�Υǩ`���Υե��`�ޥåȤΗʖ�
     * 
     * @param dateStr
     * @return ture:�ե��`�ޥåȤ��������Ǥ�,false:�ե��`�ޥåȤ��`��
     */
    public static boolean isDateStringValid(String dateStr) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        try {
            if (sdf.format(sdf.parse(dateStr)).equalsIgnoreCase(dateStr))
                return true;
            else
                return false;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * �ո�����Ηʖ�
     * 
     * @param startStr
     *            ,endStr
     * @return ture:�ե��`�ޥåȤ��������Ǥ�,false:�ե��`�ޥåȤ��`��
     */
    public static boolean CompareDate(String startStr, String endStr) {

        String startDate = startStr.replace("/", "");
        String endDate = endStr.replace("/", "");
        if (startDate.compareTo(endDate) > 0) {
            return false;
        }
        return true;
    }

    /**
     * �����ͥǩ`����ܞ�Q
     * 
     * @param argDate
     * @throws SQLException
     * @return ����(Date)
     * @throws java.text.ParseException
     */
    public static Date ConvertDate(String argDate) {
        Date date = null;
        try {
            if (argDate != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                date = sdf.parse(argDate);
            }
            return date;
        } catch (ParseException ex) {
            return null;
        }
    }

    /**
     * �����ѥ��`����ָ�����줿�ե��`�ޥå���ʽ�ǬF�ڤΥ����ƥ��ո���ȡ�ä��롣��<br>
     * 
     * @param pattern
     *            yyyy/MM/dd HH:mm:ss
     * 
     * @return yyyy/MM/dd HH:mm:ss
     */
    public static String getSystemTime(String pattern) {

        // �����ո�����ʽ�γ���
        SimpleDateFormat fl = new SimpleDateFormat(pattern);

        // �F���ո�
        Date date = new Date();

        // �����ƥ��ո����B�äƥե��ޥåȤ��Ƒ���
        return fl.format(date);
    }

    /**
     * �����ѥ��`����ָ�����줿�ե��`�ޥå���ʽ��ָ�����줿�ո����Q���롣<br>
     * 
     * @param date
     *            �ո�
     * 
     * @param format
     *            �����У�yyyyMMdd
     * 
     * @return yyyyMMdd
     */
    public static String getFormatDate(Date date, String format) {

        if (date == null) {
            return "";
        }
        // �����ո�����ʽ�γ���
        SimpleDateFormat fl = new SimpleDateFormat(format);

        // �����ƥ��ո����B�äƥե��ޥåȤ��Ƒ���
        return fl.format(date);
    }

    /**
     * ��������ʱ����������
     */
    // ���º�δ��һ���µ��·ݲ�
    public static int monthsBetween(GregorianCalendar pFurtherMonth) {
        GregorianCalendar vToday = new GregorianCalendar();
        GregorianCalendar vFurtherMonth = (GregorianCalendar) pFurtherMonth.clone();
        return monthsBetween(vToday, vFurtherMonth);
    }

    /** �����·ֺͱ��µ��·ݲ� **/
    public static int monthsBetween(String pFurtherMonth) {
        GregorianCalendar vToday = new GregorianCalendar();
        GregorianCalendar vFurtherMonth = DatetimeUtil.parse2Cal(pFurtherMonth);
        return monthsBetween(vToday, vFurtherMonth);
    }

    /** ��������ʱ����������,String�� **/
    public static int monthsBetween(String pFormerStr, String pLatterStr) {
        GregorianCalendar vFormer = DatetimeUtil.parse2Cal(pFormerStr);
        GregorianCalendar vLatter = DatetimeUtil.parse2Cal(pLatterStr);
        return monthsBetween(vFormer, vLatter);
    }

    /** ��������ʱ����������,String�� **/
    public static int monthsBetween(Date pFormerDate, Date pLatterDate) {
        String vForMerDate = DatetimeUtil.getFormatDate(pFormerDate, "yyyy/MM/dd");
        String vLatterDate = DatetimeUtil.getFormatDate(pLatterDate, "yyyy/MM/dd");
        return monthsBetween(vForMerDate, vLatterDate);
    }

    public static int monthsBetween(GregorianCalendar pFormer, GregorianCalendar pLatter) {
        GregorianCalendar vFormer = pFormer, vLatter = pLatter;
        boolean vPositive = true;
        if (pFormer.before(pLatter)) {
            vFormer = pFormer;
            vLatter = pLatter;
        } else {
            vFormer = pLatter;
            vLatter = pFormer;
            vPositive = false;
        }

        int vCounter = 0;
        while (vFormer.get(Calendar.YEAR) != vLatter.get(Calendar.YEAR)
                || vFormer.get(Calendar.MONTH) != vLatter.get(Calendar.MONTH)) {
            vFormer.add(Calendar.MONTH, 1);
            vCounter++;
        }
        if (vPositive)
            return vCounter;
        else
            return -vCounter;
    }

    public static GregorianCalendar parse2Cal(String pFormerStr) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(ConvertDate(pFormerStr));
        return calendar;
    }

    /** */
    /**
     * ��������ʱ��֮���������
     * 
     * @param startday
     *            ��ʼʱ��
     * @param endday
     *            ����ʱ��
     * @return
     */
    public int getDaysBetween(Calendar d1, Calendar d2) {
        if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
            java.util.Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }
        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            d1 = (Calendar) d1.clone();
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);// �õ������ʵ������
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        return days;
    }

    /** */
    /**
     * ��������ʱ��֮���������
     * 
     * @param startday
     *            ��ʼʱ��
     * @param endday
     *            ����ʱ��
     * @return
     */
    public int getIntervalDays(Date startday, Date endday) {
        // ȷ��startday��endday֮ǰ
        if (startday.after(endday)) {
            Date cal = startday;
            startday = endday;
            endday = cal;
        }
        // �ֱ�õ�����ʱ��ĺ�����
        long sl = startday.getTime();
        long el = endday.getTime();

        long ei = el - sl;
        // ���ݺ���������������
        return (int) (ei / (1000 * 60 * 60 * 24));
    }

    public static boolean isdate(String s) {
        String a[] = s.split("/");
        boolean flg = true;
        if (!(Integer.parseInt(a[0]) >= 1950 && Integer.parseInt(a[0]) <= 2050)) {
            flg = false;
        }
        return flg;
    }

    public static boolean checkDate(String s) {
        boolean ret = true;
        try {
            DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            ret = df.format(df.parse(s)).equals(s);
        } catch (ParseException e) {
            ret = false;
        }
        return ret;
    }

    public static Date parseDate(String s) {
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date date3 = bartDateFormat.parse(s);
            date3 = bartDateFormat.parse(s);
            return date3;
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getNextMonth(String vFromMonth) {
        Date date = getNextMonth(parseDate(vFromMonth));
        // ��ʽ��ʱ��
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
        return sdf.format(date);
    }

    /**
     * inputCalendar������������/�p���Calendar����롣<br>
     * 
     * @param inputCalendar
     *            �ո�
     * 
     * @param month
     *            +/- ����
     * 
     * @return inputCalendar������������/�p���Calendar <br>
     *         �����С�(2009/6/2,1)��2009/7/2��ȡ�ä��롣<br>
     *         (2009/6/2, -1)��2009/5/2��ȡ�ä��롣
     */
    public static Calendar getMonthAddedCalendar(Calendar inputCalendar, int month) {

        // �ո���ȡ�ä���
        Calendar outputCalendar = Calendar.getInstance();
        outputCalendar = inputCalendar;

        // inputCalendar������������/�p����
        outputCalendar.add(Calendar.MONTH, month);

        return outputCalendar;
    }

    /**
     * �F�ڤΥ����ƥ��ո���Calendar��ȡ�ä��롣<br>
     * 
     * @return �����ƥ��ո�
     */
    public static Calendar getSystemCalendar() {

        return Calendar.getInstance();
    }

    /**
     * inputCalendar������������/�p���Calendar����롣<br>
     * 
     * @param inputCalendar
     *            �ո�
     * 
     * @param date
     *            +/- ����
     * 
     * @return inputCalendar������������/�p���Calendar����롣<br>
     *         �����С�(2009/6/2,1)��2009/6/3��ȡ�ä��롣<br>
     *         (2009/6/2, -1)��2009/6/1��ȡ�ä��롣
     */
    public static Calendar getDateAddedCalendar(Calendar inputCalendar, int date) {

        // �ո���ȡ�ä���
        Calendar outputCalendar = Calendar.getInstance();
        outputCalendar = inputCalendar;

        // inputCalendar������������/�p����
        outputCalendar.add(Calendar.DATE, date);

        return outputCalendar;

    }

    /**
     * calendar����ˡ�"yyyy/MM/dd"��ʽ�������Ф�ȡ�ä��롣<br>
     * 
     * @param calendar
     *            �ո�
     * 
     * @return "yyyy/MM/dd"��ʽ��������
     */
    public static String getStringByCalendar(Calendar calendar) {

        // ��
        int year = calendar.get(Calendar.YEAR);
        // �� Calendar.MONTH �� 0 ����Τǡ�
        int month = calendar.get(Calendar.MONTH) + 1;
        // ��
        int date = calendar.get(Calendar.DATE);

        // "yyyy/MM/dd"��ʽ�ǽM�ߺϤ碌
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(year);
        stringBuilder.append("/");
        stringBuilder.append(NumericUtil.fillZeroToLen(month, 2));
        stringBuilder.append("/");
        stringBuilder.append(NumericUtil.fillZeroToLen(date, 2));

        return stringBuilder.toString();
    }

    /**
     * date����ˡ�"yyyy/MM/dd"��ʽ�������Ф�ȡ�ä��롣<br>
     * 
     * @param date
     *            �ո�(yyyyMMdd)
     * 
     * @return �ե��`�ޥåȤ����ո�(yyyy/MM/dd)
     */
    public static String formatDateForDisplay(String date) {

        String strRet = "";

        // ��ѥ��`�����ж�
        if (StringUtil.isEmpty(date)) {
            return "";
        }

        // ��ǿա�����!=���Έ���
        if (date.length() != 8) {
            return date;
        }

        // ��ǿա������������Έ���
        strRet = StringUtil.concat(date.substring(0, 4), "/", date.substring(4, 6), "/", date.substring(6));

        return strRet;
    }

    /**
     * date����ˡ�"yyyyMMdd"��ʽ�������Ф�ȡ�ä��롣<br>
     * 
     * @param date
     *            �ո�(yyyy/MM/dd)
     * 
     * @return �ե��`�ޥåȤ����ո�(yyyyMMdd)
     */
    public static String formatDateForDB(String date) {

        // ��ѥ��`�����ж�
        if (StringUtil.isEmpty(date)) {
            return "";
        }

        // ��ǿա������������Έ���
        if (date.length() < 10) {
            return date;
        }

        // ��ǿա��������������Έ���
        return date.substring(0, 10).replaceAll("/", "");
    }

    /**
     * �����ƥ��ո�����ˡ�ָ�����줿months��Ӝp����pattern����ʽ�Ǒ��롣<br>
     * 
     * @param pattern
     *            YY/MM
     * 
     * @param months
     *            ����
     * 
     * @return �٩`���ո����Ϥ��¤μӜp��Y����<br>
     *         �����С��F�ڤΥ����ƥ��ո���2009/6/2�Έ��ϡ� addMonth("yy/MM", 1)�� "09/07"��ȡ�ä��롣
     */
    public static String addMonth(String pattern, int months) {

        // �������`�����γ���
        Calendar cal = Calendar.getInstance();

        Date date = new Date();

        // �ո��Υ��å�
        cal.setTime(date);

        // �ո���Ӌ��
        cal.add(Calendar.MONTH, months);

        // �����ո�����ʽ�γ���
        SimpleDateFormat fl = new SimpleDateFormat(pattern);

        // �ո���ե��ޥåȤ��Ƒ���
        return fl.format(cal.getTime());
    }
}
