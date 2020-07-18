package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.demo.generate.datamap.TM;
import cn.org.atool.fluent.mybatis.demo.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * LimitTest
 *
 * @author wudarui
 */
public class LimitTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void test_limit_offset() throws Exception {
        UserQuery query = new UserQuery()
            .where.age().eq(10)
            .end()
            .limit(10, 20);

        mapper.listEntity(query);
        db.sqlList().wantFirstSql().end("WHERE age = ? LIMIT ?, ?");
    }

    @Test
    public void test_limit_maxSize() throws Exception {
        UserQuery query = new UserQuery()
            .where.age().eq(10)
            .end()
            .limit(20);

        mapper.listEntity(query);
        db.sqlList().wantFirstSql().end("WHERE age = ? LIMIT ?, ?");
    }
}
