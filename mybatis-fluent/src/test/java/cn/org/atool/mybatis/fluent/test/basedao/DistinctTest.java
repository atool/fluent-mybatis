package cn.org.atool.mybatis.fluent.test.basedao;

import cn.org.atool.mybatis.fluent.demo.dm.entity.UserEntityMap;
import cn.org.atool.mybatis.fluent.demo.dm.table.UserTableMap;
import cn.org.atool.mybatis.fluent.demo.entity.UserEntity;
import cn.org.atool.mybatis.fluent.demo.mapper.UserMapper;
import cn.org.atool.mybatis.fluent.demo.mapping.UserMP;
import cn.org.atool.mybatis.fluent.demo.query.UserEntityQuery;
import cn.org.atool.mybatis.fluent.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * TODO:类说明
 *
 * @author darui.wu
 * @create 2019/10/31 6:18 下午
 */
public class DistinctTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void test_distinct() {
        db.table(t_user).clean().insert(new UserTableMap(10).init()
                .user_name.values(DataGenerator.increase(index -> index > 5 ? "user2" : "user1"))
                .age.values(30)
        );
        UserEntityQuery query = new UserEntityQuery()
                .distinct(UserMP.Column.user_name)
                .and.age.eq(30);

        List<UserEntity> users = mapper.selectList(query);
        db.sqlList().wantFirstSql().eq("SELECT distinct user_name FROM t_user WHERE age = ?");
        want.list(users).eqDataMap(new UserEntityMap(2).userName.values("user1", "user2"));
    }
}