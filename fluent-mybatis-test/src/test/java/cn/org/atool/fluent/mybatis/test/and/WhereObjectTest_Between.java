package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WhereObjectTest_Between extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void between() {
        StudentQuery query = new StudentQuery()
            .where.age().between(23, 40).end();
        mapper.count(query);
        db.sqlList().wantFirstSql().where().eq("age BETWEEN ? AND ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{23, 40});
    }

    @Test
    public void between_condition_true() {
        StudentQuery query = new StudentQuery()
            .where.age().between(23, 40, (v1, v2) -> true).end();
        mapper.count(query);
        db.sqlList().wantFirstSql().where().eq("age BETWEEN ? AND ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{23, 40});
    }

    @Test
    public void between_condition_false() {
        StudentQuery query = new StudentQuery()
            .where
            .age().between(23, 40, (v1, v2) -> false)
            .userName().like("user")
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().notContain("BETWEEN");
    }

    @Test
    public void notBetween() {
        StudentQuery query = new StudentQuery()
            .where
            .age().notBetween(23, 40)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().where().eq("age NOT BETWEEN ? AND ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{23, 40});
    }

    @Test
    public void notBetween_condition_true() {
        StudentQuery query = new StudentQuery()
            .where.age().notBetween(23, 40, (v1, v2) -> true).end();
        mapper.count(query);
        db.sqlList().wantFirstSql().where().eq("age NOT BETWEEN ? AND ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{23, 40});
    }

    @Test
    public void notBetween_condition_false() {
        StudentQuery query = new StudentQuery()
            .where.age().notBetween(23, 40, (v1, v2) -> false).end();
        mapper.count(query);
        db.sqlList().wantFirstSql().notContain("BETWEEN");
    }
}
