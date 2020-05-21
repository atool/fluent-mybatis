package cn.org.atool.fluent.mybatis.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * String 工具类
 *
 * @author darui.wu
 */
public class StringUtils {
    /**
     * 验证字符串是否是数据库字段
     */
    private static final Pattern P_IS_COLUMN = Pattern.compile("^\\w\\S*[\\w\\d]*$");

    /**
     * 首尾有转义符, 去除掉转义符, 获取真正的字段名
     *
     * @param column 字段名
     * @return 字段名
     */
    public static String getTargetColumn(String column) {
        if (P_IS_COLUMN.matcher(column).matches()) {
            return column;
        } else {
            return column.substring(1, column.length() - 1);
        }
    }

    /**
     * 安全的进行字符串 format
     *
     * @param target 目标字符串
     * @param params format 参数
     * @return format 后的
     */
    public static String format(String target, Object... params) {
        if (target.contains("%s") && ArrayUtils.isNotEmpty(params)) {
            return String.format(target, params);
        } else {
            return target;
        }
    }

    /**
     * 判断字符串是否为空
     *
     * @param cs 需要判断字符串
     * @return 判断结果
     */
    public static boolean isEmpty(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否不为空
     *
     * @param cs 需要判断字符串
     * @return 判断结果
     */
    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }


    /**
     * 第一个字母大写
     */
    public static String capitalFirst(final String str) {
        if (str == null || str.length() == 0) {
            return str;
        } else {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
    }

    /**
     * 判断对象是否为空
     *
     * @param object ignore
     * @return ignore
     */
    public static boolean checkValNotNull(Object object) {
        if (object instanceof CharSequence) {
            return isNotEmpty((CharSequence) object);
        } else {
            return object != null;
        }
    }

    /**
     * 是否为CharSequence类型
     *
     * @param clazz class
     * @return true 为是 CharSequence 类型
     */
    public static boolean isCharSequence(Class<?> clazz) {
        return clazz != null && CharSequence.class.isAssignableFrom(clazz);
    }

    static Map<Character, String> Escape_Char = new HashMap<>();

    static {
        Escape_Char.put((char) 0, "\\0");
        Escape_Char.put('\n', "\\n");
        Escape_Char.put('\r', "\\r");
        Escape_Char.put('\\', "\\\\");
        Escape_Char.put('\'', "\\\'");
        Escape_Char.put('"', "\\\"");
        Escape_Char.put('\032', "\\Z");
    }

    /**
     * 转义字符串。纯转义，不添加单引号。
     *
     * @param escapeStr 被转义的字符串
     * @return 转义后的字符串
     */
    public static String escapeRawString(String escapeStr) {
        StringBuilder buf = new StringBuilder((int) (escapeStr.length() * 1.1));
        for (char c : escapeStr.toCharArray()) {
            if (Escape_Char.containsKey(c)) {
                buf.append(Escape_Char.get(c));
            } else {
                buf.append(c);
            }
        }
        return buf.toString();
    }

    public static String getVersionBanner() {
        Package pkg = StringUtils.class.getPackage();
        String version = (pkg != null ? pkg.getImplementationVersion() : "");
        StringBuilder buff = new StringBuilder()
            .append("  _____   _                          _    \n")
            .append(" |  ___| | |  _   _    ___   _ __   | |_  \n")
            .append(" | |_    | | | | | |  ( _ ) | '_ L  | __| \n")
            .append(" |  _|   | | | |_| | |  __) | | | | | |_  \n")
            .append(" |_|     |_| |___,_| |____| |_| |_| |___| \n")
            .append(" __  __         ____          _    _      \n")
            .append("| )  ( | _   _ | __ )   __ _ | |_ (_) ___ \n")
            .append("| |)(| || | | ||  _ L  { _` || __|| || __|\n")
            .append("| |  | || |_| || |_) || (_| || |_ | |(__ )\n")
            .append("|_|  |_| L__, ||____)  (__,_| L__||_||___}\n")
            .append("         |___)                            \n")
            .append(version == null ? "" : version + " \n");
        return buff.toString();
    }
}