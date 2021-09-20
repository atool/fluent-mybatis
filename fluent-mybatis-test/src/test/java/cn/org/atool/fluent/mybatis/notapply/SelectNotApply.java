package cn.org.atool.fluent.mybatis.notapply;

import cn.org.atool.fluent.mybatis.base.free.FreeQuery;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.refs.FieldRef;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SelectNotApply extends BaseTest {
    @Autowired
    StudentMapper mapper;

    @Test
    void test_apply_not_string() {
        mapper.emptyQuery().select.applyExclude("id", "user_name")
            .to().listEntity();
        db.sqlList().wantFirstSql()
            .notContain("`id`")
            .notContain("`user_name`")
            .contains("`gmt_created`, `gmt_modified`, `is_deleted`,")
            .end("FROM fluent_mybatis.student");
    }

    @Test
    void test_apply_not_fieldMapping() {
        mapper.emptyQuery().select.applyExclude(FieldRef.Student.id, FieldRef.Student.userName)
            .to().listEntity();
        db.sqlList().wantFirstSql()
            .notContain("`id`")
            .notContain("`user_name`")
            .contains("`gmt_created`, `gmt_modified`, `is_deleted`,")
            .end("FROM fluent_mybatis.student");
    }

    @Test
    void test_freeQuery_apply_not() {
        mapper.listEntity(new FreeQuery("student")
            .select.applyExclude("id", "user_name")
        );
        db.sqlList().wantFirstSql()
            .notContain("`id`")
            .notContain("`user_name`")
            .contains("`gmt_created`, `gmt_modified`, `is_deleted`,")
            .end("FROM student");
    }
}
