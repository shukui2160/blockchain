package com.tlabs.blockchain.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 2018/7/25.
 */
public class StringUtil {

    /** ���ɶ������ */
    public static String generateOrdrNo() {
		/*String cc = (int) (Math.random() * 1000) + DateUtil.dateFormat(new Date(), "yyyyMMddHHmmssss");
		BigInteger bi = new BigInteger(cc);*/
        String cc = DateUtil.dateFormat(new Date(), "yyMMddHHmmssSSS")+(int) (Math.random() * 900);
        //return baseString(bi, 62);
        return cc;
    }

    // ��һ���ַ������շָ���
    public static String[] StringToArray(String str, String seperator) {
        return str.split(seperator);
    }

    public static String encodeUtf8(String code) {
        try {
            return new String(code.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    // �ļ����������ļ����ƻ�ȡ��ʽ
    public static String getFileName() {
        String cc = (int) (Math.random() * 1000)
                + DateUtil.dateFormat(new Date(), "yyyyMMddHHmmssss");
        BigInteger bi = new BigInteger(cc);
        return baseString(bi, 62);
    }

    // ת��
    public static String baseString(BigInteger num, int base) {
        String str = "", digit = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        if (num.shortValue() == 0) {
            return "";
        } else {
            BigInteger valueOf = BigInteger.valueOf(base);
            str = baseString(num.divide(valueOf), base);
            return str + digit.charAt(num.mod(valueOf).shortValue());
        }
    }

    public static String parseDate(Date date) {
        StringBuffer buffer = new StringBuffer();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int YEAR = cal.get(Calendar.YEAR);
        int MONTH = cal.get(Calendar.MONTH) + 1;
        int DATE = cal.get(Calendar.DATE);
        int AM_PM = cal.get(Calendar.AM_PM);
        int HOUR_OF_DAY = cal.get(Calendar.HOUR_OF_DAY);
        int MINUTE = cal.get(Calendar.MINUTE);
        buffer.append(YEAR + "��" + MONTH + "��" + DATE + "��");
        if (AM_PM == 0) {
            buffer.append("����");
        } else {
            buffer.append("����");
        }
        if (HOUR_OF_DAY != 0 && MINUTE != 0) {
            buffer.append(HOUR_OF_DAY + ":" + MINUTE + "��");
        }
        return buffer.toString();
    }

    public static String parseDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuffer buffer = new StringBuffer();
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(date));
        } catch (ParseException e) {
            throw new RuntimeException("", e);
        }
        int YEAR = cal.get(Calendar.YEAR);
        int MONTH = cal.get(Calendar.MONTH) + 1;
        int DATE = cal.get(Calendar.DATE);
        int AM_PM = cal.get(Calendar.AM_PM);
        int HOUR_OF_DAY = cal.get(Calendar.HOUR_OF_DAY);
        int MINUTE = cal.get(Calendar.MINUTE);
        buffer.append(YEAR + "��" + MONTH + "��" + DATE + "��");
        if (AM_PM == 0) {
            buffer.append("����");
        } else {
            buffer.append("����");
        }
        if (HOUR_OF_DAY != 0 && MINUTE != 0) {
            buffer.append(HOUR_OF_DAY + ":" + MINUTE + "��");
        }
        return buffer.toString();
    }

    /**
     * ��ȡ�̶����ȵ��������
     *
     * @param n
     * @return
     */
    public static String getRandom(int n) {
        if (n == 0) {
            return null;
        }
        Random random = new Random();
        StringBuffer buffer = new StringBuffer(n);
        for (int i = 0; i < n; i++) {
            buffer.append(String.valueOf(random.nextInt(10)));
        }
        return buffer.toString();
    }

    /**
     * �ж��ַ����Ƿ�Ϊ��
     *
     * @param obj
     * @return
     */
    public static boolean isNotEmpty(String obj) {
        if (obj == null) {
            return false;
        }
        if (obj.trim().length() == 0) {
            return false;
        }
        if (String.valueOf(obj).equals("null")
                || String.valueOf(obj).equals("NULL")) {
            return false;
        }
        return true;
    }

