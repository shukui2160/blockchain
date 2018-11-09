package com.tlabs.blockchain.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by admin on 2018/7/25.
 */
public class DateUtil {
    public static final long MILLONS = 1000l;
    public static final long SECOND = MILLONS;
    public static final long MINITE = SECOND * 60;
    public static final long HOUR = MINITE * 60;
    public static final long DAY = HOUR * 24;
    static Calendar calendar = null;

    static {
        calendar = Calendar.getInstance();
        calendar.set(1970, 0, 1, 0, 0, 0);
    }

    /**
     * ������Date��תΪָ����ʽ���ַ���
     * @param date
     * @param format
     * @return
     * 2014��11��10��
     */
    public static String getNowDateString(Date date, String format) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            String sNowTime = sdf.format(date);
            return sNowTime;
        } else {
            return null;
        }

    }


    /**
     * �����ڶ����ʽ���ַ�������ʽ��������ֻ������������ʽ���ַ���
     *
     * @param date
     * @return
     */
    public static String shortDateFormat(Date date) {
        return dateFormat(date, "yyyy-MM-dd");
    }

    /**
     * ���ص������ڵĸ�ʽ���ַ���
     *
     * @return
     */
    public static String shortDateFormatCurrentDate() {
        return dateFormat(new Date(), "yyyy-MM-dd");
    }

    /**
     * �����ڸ�ʽ�����Գ���ʽ����ʽ���أ�����ʽ����ʱ����
     *
     * @param date
     *            ���ڶ���
     * @return
     */
    public static String longDateFormat(Date date) {
        return dateFormat(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * ����ǰ���ڸ�ʽ�����Գ���ʽ����ʽ���з���
     *
     * @return
     */
    public static String longDateFormatCurrentDate() {
        return dateFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * @Title: currentDate
     * @Description: ��ȡ��ǰʱ����ַ���
     * @param @return
     * @return String ��������
     * @throws
     */
    public static String currentDate() {
        return dateFormat(new Date(), "yyyyMMddHHmmss");
    }

    /**
     * @Title: longCurrentDate
     * @Description:
     * @param @return
     * @return String ��������
     * @throws
     */
    public static String longCurrentDate() {
        return dateFormat(new Date(), "yyyyMMddHHmmssSSSS");
    }

    /***
     * ��Stringת��ΪDate
     */
    public static Date shortDateFormat(String date) {
        return dateFormat(date, "yyyy-MM-dd");
    }

    public static Date longDateFormat(String date) {
        return dateFormat(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * ��ȡ��1970-01-01��ʼ��ʱ������ʾ
     *
     * @return
     */
    public static long getTimesFromStard() {
        return calendar.getTimeInMillis();
    }

    public static String formatStandartSecond(Integer seconds) {
        Date date = new Date(calendar.getTimeInMillis() + seconds * MILLONS);
        return longDateFormat(date);
    }

    /**
     * ����ָ���ĸ�ʽ��ʽ�����ڶ���
     *
     * @param date
     *            ���ڶ���
     * @param foramt
     *            Ҫ��ʽ�ĸ�ʽ
     * @return
     */
    public static String dateFormat(Date date, String foramt) {
        String result = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(foramt);
            result = df.format(date);
        }
        return result;
    }

    public static Date dateFormat(String date, String format) {
        Date result = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat(format);
            result = df.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException("���ڸ�ʽ���ִ���!", e);
        }
        return result;
    }

    /**
     * ������ʼ���ںͽ�������֮�������֮��
     *
     * @param startDate
     *            ��ʼʱ��
     * @param endDate
     *            ����ʱ��
     * @return ��ʼʱ�������ʱ��֮�������
     */
    public static int calculateDays(Date startDate, Date endDate) {
        int result = 0;
        if (startDate != null && endDate != null) {
            result = (int) ((endDate.getTime() - startDate.getTime()) / DAY);
        }
        return result;
    }

    /**
     * ������ʼ���ںͽ�������֮��ķ�����
     *
     * @param startDate
     *            ��ʼʱ��
     * @param endDate
     *            ����ʱ��
     * @return ��ʼʱ�������ʱ��֮�������
     */
    public static int calculateMinutes(Date startDate, Date endDate) {
        int result = 0;
        if (startDate != null && endDate != null) {
            result = Math
                    .abs((int) ((endDate.getTime() - startDate.getTime()) / MINITE));
        }
        return result;
    }

    public static int calculateMinutes(Date startDate, String orderCreateDate) {
        Date endDate = longDateFormat(orderCreateDate);
        return calculateDays(startDate, endDate);
    }

    /**
     * ��ǰʱ���
     *
     * @return
     */
    public static long currentTime() {
        return new Date().getTime();
    }

    /**
     * ��ǰʱ���תΪ����
     *
     * @param time
     * @return 2012-11-11 ��ʽ
     */
    public static String currentTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(time));
    }

    /**
     *
     * @param time
     * @param patten
     * @return
     */
    public static String currentTime(long time, String patten) {
        SimpleDateFormat sdf = new SimpleDateFormat(patten);
        return sdf.format(new Date(time));
    }

    /**
     * <pre>
     * ��ǰʱ��תΪʱ���
     * ת��ʧ�ܷ���-1
     * @param time ��׼�����ڸ�ʽ
     * @return long
     * </pre>
     */
    public static long currentTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long date = -1;
        try {
            date = sdf.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static long currentTime(String time, String paten) {
        SimpleDateFormat sdf = new SimpleDateFormat(paten);
        long date = -1;
        try {
            date = sdf.parse(time).getTime();
        } catch (ParseException e) {
            throw new RuntimeException("���ڸ�ʽ���ִ���!", e);
        }
        return date;
    }

    /**
     *
     * @Title: getCurrentHour
     * @Description: ��ȡ����ǰ��Сʱ
     * @param @return
     * @return String ��������
     * @throws
     */
    @SuppressWarnings("deprecation")
    public static int getCurrentHour() {
        Date date = new Date();
        int hours = date.getHours();
        return hours;
    }

    /**
     * @Title: minuteNum
     * @Description: ��ȡ����ʱ������ķ�����
     * @param @param expir
     * @param @return
     * @return int �������� �����ӡ�
     * @throws
     */
    public static int subMinute(long expir) {
        long time = new Date().getTime();
        long m = time - expir;
        return (int) m / 1000 / 60;
    }

    public static Date nextDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    public static void main(String[] args) {
        // System.out.println(getCurrentHour());
        // long expir = new Date().getTime();
        // try {
        // Thread.sleep(1000);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // System.out.println(subMinute(expir));

        System.out.println(nextDate());
        System.out.println(longCurrentDate());
    }
}
