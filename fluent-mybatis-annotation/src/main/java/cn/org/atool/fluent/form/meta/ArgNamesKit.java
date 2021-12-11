package cn.org.atool.fluent.form.meta;

import cn.org.atool.fluent.common.kits.SegmentLocks;
import cn.org.atool.fluent.form.annotation.MethodType;
import cn.org.atool.fluent.mybatis.model.KeyMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * ArgNamesKit: 方法参数解析工具
 *
 * @author wudarui
 */
public class ArgNamesKit {
    private static final KeyMap<MethodArgNames> cached = new KeyMap<>();

    private static final SegmentLocks<String> lock = new SegmentLocks<>(32);

    /**
     * 从 method...() 方法中解析字段名称
     *
     * @param method 方法名称
     * @return 条件字段列表
     */
    public static MethodArgNames parseMethodStyle(String method) {
        lock.lockDoing(cached::containsKey, method, () -> cached.put(method, internalParse(method)));
        return cached.get(method);
    }

    private static MethodArgNames internalParse(String method) {
        int index = method.indexOf("By");
        if (index <= 0) {
            return MethodArgNameNotFound;
        }
        String prefix = method.substring(0, index);
        if (MethodType.AUTO_PREFIX.contains(prefix)) {
            MethodArgNames names = parseArgNames(method, index + 2);
            if (Objects.equals("top", prefix)) {
                names.setTopN(1);
            }
            return names;
        } else if (prefix.matches("top\\d+")) {
            MethodArgNames names = parseArgNames(method, index + 2);
            names.setTopN(Integer.parseInt(prefix.substring(3)));
            return names;
        } else {
            return MethodArgNameNotFound;
        }
    }

    /* Collections.emptyList() 表示从方法名称中未解析到参数名 */
    private static final MethodArgNames MethodArgNameNotFound = new MethodArgNames(true, Collections.emptyList());

    private static MethodArgNames parseArgNames(String method, int offset) {
        List<String> names = new ArrayList<>();
        StringBuilder name = new StringBuilder();
        Boolean isAnd = null;
        int orderOffset = 0;
        for (int index = offset; index < method.length(); ) {
            char ch = method.charAt(index);
            if (matchWord(method, index, "And") && isCapital(method, index + 3)) {
                names.add(name.toString());
                name = new StringBuilder();
                index += 3;
                if (Objects.equals(isAnd, false)) {
                    throw new IllegalStateException("The method name[" + method + "] cannot contain both and or logic.");
                }
                isAnd = true;
            } else if (matchWord(method, index, "Or") && isCapital(method, index + 2)) {
                names.add(name.toString());
                name = new StringBuilder();
                index += 2;
                if (Objects.equals(isAnd, true)) {
                    throw new IllegalStateException("The method name[" + method + "] cannot contain both and or logic.");
                }
                isAnd = false;
            } else if (matchWord(method, index, "OrderBy") && isCapital(method, index + 7)) {
                orderOffset = index + 7;
                break;
            } else {
                name.append(ch);
                index += 1;
            }
        }
        if (!name.toString().isEmpty()) {
            names.add(name.toString());
        }
        MethodArgNames methodArgNames = new MethodArgNames(isAnd == null || isAnd, names);
        if (orderOffset == 0) {
            return methodArgNames;
        }
        name = new StringBuilder();
        for (int index = orderOffset; index < method.length(); ) {
            char ch = method.charAt(index);
            if (matchWord(method, index, "Asc") && isCapital(method, index + 3)) {
                methodArgNames.addOrderBy(name.toString(), true);
                name = new StringBuilder();
                index += 3;
            } else if (matchWord(method, index, "Desc") && isCapital(method, index + 4)) {
                methodArgNames.addOrderBy(name.toString(), false);
                name = new StringBuilder();
                index += 4;
            } else {
                name.append(ch);
                index += 1;
            }
        }
        if (!name.toString().isEmpty()) {
            methodArgNames.addOrderBy(name.toString(), true);
        }
        return methodArgNames;
    }

    static boolean isCapital(String text, int index) {
        if (text.length() <= index) {
            return true;
        } else {
            char ch = text.charAt(index);
            return ch >= 'A' && ch <= 'Z';
        }
    }

    /**
     * text文本从start位置点开始是子字符word
     *
     * @param text  完整文本
     * @param start 匹配偏移点
     * @param word  要匹配的字符串
     * @return true/false
     */
    public static boolean matchWord(String text, int start, String word) {
        if (text.length() < start + word.length()) {
            return false;
        }
        for (int index = 0; index < word.length(); index++) {
            if (text.charAt(start + index) != word.charAt(index)) {
                return false;
            }
        }
        return true;
    }
}