package org.ssm.dufy.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.regex.Pattern;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

public class NumericUtil {
    // Ĭ�ϳ������㾫��
    private static final int DEF_DIV_SCALE = 10;
    /**
     * ETFFSL���Ĭ��ֵ
     */
    public static final double ETFFSL_DEFAULT_DOUBLE = 0.0000;

    public static boolean checkNumber(double value) {
        String str = String.valueOf(value);
        String regex = "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";
        return str.matches(regex);
    }

    public static boolean checkNumber(int value) {
        String str = String.valueOf(value);
        String regex = "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";
        return str.matches(regex);
    }

    public static boolean checkNumber(String value) {
        String regex = "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";
        return value.matches(regex);
    }

    /**
     * ������λ
     * 
     * @param money
     *            ���
     * @return ����
     */
    public static double convertDouble(Double money) {
        if (null == money) {
            return ETFFSL_DEFAULT_DOUBLE;
        }
        return Math.round(money * 10000) / 10000.0;
    }

    /**
     * ��ʽ��double����ֵ��ʹ����ĩβ������λС��
     * 
     * @param value
     * @return
     */
    public static String formatDouble(double value) {
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(value);
    }

    /**
     * ��������С��
     * 
     * @return
     */
    public static String formatDouble(Double b) {
        BigDecimal bg = new BigDecimal(b);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * �����ƶ�λ�������
     */
    public static String randomNumeric(int i) {
        return RandomStringUtils.randomNumeric(i);
    }

    /**
     * ȡ��һ�������,ȡֵ��Χ��0 <= value < scope
     * 
     * @param scope
     *            ���ֵ
     */
    public static int getRandom(int scope) {
        Random random = new Random();
        return random.nextInt(scope);
    }

    /**
     * ���ַ�������ת��Ϊint������
     * 
     * @param str��ת���ַ���
     * @param defValueת��ʧ�ܺ��Ĭ��ֵ
     * @return int
     */
    public static short parseShort(String str, short defValue) {
        if (str == null) return  0;
        try {
            return Short.parseShort(str);
        } catch (Exception e) {
            return defValue;
        }
    }

    /**
     * ���ַ�������ת��Ϊint������
     * 
     * @param str��ת���ַ���
     * @param defValueת��ʧ�ܺ��Ĭ��ֵ
     * @return int
     */
    public static int parseInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return defValue;
        }
    }

    /**
     * ���ַ�������ת��Ϊlong������
     * 
     * @param str��ת���ַ���
     * @param defValueת��ʧ�ܺ��Ĭ��ֵ
     * @return long
     */
    public static long parseLong(String str, long defValue) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            return defValue;
        }
    }

    /**
     * ���ַ�������ת��Ϊdouble������
     * 
     * @param str��ת���ַ���
     * @param defValueת��ʧ�ܺ��Ĭ��ֵ
     * @return double
     */
    public static double parseDouble(String str, double defValue) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return defValue;
        }
    }
    
    /**
     * �����ж�
     * @param count
     * @return
     */
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();   
     }  

    /**
     * �ڲ���lenλ������ǰ���Զ�����
     */
    public static String getLimitLenStr(String str, int len) {
        if (str == null) {
            return "";
        }
        while (str.length() < len) {
            str = "0" + str;
        }
        return str;
    }

    /**
    * �������־��� ��Ҫ��ʽ��������
    * 
    * @param value
    *            double ����������0.00��ʾ��ȷ��С�������λ��
    * @param precision
    *            String
    * @return double
    */
   public static double setPrecision(double value, String precision) {
       return Double.parseDouble(new DecimalFormat(precision).format(value));
   }

   public static boolean validateCommonDig(String value) {
       if (StringUtils.isNotBlank(value)) {
           try {
               Double.parseDouble(StringUtil.trim(value));
               return true;
           } catch (Exception ex) {

           }
       }
       return false;
   }

   public static double division(double numerator, double denominator,
           int digit) {
       double result = 0;
       result = Math.round((numerator / denominator) * Math.pow(10, digit))
               / Math.pow(10, digit);
       return result;
   }

   /**
    * �ṩ��ȷ�ļ������㡣
    * 
    * @param v1
    *            ������
    * @param v2
    *            ����
    * @return ���������Ĳ�
    */
   public static double sub(double v1, double v2) {
       BigDecimal b1 = new BigDecimal(Double.toString(v1));
       BigDecimal b2 = new BigDecimal(Double.toString(v2));
       return b1.subtract(b2).doubleValue();
   }

   /**
    * �ṩ��ȷ�ļӷ����㡣
    * 
    * @param v1
    *            ������
    * @param v2
    *            ����
    * @return ���������ĺ�
    */
   public static double add(double v1, double v2) {
       BigDecimal b1 = new BigDecimal(Double.toString(v1));
       BigDecimal b2 = new BigDecimal(Double.toString(v2));
       return b1.add(b2).doubleValue();
   }

   /**
    * �ṩ��ȷ�ĳ˷����㡣
    * 
    * @param v1
    *            ������
    * @param v2
    *            ����
    * @return ���������Ļ�
    */
   public static double mul(double v1, double v2) {
       BigDecimal b1 = new BigDecimal(Double.toString(v1));
       BigDecimal b2 = new BigDecimal(Double.toString(v2));
       return b1.multiply(b2).doubleValue();
   }

   /**
    * �ṩ����ԣ���ȷ�ĳ������㣬�����������������ʱ����ȷ�� С�����Ժ�10λ���Ժ�������������롣
    * 
    * @param v1
    *            ������
    * @param v2
    *            ����
    * @return ������������
    */
   public static double div(double v1, double v2) {
       return div(v1, v2, DEF_DIV_SCALE);
   }

   /**
    * �ṩ����ԣ���ȷ�ĳ������㡣�����������������ʱ����scale����ָ �����ȣ��Ժ�������������롣
    * 
    * @param v1
    *            ������
    * @param v2
    *            ����
    * @param scale
    *            ��ʾ��ʾ��Ҫ��ȷ��С�����Ժ�λ��
    * @return ������������
    */
   public static double div(double v1, double v2, int scale) {
       if (scale < 0) {
           throw new IllegalArgumentException("The scale must be a positive integer or zero");
       }
       BigDecimal b1 = new BigDecimal(Double.toString(v1));
       BigDecimal b2 = new BigDecimal(Double.toString(v2));
       return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
   }

   /**
    * Stringת��double
    * 
    * @param string
    * @return double
    */
   public static double convertSourData(String dataStr) throws Exception {
       if (dataStr != null && !"".equals(dataStr)) {
           return Double.valueOf(dataStr);
       }
       throw new NumberFormatException("convert error!");
   }

   /**
    * str����ˡ������������ȡ�ä��롣����Ǥ��ʤ����ϡ�0����롣<br>
    * 
    * @param str
    *            ��Q����������
    * 
    * @return ���֤� ��Q�Ǥ��ʤ���С�0�������
    */
   public static int strToInt(String str) {

       // �����ФΥ��ک`����ȡ��
       str = StringUtil.nvl(str);

       int intRet = 0;
       // ���Q��������꥿�`�󤹤�
       try {
           // ����Q������
           intRet = Integer.parseInt(str);
       } catch (NumberFormatException e) {
           // ��0����롣
           return 0;
       }
       return intRet;
   }

   /**
    * str����ˡ������������ȡ�ä��롣����Ǥ��ʤ����ϡ�0����롣<br>
    * 
    * @param str
    *            ��Q����������
    * 
    * @return ���֤� ��Q�Ǥ��ʤ���С�0�������
    */
   public static BigDecimal strToBigDecimal(String str) {
       // �����ФΥ��ک`����ȡ�롣
       str = StringUtil.nvl(str);
       // ��Q��������꥿�`�󤹤�
       return new BigDecimal(str);
   }

   /**
    * src����ˡ�ָ�����줿len��"0"���a�㤹�롣<br>
    * 
    * @param src
    *            ��������
    * 
    * @param len
    *            �a��Y������
    * 
    * @return ������a�㤷������ <br>
    *         �����С�fillZeroToLen(123, 4)�� "0123"��ȡ�ä��롣<br>
    *         fillZeroToLen(123, 6)�� "000123"��ȡ�ä��롣
    */
   public static String fillZeroToLen(int src, int len) {

       // ͬ���v������ӳ���
       return fillZeroToLen(String.valueOf(src), len);
   }

   /**
    * src����ˡ�ָ�����줿len��"0"���a�㤹�롣<br>
    * 
    * @param src
    *            ��������
    * 
    * @param len
    *            �a��Y������
    * 
    * @return ������a�㤷������ <br>
    *         �����С�fillZeroToLen("123", 4)�� "0123"��ȡ�ä��롣<br>
    *         fillZeroToLen("123", 6)�� "000123"��ȡ�ä��롣
    */
   public static String fillZeroToLen(String src, int len) {
       StringBuilder sbTemp = new StringBuilder();
       for (int i = 0; i < len; i++) {
           sbTemp.append("0");
       }
       sbTemp.append(src);

       String strTemp = sbTemp.toString();
       return strTemp.substring(strTemp.length() - len);
   }

   /**
    * "1,234,567.123"�Τ褦�ʻ����ʾ��ʽ�ν��~�����Ф��顢 DB�˒��뤹�뤿���"1234567.123"�Τ褦����ʽ�ˉ�Q���롣
    * 
    * @param src
    *            ��ʾ�ý��~�ե��`�ޥå�
    * 
    * @return �ե��`�ޥåȽY�� <br>
    *         �����С�formatKingakuForDB("1,234,567.123")��"1234567.123"��ȡ�ä��롣
    */
   public static String formatKingakuForDB(String src) {

       // ����~�ѥ��`�����ж�
       if (StringUtil.isEmpty(src)) {
           // ����~�����O��
           src = "0";
       } else {
           // ����~�Θ���
           src = src.replaceAll(",", "");

       }
       // ��Q�Y�������
       return String.valueOf(strToBigDecimal(src));
   }

   /**
    * "1234567.123"�Τ褦��DB����ν��~�����Ф��顢 ����˱�ʾ���뤿���"1,234,567.123"�Τ褦����ʽ�ˉ�Q���롣<br>
    * 
    * @param kingaku
    *            ��ʾ�ý��~�ե��`�ޥå�
    * 
    * @return �ե��`�ޥåȽY��<br>
    *         �����С�formatKingakuForGamen("1234567.123")��"1,234,567.123"��ȡ�ä��롣
    */
   public static String formatKingakuForGamen(String kingaku) {

       String formatter = "#,##0.000";
       DecimalFormat deFormat = new DecimalFormat(formatter);
       // ��Q�Y�������
       return deFormat.format(strToBigDecimal(kingaku));
   }

   public static Double objectToDouble(Object argObj) {
       if (argObj == null) {
           return new Double(0);
       } else if ("".equals(argObj)) {
           return new Double(0);
       } else {
           return Double.parseDouble(argObj.toString());
       }
   }
}
