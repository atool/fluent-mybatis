package cn.org.atool.fluent.mybatis.test.segment2;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
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
    void if_demo() {
        int age = 43;
        String name = "my name is fluent mybatis";
        String address = "";
        mapper.listEntity(new StudentQuery()
            .where.age().eq(age, arg -> arg > 30)
            .and.userName().eq(name, arg -> arg.contains("fluent"))
            .and.address().like(address, If::notBlank).end()
        );
        db.sqlList().wantFirstSql()
            .end("FROM student WHERE age = ? AND user_name = ?");
        db.sqlList().wantFirstPara().eqList(43, "my name is fluent mybatis");
    }

    @Test
    void if_demo2() {
        int age = 43;
        String name = "my name is fluent mybatis";
        String address = "";

        StudentQuery query = new StudentQuery();
        if (age > 30) {
            query.where.age().eq(age);
        }
        if (If.notBlank(name) && name.contains("fluent")) {
            query.where.userName().eq(name);
        }
        if (If.notBlank(address)) {
            query.where.address().like(address);
        }
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .end("FROM student WHERE age = ? AND user_name = ?");
        db.sqlList().wantFirstPara().eqList(43, "my name is fluent mybatis");
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
