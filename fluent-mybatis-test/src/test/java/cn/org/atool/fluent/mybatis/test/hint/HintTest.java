package cn.org.atool.fluent.mybatis.test.hint;

import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentUpdate;
import cn.org.atool.fluent.mybatis.segment.model.HintType;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;

import java.sql.SQLSyntaxErrorException;

public class HintTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    void beforeSelect() {
        StudentQuery query = StudentQuery.emptyQuery()
            .hint("/*+DBP: $ROUTE={GROUP_ID(分片位),TABLE_NAME(物理表名)}*/")
            .where.userName().eq("test").end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql().start("" +
            "/*+DBP: $ROUTE={GROUP_ID(分片位),TABLE_NAME(物理表名)}*/ SELECT `id`");
    }

    @Test
    void afterSelect() {
        StudentQuery query = StudentQuery.emptyQuery()
            .hint(HintType.After_CrudKey, "/** hint **/")
            .where.userName().eq("test").end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql().start("SELECT /** hint **/ `id`");
    }

    @Test
    void beforeSelectTable() {
        StudentQuery query = StudentQuery.emptyQuery()
            .hint(HintType.Before_Table, "/** hint **/")
            .where.userName().eq("test").end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .start("SELECT `id`").contains("FROM /** hint **/ fluent_mybatis.student");
    }

    @Test
    void afterSelectTable() {
        StudentQuery query = StudentQuery.emptyQuery()
            .hint(HintType.After_Table, "/** hint **/")
            .where.userName().eq("test").end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .start("SELECT `id`, ")
            .contains(", `gmt_created`, `gmt_modified`, `is_deleted` FROM fluent_mybatis.student /** hint **/");
    }

    @Test
    void before_SelectCount() {
        StudentQuery query = StudentQuery.emptyQuery()
            .hint("/** hint **/")
            .where.userName().eq("test").end();
        mapper.count(query);
        db.sqlList().wantFirstSql().start("/** hint **/ SELECT COUNT(*)");
    }

    @Test
    void afterSelect_count() {
        StudentQuery query = StudentQuery.emptyQuery()
            .hint(HintType.After_CrudKey, "/** hint **/")
            .where.userName().eq("test").end();
        mapper.count(query);
        db.sqlList().wantFirstSql().start("SELECT /** hint **/ COUNT(*)");
    }

    @Test
    void beforeTable_SelectCount() {
        StudentQuery query = StudentQuery.emptyQuery()
            .hint(HintType.Before_Table, "/** hint **/")
            .where.userName().eq("test").end();
        mapper.count(query);
        db.sqlList().wantFirstSql().start("SELECT COUNT(*) FROM /** hint **/ fluent_mybatis.student");
    }

    @Test
    void afterTable_SelectCount() {
        StudentQuery query = StudentQuery.emptyQuery()
            .hint(HintType.After_Table, "force index(create_time)")
            .where.userName().eq("test").end();
        want.exception(() -> mapper.count(query),
            SQLSyntaxErrorException.class, BadSqlGrammarException.class);
        db.sqlList().wantFirstSql().start("" +
            "SELECT COUNT(*) FROM fluent_mybatis.student force index(create_time) WHERE");
    }

    @Test
    void beforeUpdate() {
        StudentUpdate update = StudentUpdate.emptyUpdater()
            .set.userName().is("test").end()
            .where.id().eq(3L).end()
            .hint("/** hint **/");
        mapper.updateBy(update);
        db.sqlList().wantFirstSql().start("/** hint **/ UPDATE fluent_mybatis.student");
    }

    @Test
    void afterUpdate() {
        StudentUpdate update = StudentUpdate.emptyUpdater()
            .set.userName().is("test").end()
            .where.id().eq(3L).end()
            .hint(HintType.After_CrudKey, "/** hint **/");
        mapper.updateBy(update);
        db.sqlList().wantFirstSql().start("UPDATE /** hint **/ fluent_mybatis.student");
    }

    @Test
    void beforeTableUpdate() {
        StudentUpdate update = StudentUpdate.emptyUpdater()
            .set.userName().is("test").end()
            .where.id().eq(3L).end()
            .hint(HintType.Before_Table, "/** hint **/");
        mapper.updateBy(update);
        db.sqlList().wantFirstSql().start("UPDATE /** hint **/ fluent_mybatis.student");
    }

    @Test
    void afterTableUpdate() {
        StudentUpdate update = StudentUpdate.emptyUpdater()
            .set.userName().is("test").end()
            .where.id().eq(3L).end()
            .hint(HintType.After_Table, "/** hint **/");
        mapper.updateBy(update);
        db.sqlList().wantFirstSql().start("UPDATE fluent_mybatis.student /** hint **/");
    }

    @Test
    void beforeDelete() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.id().eq(3L).end()
            .hint("/** hint **/");
        mapper.delete(query);
        db.sqlList().wantFirstSql().start("/** hint **/ DELETE FROM fluent_mybatis.student");
    }

    @Test
    void afterDelete() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.id().eq(3L).end()
            .hint(HintType.After_CrudKey, "/** hint **/");
        mapper.delete(query);
        db.sqlList().wantFirstSql().start("DELETE /** hint **/ FROM fluent_mybatis.student");
    }

    @Test
    void beforeTableDelete() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.id().eq(3L).end()
            .hint(HintType.Before_Table, "/** hint **/");
        mapper.delete(query);
        db.sqlList().wantFirstSql().start("DELETE FROM /** hint **/ fluent_mybatis.student");
    }

    @Test
    void afterTableDelete() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.id().eq(3L).end()
            .hint(HintType.After_Table, "/** hint **/");
        mapper.delete(query);
        db.sqlList().wantFirstSql().start("DELETE FROM fluent_mybatis.student /** hint **/");
    }
}