    /**
     * �ж϶����Ƿ�Ϊ�ջ���int Ϊ0 doubleΪ0.0
     *
     * @param obj
     * @return
     */
    public static boolean isNotEmpty(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Integer) {
            int parseInt = (int) obj;
            if (parseInt == 0) {
                return false;
            }
        }
        if (obj instanceof Double) {
            double parseDouble = (double) obj;
            if (parseDouble == 0.0) {
                return false;
            }
        }
        if (obj instanceof Float) {
            float parseDouble = (float) obj;
            if (parseDouble == 0.0) {
                return false;
            }
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if ("".equals(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * ����ֻ������Ƿ�淶
     *
     * @param mobile
     * @return
     */
    public static boolean validatePhone(String mobile) {
        if (mobile == null) {
            return false;
        }
        if (mobile.length() != 11) {
            return false;
        }
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobile);
        if (!m.matches()) {
            return false;
        }
        return true;
    }

    /**
     *
     * @Title: transform
     * @Description: ����д��ĸת��Ϊ�»��ߺ�Сд�� userName--->user_name
     * @param @param name
     * @param @return
     * @return String ��������
     * @throws
     */
    public static String transform(String name) {
        String transform = null;
        if (name != null && name.length() > 0) {
            char[] chars = new char[name.length() * 2];
            int j = 0;
            for (int i = 0; i < name.length(); i++) {
                char charAt = name.charAt(i);
                // ����Ǵ�д�ַ�
                if (Character.isUpperCase(charAt)) {
                    // ����д�ַ�ת��ΪСд�ַ�
                    chars[j] = '_';
                    chars[j + 1] = Character.toLowerCase(charAt);
                    j += 2;
                } else {
                    chars[j] = charAt;
                    j++;
                }
            }
            transform = new String(chars).trim();
        }
        return transform;
    }

    /**
     * ��ȡһ��32λ�ַ�����uuid
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * ��ȡһ�����ȵ�����ַ���
     *
     * @param length
     *            ָ���ַ�������
     * @return һ�����ȵ��ַ���
     */
    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * @Title: getUserBirthdayPost
     * @Description: ��ȡ���û����������
     * @param @param date
     * @param @return
     * @return String ��������
     * @throws
     */
    public static String getUserBirthdayPost(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String year = String.valueOf(cal.get(Calendar.YEAR));
        return year.charAt(2) + "0��";
    }

    /**
     * �ַ���ת������
     * @param str Ҫת�����ַ���
     * @return  ת����Ķ���������
     */
    public static byte[] hex2byte(String str) { // �ַ���ת������
        if (str == null)
            return null;
        str = str.trim();
        int len = str.length();
        if (len == 0 || len % 2 == 1)
            return null;
        byte[] b = new byte[len / 2];
        try {
            for (int i = 0; i < str.length(); i += 2) {
                b[i / 2] = (byte) Integer.decode("0X" + str.substring(i, i + 2)).intValue();
            }
            return b;
        } catch (Exception e) {
            return null;
        }
    }



    public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };

    /**
     *
     * @Title: generateShortUuid
     * @Description: ���㷨����62���ɴ�ӡ�ַ���ͨ���������32λUUID������UUID��Ϊʮ�����ƣ����Խ�UUID�ֳ�8�飬
    ÿ4��Ϊһ�飬Ȼ��ͨ��ģ62�����������Ϊ����ȡ���ַ���
    �����ظ��ʴ�󽵵͡�
     * @param @return
     * @return String    ��������
     * @throws
     */
    public static String generateShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }

    public static void main(String[] args) {
        // System.out.println(fileName);
        // System.out.println(isNotEmpty(0.0));
//		System.out.println(transform("AbcD"));
//		System.out.println(getFileName());
        for (int i = 0; i < 10000; i++) {
//			System.out.println(UUID.randomUUID().toString().replace("-", ""));
            System.out.println(StringUtil.generateShortUuid());
        }
    }
}
