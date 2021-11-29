package cn.org.atool.fluent.form.kits;

import cn.org.atool.fluent.form.meta.NameAndPair;

import java.util.ArrayList;
import java.util.List;

/**
 * FinderNameKit
 *
 * @author wudarui
 */
public interface FinderNameKit {
    /**
     * 从 findBy...() 方法中解析字段名称
     *
     * @param findMethod 方法名称
     * @return 条件字段列表
     */
    static List<NameAndPair> parseFindFields(String method) {
        if (!method.startsWith("findBy")) {
            return null;
        }
        List<NameAndPair> pairs = new ArrayList<>();
        StringBuilder name = new StringBuilder();
        boolean isAnd = true;
        for (int index = 6; index < method.length(); ) {
            char ch = method.charAt(index);
            if (matchWord(method, index, "And") && isCapital(method.charAt(index + 3))) {
                pairs.add(new NameAndPair(name.toString(), isAnd));
                name = new StringBuilder();
                index += 3;
                isAnd = true;
            } else if (matchWord(method, index, "Or") && isCapital(method.charAt(index + 2))) {
                pairs.add(new NameAndPair(name.toString(), isAnd));
                name = new StringBuilder();
                index += 2;
                isAnd = false;
            } else {
                name.append(ch);
                index += 1;
            }
        }
        pairs.add(new NameAndPair(name.toString(), isAnd));
        return pairs;
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