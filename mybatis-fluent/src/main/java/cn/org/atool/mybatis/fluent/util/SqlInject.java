package cn.org.atool.mybatis.fluent.util;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 简单判断有没有sql注入风险
 *
 * @author darui.wu
 * @create 2019/10/10 10:17 上午
 */
public class SqlInject {
    private static final String[] KEYWORDS = {"and", "or", "not", "use", "insert", "delete", "update", "select", "count",
            "group", "union", "create", "drop", "truncate", "alter", "grant", "execute", "exec",
            "xp_cmdshell", "call", "declare", "source", "sql"};
    /**
     * 可能产生注入的危险字符串
     */
    private static final List<String> DANGER_CHAR = Arrays.asList(";", "--", "*", "%", "\\", "'", "#", "/");

    private static Map<Character, List<char[]>> KEYWORD_MAP = initMap(KEYWORDS);

    private static Map<Character, List<char[]>> DANGER_MAP = initMap(DANGER_CHAR.toArray(new String[0]));

    private static String KEYWORD_STR = "[" + Stream.of(KEYWORDS).collect(Collectors.joining(",")) + "]";

    private static String DANGER_STR = "[" + DANGER_CHAR.stream().collect(Collectors.joining(",")) + "]";

    private static final int TO_UPPER = 'A' - 'a';

    private static Map<Character, List<char[]>> initMap(String... words) {
        Map<Character, List<char[]>> map = new HashMap<>(256);
        Stream.of(words).forEach(word -> {
            char[] chars = word.toCharArray();
            char first = Character.toLowerCase(chars[0]);
            if (!map.containsKey(first)) {
                map.put(first, new ArrayList<>());
                map.put(Character.toUpperCase(first), map.get(first));
            }
            map.get(first).add(Arrays.copyOfRange(chars, 1, chars.length));
        });
        return map;
    }

    /**
     * 预先判断字段 propertyName的值 propertyValue 不包含sql注入字符
     *
     * @param propertyName  属性名称
     * @param propertyValue 具体属性值
     * @return 返回无注入风险的 propertyValue
     */
    public static String assertNoInject(String propertyName, String propertyValue) {
        if (probablySqlInject(propertyValue)) {
            throw new RuntimeException(propertyName + " can't contain following KeyWord: " + KEYWORD_STR + " or following danger characters: " + DANGER_STR);
        }
        return propertyValue;
    }

    /**
     * 是否含有sql注入，返回true表示含有
     *
     * @param str
     * @return
     */
    public static boolean probablySqlInject(String str) {
        if (str == null) {
            return false;
        }
        boolean isWordStart = true;
        int length = str.length();
        int index = 0;
        for (char ch : str.toCharArray()) {
            index++;
            if (isBlank(ch)) {
                isWordStart = true;
                continue;
            }
            if (isWordStart && isKeyWord(str, length, KEYWORD_MAP.get(ch), index, false)) {
                return true;
            }
            if (isKeyWord(str, length, DANGER_MAP.get(ch), index, true)) {
                return true;
            }
            isWordStart = false;
        }
        return false;
    }

    private static boolean isKeyWord(String str, int length, List<char[]> charsList, int index, boolean isDanger) {
        if (charsList == null) {
            return false;
        }
        for (char[] chars : charsList) {
            if (isKeyWord(str, length, chars, index, isDanger)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isKeyWord(String str, int length, char[] chars, int index, boolean isDanger) {
        int start = index;
        for (char ch : chars) {
            if (start >= length) {
                return false;
            }
            char charAt = str.charAt(start);
            if (ch != charAt && ch + TO_UPPER != charAt) {
                return false;
            }
            start++;
        }
        return isDanger || start == length || isBlank(str.charAt(start));
    }

    /**
     * 是否是空格符
     *
     * @param ch
     * @return
     */
    private static boolean isBlank(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r' || ch == 0;
    }

    /**
     * 简单判断是否可能存在SQL注入危险
     * 不允许可能产生注入的字符串
     *
     * @param str
     * @return
     */
    public static boolean hasSimpleInject(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        int index = 0;
        for (char ch : str.toCharArray()) {
            index++;
            if (isKeyWord(str, length, DANGER_MAP.get(ch), index, true)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 简单判断 propertyName的值 propertyValue 不包含sql危险注入字符
     * ";", "--", "*", "%", "\\", "'", "#", "/"
     *
     * @param propertyName  属性名称
     * @param propertyValue 具体属性值
     * @return 返回无注入风险的 propertyValue
     */
    public static String assertSimpleNoInject(String propertyName, String propertyValue) {
        if (hasSimpleInject(propertyValue)) {
            throw new RuntimeException(propertyName + " can't contain following string: " + DANGER_STR);
        }
        return propertyValue;
    }
}