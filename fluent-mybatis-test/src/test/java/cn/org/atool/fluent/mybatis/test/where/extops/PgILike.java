package cn.org.atool.fluent.mybatis.test.where.extops;

import cn.org.atool.fluent.mybatis.base.free.FreeQuery;
import cn.org.atool.fluent.mybatis.base.model.op.SqlOps;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PgILike extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    void pgLike() {
        FreeQuery query = new FreeQuery("a")
            .select.apply("id", "name").end()
            .where.apply("name", SqlOps.ILike, "%name%")
            .end();
        try {
            mapper.listObjs(query);
        } catch (Exception ignored) {
        }
        db.sqlList().wantFirstSql().eq("SELECT `id`, `name` FROM `a` WHERE `name` ILIKE ?");
        db.sqlList().wantFirstPara().eqList("%name%");
    }
}
