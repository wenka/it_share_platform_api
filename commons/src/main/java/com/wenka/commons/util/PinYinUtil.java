package com.wenka.commons.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-5.
 */
public class PinYinUtil {
    public PinYinUtil() {
    }

    public static String getFirstSpell(String chinese) {
        if (chinese != null && !"".equals(chinese)) {
            StringBuffer pybf = new StringBuffer();
            char[] arr = chinese.toCharArray();
            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
            defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

            for (int i = 0; i < arr.length; ++i) {
                if (arr[i] > 128) {
                    try {
                        String[] e = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
                        if (e != null) {
                            pybf.append(e[0].charAt(0));
                        }
                    } catch (BadHanyuPinyinOutputFormatCombination var6) {
                        var6.printStackTrace();
                    }
                } else {
                    pybf.append(arr[i]);
                }
            }

            return pybf.toString().replaceAll("\\W", "").trim().toLowerCase();
        } else {
            return null;
        }
    }

    public static String getFullSpell(String chinese) {
        if (chinese != null && !"".equals(chinese)) {
            StringBuffer pybf = new StringBuffer();
            char[] arr = chinese.toCharArray();
            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
            defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

            for (int i = 0; i < arr.length; ++i) {
                if (arr[i] > 128) {
                    try {
                        pybf.append(PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat)[0]);
                    } catch (BadHanyuPinyinOutputFormatCombination var6) {
                        var6.printStackTrace();
                    }
                } else {
                    pybf.append(arr[i]);
                }
            }

            return pybf.toString().toLowerCase();
        } else {
            return null;
        }
    }
}
