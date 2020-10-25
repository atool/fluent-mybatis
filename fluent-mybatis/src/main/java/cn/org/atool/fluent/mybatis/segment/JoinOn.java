package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.segment.where.BaseWhere;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JoinOn {
    private List<String> where = new ArrayList<>();

    public JoinOn on(BaseWhere w1, BaseWhere w2) {
        String column1 = ((WhereApply) w1).getCurrent().column;
        String column2 = ((WhereApply) w2).getCurrent().column;
        this.where.add(String.format("t1.%s = t2.%s", column1, column2));
        return this;
    }

    public String where() {
        return this.where.stream().collect(Collectors.joining(" AND "));
    }
}
