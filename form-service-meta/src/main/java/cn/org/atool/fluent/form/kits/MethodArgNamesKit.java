package cn.org.atool.fluent.form.kits;

import cn.org.atool.fluent.form.meta.MethodArgNames;
import cn.org.atool.fluent.mybatis.base.model.KeyMap;
import cn.org.atool.fluent.mybatis.utility.LockKit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static cn.org.atool.fluent.form.kits.MethodStyle.*;

/**
 * FinderNameKit
 *
 * @author wudarui
 */
public class MethodArgNamesKit {
    private static final KeyMap<MethodArgNames> cached = new KeyMap<>();

    private static final LockKit<String> lock = new LockKit<>(32);

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
        if (isFindBy(method)) {
            return parseArgNames(FindBy, method, FindBy.offset);
        } else if (isCountBy(method)) {
            return parseArgNames(CountBy, method, CountBy.offset);
        } else if (isExistsBy(method)) {
            return parseArgNames(ExistsBy, method, ExistsBy.offset);
        } else {
            int[] ret = isTopNBy(method);
            if (ret == null) {
                /* Collections.emptyList() 表示从方法名称中未解析到参数名 */
                return new MethodArgNames(null, true, Collections.emptyList());
            } else {
                return parseArgNames(TopNBy, method, ret[0]).setTopN(ret[1]);
            }
        }
    }

    private static MethodArgNames parseArgNames(MethodStyle type, String method, int offset) {
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
        MethodArgNames methodArgNames = new MethodArgNames(type, isAnd == null || isAnd, names);
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