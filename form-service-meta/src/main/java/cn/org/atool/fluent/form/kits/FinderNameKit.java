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
        String[] fields = sub.split("And");
        for (int index = 0; index < fields.length; index++) {
            fields[index] = MybatisUtil.lowerFirst(fields[index], StrConstant.EMPTY);
        }
        return fields;
    }
}
