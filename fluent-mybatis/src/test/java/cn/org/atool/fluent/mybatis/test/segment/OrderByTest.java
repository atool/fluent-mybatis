package cn.org.atool.fluent.mybatis.test.segment;

import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.query.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * OrderByTest
 *
 * @author darui.wu
 * @create 2020/6/20 4:49 下午
 */
public class OrderByTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void test_order() throws Exception {
        UserQuery query = new UserQuery()
            .selectId()
            .and.id.eq(24L)
            .orderBy(by -> by.id.asc().age.desc());
        mapper.selectList(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id FROM t_user WHERE id = ? ORDER BY id ASC, age DESC");
    }
}