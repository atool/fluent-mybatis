package cn.org.atool.fluent.form.meta;

import lombok.ToString;

import static cn.org.atool.fluent.mybatis.mapper.StrConstant.EMPTY;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.lowerFirst;

/**
 * 方法名称中解析出来的字段及关联方式
 *
 * @author darui.wu
 */
@ToString
public class NameAndPair {
    public final String name;

    public final boolean isAnd;

    public NameAndPair(String name, boolean isAnd) {
        this.name = lowerFirst(name, EMPTY);
        this.isAnd = isAnd;
    }

    public static NameAndPair pair(String name, boolean isAnd) {
        return new NameAndPair(name, isAnd);
    }
}