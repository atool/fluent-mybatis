package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.UserUpdate;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class UpdateApplyTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    void is() {
        mapper.updateBy(new UserUpdate()
            .set.age().is(34).end()
            .where.id().eq(2).end()
        );
        db.sqlList().wantFirstSql()
            .eq("UPDATE t_user SET gmt_modified = now(), age = ? WHERE id = ?");
    }

    @Test
    void isNull() {
        mapper.updateBy(new UserUpdate()
            .set
            .age().is(34)
            .userName().isNull()
            .end()
            .where.id().eq(2).end()
        );
        db.sqlList().wantFirstSql()
            .end("SET gmt_modified = now(), age = ?, user_name = ? WHERE id = ?");
    }

    @Test
    void is_If() {
        mapper.updateBy(new UserUpdate()
            .set
            .age().is_If(false, 34)
            .userName().is_If(true, null)
            .end()
            .where.id().eq(2).end()
        );
        db.sqlList().wantFirstSql()
            .end("SET gmt_modified = now(), user_name = ? WHERE id = ?");
    }

    @Test
    void is_IfNotNull() {
        mapper.updateBy(new UserUpdate()
            .set
            .age().is_IfNotNull(34)
            .userName().is_IfNotNull(null)
            .end()
            .where.id().eq(2).end()
        );
        db.sqlList().wantFirstSql()
            .end("SET gmt_modified = now(), age = ? WHERE id = ?");
    }

    @Test
    void is_IfNotBlank() {
        mapper.updateBy(new UserUpdate()
            .set
            .version().is_IfNotBlank("19")
            .userName().is_IfNotBlank(null)
            .userName().is_IfNotBlank("  ")
            .end()
            .where.id().eq(2).end()
        );
        db.sqlList().wantFirstSql()
            .end("SET gmt_modified = now(), version = ? WHERE id = ?");
    }

    @Test
    void apply() {
        mapper.updateBy(new UserUpdate()
            .set
            .userName().apply("concat('user_name', '_abc')")
            .end()
            .where.id().eq(2).end()
        );
        db.sqlList().wantFirstSql()
            .end("SET gmt_modified = now(), user_name = concat('user_name', '_abc') WHERE id = ?");
    }

    @Test
    void apply_If() {
        mapper.updateBy(new UserUpdate()
            .set
            .userName().apply_If(false, "concat('user_name', '_abc')")
            .age().apply_If(true, "age+1")
            .end()
            .where.id().eq(2).end()
        );
        db.sqlList().wantFirstSql()
            .end("SET gmt_modified = now(), age = age+1 WHERE id = ?");
    }
}