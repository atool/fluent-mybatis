package cn.org.atool.fluent.mybatis.test.segment2;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentUpdate;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UpdateApplyTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    void is() {
        mapper.updateBy(new StudentUpdate()
            .update.age().is(34).end()
            .where.id().eq(2).end()
        );
        db.sqlList().wantFirstSql()
            .eq("UPDATE student SET gmt_modified = now(), age = ? WHERE id = ?");
    }

    @Test
    void isNull() {
        mapper.updateBy(new StudentUpdate()
            .update.age().is(34)
            .set.userName().isNull().end()
            .where.id().eq(2).end()
        );
        db.sqlList().wantFirstSql()
            .end("SET gmt_modified = now(), age = ?, user_name = ? WHERE id = ?");
    }

    @Test
    void is_If() {
        mapper.updateBy(new StudentUpdate()
            .update.age().is(34, If::everFalse)
            .set.userName().is(null, If::everTrue).end()
            .where.id().eq(2).end()
        );
        db.sqlList().wantFirstSql()
            .end("SET gmt_modified = now(), user_name = ? WHERE id = ?");
    }

    @Test
    void is_IfNotNull() {
        mapper.updateBy(new StudentUpdate()
            .update.age().is(34, If::notNull)
            .set.userName().is(null, If::notNull).end()
            .where.id().eq(2).end()
        );
        db.sqlList().wantFirstSql()
            .end("SET gmt_modified = now(), age = ? WHERE id = ?");
    }

    @Test
    void is_IfNotBlank() {
        mapper.updateBy(new StudentUpdate()
            .update.version().is("19", If::notBlank)
            .set.userName().is(null, If::notBlank)
            .set.userName().is("  ", If::notBlank).end()
            .where.id().eq(2).end()
        );
        db.sqlList().wantFirstSql()
            .end("SET gmt_modified = now(), version = ? WHERE id = ?");
    }

    @Test
    void apply() {
        mapper.updateBy(new StudentUpdate()
            .update.userName().applyFunc("concat('user_name', '_abc')").end()
            .where.id().eq(2).end()
        );
        db.sqlList().wantFirstSql()
            .end("SET gmt_modified = now(), user_name = concat('user_name', '_abc') WHERE id = ?");
    }

    @Test
    void apply_If() {
        mapper.updateBy(new StudentUpdate()
            .update.userName().applyFunc(false, "concat('user_name', '_abc')")
            .set.age().applyFunc(true, "age+1").end()
            .where.id().eq(2).end()
        );
        db.sqlList().wantFirstSql()
            .end("SET gmt_modified = now(), age = age+1 WHERE id = ?");
    }
}