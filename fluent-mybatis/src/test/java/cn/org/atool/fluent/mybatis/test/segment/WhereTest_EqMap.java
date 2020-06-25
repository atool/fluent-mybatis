package cn.org.atool.fluent.mybatis.test.segment;

import cn.org.atool.fluent.mybatis.demo.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.demo.generate.helper.UserMapping;
import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * WhereTest_EqMap
 *
 * @author darui.wu
 * @create 2020/6/21 3:40 下午
 */
public class WhereTest_EqMap extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void test_eq_map() throws Exception {
        UserQuery query = new UserQuery()
            .selectId()
            .where.eqByNotNull(new HashMap<String, Object>() {
                {
                    this.put(UserMapping.userName.column, "user1");
                }
            }).end();
        mapper.selectList(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id FROM t_user WHERE user_name = ?");
    }

    @Test
    public void test_eq_entity() throws Exception {
        UserQuery query = new UserQuery()
            .selectId()
            .where
            .eqByNotNull(new UserEntity().setUserName("u2"))
            .end();
        mapper.selectList(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id FROM t_user WHERE user_name = ?");
    }
}
