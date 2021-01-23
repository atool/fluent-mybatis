package cn.org.atool.fluent.mybatis.test.hint;

import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentUpdate;
import cn.org.atool.fluent.mybatis.segment.model.HintType;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class HintTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    void beforeSelect() {
        StudentQuery query = new StudentQuery()
            .hint("/** hint **/")
            .where.userName().eq("test").end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql().start("/** hint **/ SELECT id");
    }

    @Test
    void afterSelect() {
        StudentQuery query = new StudentQuery()
            .hint(HintType.After_CrudKey, "/** hint **/")
            .where.userName().eq("test").end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql().start("SELECT /** hint **/ id");
    }

    @Test
    void beforeSelectTable() {
        StudentQuery query = new StudentQuery()
            .hint(HintType.Before_Table, "/** hint **/")
            .where.userName().eq("test").end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql().start("SELECT id").contains("FROM /** hint **/ student");
    }

    @Test
    void afterSelectTable() {
        StudentQuery query = new StudentQuery()
            .hint(HintType.After_Table, "/** hint **/")
            .where.userName().eq("test").end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql().start("SELECT id").contains("FROM student /** hint **/");
    }

    @Test
    void before_SelectCount() {
        StudentQuery query = new StudentQuery()
            .hint("/** hint **/")
            .where.userName().eq("test").end();
        mapper.count(query);
        db.sqlList().wantFirstSql().start("/** hint **/ SELECT COUNT(*)");
    }

    @Test
    void afterSelect_count() {
        StudentQuery query = new StudentQuery()
            .hint(HintType.After_CrudKey, "/** hint **/")
            .where.userName().eq("test").end();
        mapper.count(query);
        db.sqlList().wantFirstSql().start("SELECT /** hint **/ COUNT(*)");
    }

    @Test
    void beforeTable_SelectCount() {
        StudentQuery query = new StudentQuery()
            .hint(HintType.Before_Table, "/** hint **/")
            .where.userName().eq("test").end();
        mapper.count(query);
        db.sqlList().wantFirstSql().start("SELECT COUNT(*) FROM /** hint **/ student");
    }

    @Test
    void afterTable_SelectCount() {
        StudentQuery query = new StudentQuery()
            .hint(HintType.After_Table, "/** hint **/")
            .where.userName().eq("test").end();
        mapper.count(query);
        db.sqlList().wantFirstSql().start("SELECT COUNT(*) FROM student /** hint **/");
    }

    @Test
    void beforeUpdate() {
        StudentUpdate update = new StudentUpdate()
            .update.userName().is("test").end()
            .where.id().eq(3L).end()
            .hint("/** hint **/");
        mapper.updateBy(update);
        db.sqlList().wantFirstSql().start("/** hint **/ UPDATE student");
    }

    @Test
    void afterUpdate() {
        StudentUpdate update = new StudentUpdate()
            .update.userName().is("test").end()
            .where.id().eq(3L).end()
            .hint(HintType.After_CrudKey, "/** hint **/");
        mapper.updateBy(update);
        db.sqlList().wantFirstSql().start("UPDATE /** hint **/ student");
    }

    @Test
    void beforeTableUpdate() {
        StudentUpdate update = new StudentUpdate()
            .update.userName().is("test").end()
            .where.id().eq(3L).end()
            .hint(HintType.Before_Table, "/** hint **/");
        mapper.updateBy(update);
        db.sqlList().wantFirstSql().start("UPDATE /** hint **/ student");
    }

    @Test
    void afterTableUpdate() {
        StudentUpdate update = new StudentUpdate()
            .update.userName().is("test").end()
            .where.id().eq(3L).end()
            .hint(HintType.After_Table, "/** hint **/");
        mapper.updateBy(update);
        db.sqlList().wantFirstSql().start("UPDATE student /** hint **/");
    }

    @Test
    void beforeDelete() {
        StudentQuery query = new StudentQuery()
            .where.id().eq(3L).end()
            .hint("/** hint **/");
        mapper.delete(query);
        db.sqlList().wantFirstSql().start("/** hint **/ DELETE FROM student");
    }

    @Test
    void afterDelete() {
        StudentQuery query = new StudentQuery()
            .where.id().eq(3L).end()
            .hint(HintType.After_CrudKey, "/** hint **/");
        mapper.delete(query);
        db.sqlList().wantFirstSql().start("DELETE /** hint **/ FROM student");
    }

    @Test
    void beforeTableDelete() {
        StudentQuery query = new StudentQuery()
            .where.id().eq(3L).end()
            .hint(HintType.Before_Table, "/** hint **/");
        mapper.delete(query);
        db.sqlList().wantFirstSql().start("DELETE FROM /** hint **/ student");
    }

    @Test
    void afterTableDelete() {
        StudentQuery query = new StudentQuery()
            .where.id().eq(3L).end()
            .hint(HintType.After_Table, "/** hint **/");
        mapper.delete(query);
        db.sqlList().wantFirstSql().start("DELETE FROM student /** hint **/");
    }
}
