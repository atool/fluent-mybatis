package cn.org.atool.fluent.mybatis.test.where.and;

import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class WhereObjectTest_Le extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void le() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().le(34)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT COUNT(*) FROM fluent_mybatis.student WHERE `age` <= ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void le_condition() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().le(34, o -> true)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM fluent_mybatis.student WHERE `age` <= ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }

    @Test
    public void le_IfNotNull() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.age().le(34, Objects::nonNull)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM fluent_mybatis.student WHERE `age` <= ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{34});
    }
}
