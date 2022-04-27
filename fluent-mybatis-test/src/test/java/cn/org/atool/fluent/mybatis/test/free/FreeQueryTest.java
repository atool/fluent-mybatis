package cn.org.atool.fluent.mybatis.test.free;

import cn.org.atool.fluent.mybatis.base.free.FreeQuery;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.LIKE;

public class FreeQueryTest extends BaseTest {
    /**
     * 随便哪个mapper
     */
    @Autowired
    private StudentMapper mapper;

    @SuppressWarnings("all")
    @Test
    void select() {
        FreeQuery query = new FreeQuery("dual")
            .select.apply("SEQ_xxx_ID.nextval").end();
        try {
            mapper.listObjs(query).get(0);
            want.fail("不可能执行到这里");
        } catch (Exception e) {
            db.sqlList().wantFirstSql().eq("SELECT SEQ_xxx_ID.nextval FROM `dual`");
        }
    }

    @Test
    void test2() {
        FreeQuery factQuery = new FreeQuery("a")
            .select.apply("id").end()
            .where
            .and(q -> q.where.apply("name", LIKE, "1-%")
                .or.apply("age", LIKE, "2-%")
                .end()
            ).end();
        try {
            mapper.listObjs(factQuery);
        } catch (Exception ignore) {
        }
        db.sqlList().wantFirstSql().eq("" +
            "SELECT `id` FROM `a` WHERE (`name` LIKE ? OR `age` LIKE ?)");
        db.sqlList().wantFirstPara().eq(new String[]{"1-%", "2-%"});
    }
}
