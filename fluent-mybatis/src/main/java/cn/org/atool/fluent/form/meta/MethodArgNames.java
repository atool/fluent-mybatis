package cn.org.atool.fluent.form.meta;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.org.atool.fluent.mybatis.utility.StrConstant.EMPTY;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.lowerFirst;

/**
 * 方法名称中解析出来的字段及关联方式
 *
 * @author darui.wu
 */
@ToString
@Accessors(chain = true)
public class MethodArgNames {
    public final boolean isAnd;

    public final List<String> names;

    public final List<OrderBy> orderBy = new ArrayList<>();

    @Setter
    @Getter
    private int topN = 0;

    public MethodArgNames(boolean isAnd, List<String> names) {
        this.names = names.stream().map(name -> lowerFirst(name, EMPTY)).collect(Collectors.toList());
        this.isAnd = isAnd;
    }

    public int size() {
        return this.names.size();
    }

    public String get(int index) {
        if (index < this.names.size()) {
            return this.names.get(index);
        } else {
            return null;
        }
    }

    public void addOrderBy(String name, boolean isAsc) {
        this.orderBy.add(new OrderBy(name, isAsc));
    }

    public static MethodArgNames build(boolean isAnd, List<String> names) {
        return new MethodArgNames(isAnd, names);
    }

    @ToString
    public static class OrderBy {
        public String name;

        public boolean isAsc;

        public OrderBy(String name, boolean isAsc) {
            this.name = name;
            this.isAsc = isAsc;
        }
    }
}