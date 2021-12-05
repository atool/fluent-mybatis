package cn.org.atool.fluent.form.meta;

import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

import static cn.org.atool.fluent.mybatis.mapper.StrConstant.EMPTY;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.lowerFirst;

/**
 * 方法名称中解析出来的字段及关联方式
 *
 * @author darui.wu
 */
@ToString
public class MethodArgNames {
    public final boolean isAnd;

    public final List<String> names;

    public MethodArgNames(boolean isAnd, List<String> names) {
        this.names = names.stream().map(name -> lowerFirst(name, EMPTY)).collect(Collectors.toList());
        this.isAnd = isAnd;
    }

    public int size() {
        return this.names.size();
    }

    public static MethodArgNames build(boolean isAnd, List<String> names) {
        return new MethodArgNames(isAnd, names);
    }

    public String get(int index) {
        if (index < this.names.size()) {
            return this.names.get(index);
        } else {
            return null;
        }
    }
}