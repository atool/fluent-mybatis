package cn.org.atool.fluent.mybatis.free;

import cn.org.atool.fluent.mybatis.base.splice.FreeQuery;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FreeQueryTest extends BaseTest {
    /**
     * 随便哪个mapper
     */
    @Autowired
    private StudentMapper mapper;

    @Test
    void select() {
        FreeQuery query = new FreeQuery("dual")
            .select.apply("SEQ_xxx_ID.nextval").end();
        try {
            Object o = mapper.listObjs(query).get(0);
            want.fail("不可能执行到这里");
        } catch (Exception e) {
            db.sqlList().wantFirstSql().eq("SELECT SEQ_xxx_ID.nextval FROM dual");
        }
    }
}
