package com.wenka.commons.util;


import java.util.Arrays;

/**
 * @author wenka
 */
public class SecurityCode {

    /**
     * 难度
     */
    public enum SecurityCodeLevel {
        Simple, Medium, Hard
    }

    /**
     * 字符池
     */
    private static final char[] CODES = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    /**
     * 获取默认简单4位数字字符
     *
     * @return
     */
    public static String getSecurityCode() {
        return getSecurityCode(4, SecurityCodeLevel.Simple, false);
    }

    /**
     * 自定义取随机字符
     *
     * @param length
     * @param level
     * @param isCanRepeat
     * @return
     */
    public static String getSecurityCode(int length, SecurityCodeLevel level, boolean isCanRepeat) {
        int len = length;
        char[] codes = new char[0];
        if (level == SecurityCodeLevel.Simple) {
            codes = new char[10];
            System.arraycopy(CODES, 0, codes, 0, 10);
        } else if (level == SecurityCodeLevel.Medium) {
            codes = new char[36];
            System.arraycopy(CODES, 0, codes, 0, 36);
        } else if (level == SecurityCodeLevel.Hard) {
            codes = new char[CODES.length];
            System.arraycopy(CODES, 0, codes, 0, CODES.length -1);
        }
        int n = codes.length;
        if (len > n && isCanRepeat == false) {
            throw new RuntimeException("随机字符提取错误");
        }
        char[] result = new char[len];
        if (isCanRepeat) {
            for (int i = 0; i < result.length; i++) {
                int r = (int) (Math.random() * n);
                result[i] = codes[r];
            }
        } else {
            for (int i = 0; i < result.length; i++) {
                int r = (int) (Math.random() * n);
                result[i] = codes[r];
                codes[r] = codes[n - 1];
                n--;
            }
        }
        return String.valueOf(result);
    }

    public static void main(String[] args){
        String securityCode = SecurityCode.getSecurityCode(6,SecurityCodeLevel.Hard,false);
        System.out.print(securityCode);
    }
}