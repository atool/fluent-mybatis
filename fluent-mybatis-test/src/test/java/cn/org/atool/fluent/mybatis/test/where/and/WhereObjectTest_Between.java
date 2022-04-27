package cn.org.atool.fluent.mybatis.test.where.and;

import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WhereObjectTest_Between extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void between() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().between(23, 40).end();
        mapper.count(query);
        db.sqlList().wantFirstSql().where().eq("`age` BETWEEN ? AND ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{23, 40});
    }

    @Test
    public void between_condition_true() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().between(23, 40, (v1, v2) -> true).end();
        mapper.count(query);
        db.sqlList().wantFirstSql().where().eq("`age` BETWEEN ? AND ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{23, 40});
    }

    @Test
    public void between_condition_false() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where
            .age().between(23, 40, (v1, v2) -> false)
            .userName().like("user")
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().notContain("BETWEEN");
    }

    @Test
    public void notBetween() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where
            .age().notBetween(23, 40)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().where().eq("`age` NOT BETWEEN ? AND ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{23, 40});
    }

    @Test
    public void notBetween_condition_true() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().notBetween(23, 40, (v1, v2) -> true).end();
        mapper.count(query);
        db.sqlList().wantFirstSql().where().eq("`age` NOT BETWEEN ? AND ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{23, 40});
    }

    @Test
    public void notBetween_condition_false() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().notBetween(23, 40, (v1, v2) -> false).end();
        mapper.count(query);
        db.sqlList().wantFirstSql().notContain("BETWEEN");
    }
}
