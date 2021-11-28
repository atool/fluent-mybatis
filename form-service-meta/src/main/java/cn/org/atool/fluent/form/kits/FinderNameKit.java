package cn.org.atool.fluent.form.kits;

import cn.org.atool.fluent.mybatis.mapper.StrConstant;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;

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
    static String[] parseFindFields(String findMethod) {
        if (!findMethod.startsWith("findBy")) {
            return null;
        }
        String sub = findMethod.substring(6);
        for (int index = 0; index < sub.length(); ) {
            char ch = sub.charAt(index);
            if (ch == 'A' && isAnd(sub, index)) {

            } else if (ch == 'O' && isOr(sub, index)) {

            } else {
                index++;
            }
        }
        String[] fields = sub.split("And");
        for (int index = 0; index < fields.length; index++) {
            fields[index] = MybatisUtil.lowerFirst(fields[index], StrConstant.EMPTY);
        }
        return fields;
    }

    static boolean isOr(String str, int index) {
        if (str.length() < index + 3) {
            return false;
        } else if (str.charAt(index) == 'O' &&
            str.charAt(index + 1) == 'r' &&
            str.charAt(index + 2) >= 'A' && str.charAt(index + 2) <= 'Z'
        ) {
            return true;
        } else {
            return false;
        }
    }

    static boolean isAnd(String str, int index) {
        if (str.length() < index + 4) {
            return false;
        } else if (str.charAt(index) == 'A' &&
            str.charAt(index + 1) == 'n' &&
            str.charAt(index + 2) == 'd' &&
            str.charAt(index + 3) >= 'A' && str.charAt(index + 3) <= 'Z'
        ) {
            return true;
        } else {
            return false;
        }
    }
}
