package org.com.utils;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 
 * @Description: 汉语拼音工具类
 * @author binary
 */
public class PinyinUtil {

    private final static Logger logger = LoggerFactory.getLogger(Thread.currentThread().getClass());

    /**
     * 
     * @MethodName: getFullPinyin
     * @Description: 获取字符串的全拼
     * @param src
     * @return
     */
    public static String getFullPinyin(String src) {
        if (StringUtils.isNotBlank(src)) {
            char[] srcCharArray = src.toCharArray();
            HanyuPinyinOutputFormat pyformat = new HanyuPinyinOutputFormat();
            pyformat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            pyformat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            pyformat.setVCharType(HanyuPinyinVCharType.WITH_V);
            StringBuilder rs = new StringBuilder();
            try {
                for (int i = 0; i < srcCharArray.length; i++) {
                    if (Character.toString(srcCharArray[i]).matches("[\\u4E00-\\u9FA5]+")) {
                        rs.append(PinyinHelper.toHanyuPinyinStringArray(srcCharArray[i], pyformat)[0]);
                    } else {
                        rs.append(srcCharArray[i]);
                    }
                }
                return rs.toString();
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                logger.error(e.getMessage(), e);
            }
        }
        return "";
    }

    /**
     * 
     * @MethodName: getFullPinyinFirst
     * @Description: 获取字符串的简拼
     * @param src
     * @return
     */
    public static String getJianPin(String src) {
        if (StringUtils.isNotBlank(src)) {
            StringBuilder rs = new StringBuilder();
            for (int i = 0; i < src.length(); i++) {
                char word = src.charAt(i);
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
                if (pinyinArray != null) {
                    rs.append(pinyinArray[0].charAt(0));
                } else {
                    rs.append(word);
                }
            }
            return rs.toString();
        }
        return "";
    }

    /**
     * 
     * @MethodName: getInitial
     * @Description: 获取字符串的首字母 如果是数字返回#
     * @param src
     * @return
     */
    public static String getInitial(String src) {
        if (StringUtils.isNotBlank(src)) {
            String jianPin = getJianPin(src);
            if (StringUtils.isNotBlank(jianPin)) {
                String rs = jianPin.substring(0, 1);
                if (Pattern.compile("[0-9]*").matcher(rs).matches())
                    rs = "#";
                return rs;
            }
        }
        return "";
    }

    public static void main(String[] args) {
        String src = "北京市";
        String src_number = "1390";
        String src_empty = "";
        String src_special = "$%";

        System.err.println(getFullPinyin(src));
        System.err.println(getJianPin(src));
        System.err.println(getInitial(src));

        System.err.println(getFullPinyin(src_number));
        System.err.println(getJianPin(src_number));
        System.err.println(getInitial(src_number));

        System.err.println(getFullPinyin(src_empty));
        System.err.println(getJianPin(src_empty));
        System.err.println(getInitial(src_empty));

        System.err.println(getFullPinyin(src_special));
        System.err.println(getJianPin(src_special));
        System.err.println(getInitial(src_special));
    }

}
