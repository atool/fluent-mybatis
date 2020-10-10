package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * AndObjectTest_Exists
 *
 * @author darui.wu
 * @create 2020/6/18 5:39 下午
 */
public class WhereObjectTest_Exists extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    void test_exists() {
        UserQuery query = new UserQuery()
            .where.isDeleted().eq(true)
            .and.exists("select 1 from t_user where age=?", 34)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT COUNT(*) FROM t_user WHERE is_deleted = ? AND EXISTS (select 1 from t_user where age=?)");
    }

    @Test
    void test_not_exists() {
        UserQuery query = new UserQuery()
            .where.isDeleted().eq(true)
            .and.notExists("select 1 from t_user where age=?", 34)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT COUNT(*) FROM t_user " +
                "WHERE is_deleted = ? " +
                "AND NOT EXISTS (select 1 from t_user where age=?)");
    }
}
