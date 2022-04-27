package cn.org.atool.fluent.mybatis.test.segment;

import cn.org.atool.fluent.mybatis.base.free.FreeQuery;
import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;

public class WhereApplyTest2 extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    void apply() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where.userName().applyFunc(SqlOp.EQ, "user_name+1").end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `user_name` = user_name+1");
    }

    @Test
    void apply_any() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where.age().applyFunc(SqlOp.GT, "any(?)",
                StudentQuery.emptyQuery().select.age().end()
                    .where.id().lt(30L).end())
            .and.id().gt(50L)
            .end()
        );
        db.sqlList().wantFirstSql().end("" +
            "WHERE `age` > any(SELECT `age` FROM fluent_mybatis.student WHERE `id` < ?) " +
            "AND `id` > ?");
    }

    @Test
    void orderBy() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .orderBy.gmtCreated().desc().end()
            .limit(10)
        ).sort(Comparator.comparing(StudentEntity::getGmtCreated));

        db.sqlList().wantFirstSql().end("" +
            "FROM fluent_mybatis.student ORDER BY `gmt_created` DESC LIMIT ?, ?");

        mapper.listEntity(new FreeQuery(
            StudentQuery.emptyQuery()
                .selectAll()
                .orderBy.gmtCreated().desc().end()
                .limit(10), "a1")
            .select("*").orderBy.asc("gmt_created").end());
        db.sqlList().wantSql(1)
            .start("SELECT * FROM (SELECT `id`,")
            .end("FROM fluent_mybatis.student ORDER BY `gmt_created` DESC LIMIT ?, ?) a1 ORDER BY a1.`gmt_created` ASC");
    }

    @Test
    void apply_date() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where.gmtModified().applyFunc(SqlOp.GT, "DATE_ADD(gmt_created, INTERVAL ? DAY)", 10)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("FROM fluent_mybatis.student WHERE `gmt_modified` > DATE_ADD(gmt_created, INTERVAL ? DAY)");
        db.sqlList().wantFirstPara().eqList(10);
    }
}
