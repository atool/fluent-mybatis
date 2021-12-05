package cn.org.atool.fluent.form.kits;

import cn.org.atool.fluent.form.meta.MethodArgNames;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * FinderNameKit
 *
 * @author wudarui
 */
public interface FinderNameKit {
    /**
     * 从 findBy...() 方法中解析字段名称
     *
     * @param method 方法名称
     * @return 条件字段列表
     */
    static MethodArgNames parseFindFields(String method) {
        if (!method.startsWith("findBy")) {
            return new MethodArgNames(true, Collections.emptyList());
        }
        List<String> names = new ArrayList<>();
        StringBuilder name = new StringBuilder();
        Boolean isAnd = null;
        for (int index = 6; index < method.length(); ) {
            char ch = method.charAt(index);
            if (matchWord(method, index, "And") && isCapital(method.charAt(index + 3))) {
                names.add(name.toString());
                name = new StringBuilder();
                index += 3;
                if (Objects.equals(isAnd, false)) {
                    throw new IllegalStateException("The method name[" + method + "] cannot contain both and or logic.");
                }
                isAnd = true;
            } else if (matchWord(method, index, "Or") && isCapital(method.charAt(index + 2))) {
                names.add(name.toString());
                name = new StringBuilder();
                index += 2;
                if (Objects.equals(isAnd, true)) {
                    throw new IllegalStateException("The method name[" + method + "] cannot contain both and or logic.");
                }
                isAnd = false;
            } else {
                name.append(ch);
                index += 1;
            }
        }
        names.add(name.toString());
        return new MethodArgNames(isAnd == null || isAnd, names);
    }

    static boolean isCapital(char ch) {
        return ch >= 'A' && ch <= 'Z';
    }

    static boolean matchWord(String text, int start, String word) {
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