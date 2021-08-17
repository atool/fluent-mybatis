package cn.org.atool.fluent.mybatis.test.segment2;

import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WhereApplyTest2 extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    void apply() {
        mapper.listEntity(new StudentQuery()
            .where.userName().applyFunc(SqlOp.EQ, "user_name+1").end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `user_name` = user_name+1");
    }

    @Test
    void apply_any() {
        mapper.listEntity(new StudentQuery()
            .where.age().applyFunc(SqlOp.GT, "any(?)",
                new StudentQuery().select.age().end()
                    .where.id().lt(30L).end())
            .and.id().gt(50L)
            .end()
        );
        db.sqlList().wantFirstSql().end("" +
            "WHERE `age` > any(SELECT `age` FROM fluent_mybatis.student WHERE `id` < ?) " +
            "AND `id` > ?");
    }

    @Test
    void apply_date() {
        mapper.listEntity(new StudentQuery()
            .where.gmtModified().applyFunc(SqlOp.GT, "DATE_ADD(gmt_created, INTERVAL ? DAY)", 10)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("FROM fluent_mybatis.student WHERE `gmt_modified` > DATE_ADD(gmt_created, INTERVAL ? DAY)");
        db.sqlList().wantFirstPara().eqList(10);
    }
}
