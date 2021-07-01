package cn.org.atool.fluent.mybatis.test.segment2;

import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * LimitTest
 *
 * @author wudarui
 */
public class LimitTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_limit_offset() throws Exception {
        StudentQuery query = new StudentQuery()
            .where.age().eq(10)
            .end()
            .limit(10, 20);

        mapper.listEntity(query);
        db.sqlList().wantFirstSql().end("WHERE `age` = ? LIMIT ?, ?");
    }

    @Test
    public void test_limit_maxSize() throws Exception {
        StudentQuery query = new StudentQuery()
            .where.age().eq(10)
            .end()
            .limit(20);

        mapper.listEntity(query);
        db.sqlList().wantFirstSql().end("WHERE `age` = ? LIMIT ?, ?");
    }
}
