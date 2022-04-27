package cn.org.atool.fluent.mybatis.test.segment;

import cn.org.atool.fluent.mybatis.base.free.FreeQuery;
import cn.org.atool.fluent.mybatis.generator.shared2.Ref;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static cn.org.atool.fluent.mybatis.segment.model.Aggregate.SUM;

@SuppressWarnings("unchecked")
public class SelectApplyTest extends BaseTest {
    @Autowired
    StudentMapper mapper;

    @Test
    void test_apply_field() {
        mapper.emptyQuery().select.applyField("id", "userName")
            .end()
            .to().listEntity();
        db.sqlList().wantFirstSql().eq("SELECT `id`, `user_name` FROM fluent_mybatis.student");
    }

    @Test
    void test_apply_not_column() {
        mapper.emptyQuery().select.exclude("id", "user_name")
            .to().listEntity();
        db.sqlList().wantFirstSql()
            .notContain("`id`")
            .notContain("`user_name`")
            .contains(", `gmt_created`, `gmt_modified`, `is_deleted`")
            .end("FROM fluent_mybatis.student");
    }

    @Test
    void test_apply_not_field() {
        mapper.emptyQuery().select.excludeField("id", "userName")
            .to().listEntity();
        db.sqlList().wantFirstSql()
            .notContain("`id`")
            .notContain("`user_name`")
            .contains(", `gmt_created`, `gmt_modified`, `is_deleted`")
            .end("FROM fluent_mybatis.student");
    }

    @Test
    void test_apply_not_fieldMapping() {
        mapper.emptyQuery().select.exclude(Ref.Field.Student.id, Ref.Field.Student.userName)
            .to().listEntity();
        db.sqlList().wantFirstSql()
            .notContain("`id`")
            .notContain("`user_name`")
            .contains(", `gmt_created`, `gmt_modified`, `is_deleted`")
            .end("FROM fluent_mybatis.student");
    }

    @Test
    void test_apply_not_IGetter() {
        mapper.emptyQuery().select.exclude(StudentEntity::getId, StudentEntity::getUserName)
            .to().listEntity();
        db.sqlList().wantFirstSql()
            .notContain("`id`")
            .notContain("`user_name`")
            .contains(", `gmt_created`, `gmt_modified`, `is_deleted`")
            .end("FROM fluent_mybatis.student");
    }

    @Test
    void test_freeQuery_apply_not() {
        mapper.listEntity(new FreeQuery("student")
            .select.excludeField("id", "userName")
        );
        db.sqlList().wantFirstSql()
            .notContain("`id`")
            .notContain("`user_name`")
            .contains(", `gmt_created`, `gmt_modified`, `is_deleted`")
            .end("FROM `student`");
    }

    @Test
    void test_apply_getter_as() {
        mapper.emptyQuery().select.applyAs(StudentEntity::getUserName, "un")
            .end()
            .to().listEntity();
        db.sqlList().wantFirstSql().eq("SELECT `user_name` AS un FROM fluent_mybatis.student");
    }

    @Test
    void test_apply_field_as() {
        mapper.emptyQuery().select.applyFieldAs("userName", "un")
            .end()
            .to().listEntity();
        db.sqlList().wantFirstSql().eq("SELECT `user_name` AS un FROM fluent_mybatis.student");
    }

    @Test
    void test_apply_func_field() {
        mapper.emptyQuery().select.applyFuncByField(SUM, "bonusPoints", "SUM_POINTS")
            .end()
            .to().listEntity();
        db.sqlList().wantFirstSql().eq("SELECT SUM(`bonus_points`) AS SUM_POINTS FROM fluent_mybatis.student");
    }

    @Test
    void test_apply_func_getter() {
        mapper.emptyQuery().select.applyFunc(SUM, StudentEntity::getBonusPoints, "SUM_POINTS")
            .end()
            .to().listEntity();
        db.sqlList().wantFirstSql().eq("SELECT SUM(`bonus_points`) AS SUM_POINTS FROM fluent_mybatis.student");
    }
}
