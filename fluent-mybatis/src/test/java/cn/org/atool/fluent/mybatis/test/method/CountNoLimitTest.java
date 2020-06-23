package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.demo.generate.datamap.TM;
import cn.org.atool.fluent.mybatis.demo.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.query.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author darui.wu
 * @create 2019/10/29 9:33 下午
 */
public class CountNoLimitTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void test_count_no_limit() throws Exception {
        UserQuery query = new UserQuery()
            .where.id().eq(24L).end()
            .orderBy.userName().end()
            .limit(10);

        mapper.selectCount(query);
        db.sqlList().wantFirstSql()
            .start("SELECT COUNT( * )")
            .end("FROM t_user WHERE id = ? ORDER BY user_name LIMIT ?, ?");

        mapper.countNoLimit(query);
        db.sqlList().wantSql(1).end("WHERE id = ?");
    }

    @Test
    public void test_count_and_list() throws Exception {
        db.table(t_user).clean()
            .insert(TM.user.createWithInit(100)
                .age.values(10)
                .user_name.values("u1", "u2", "u3", "u2")
            );
        UserQuery query = new UserQuery()
            .where
            .age().eq(10)
            .end()
            .orderBy.userName()
            .end()
            .limit(10, 20);
        int count = mapper.countNoLimit(query);
        db.sqlList().wantFirstSql()
            .start("SELECT COUNT( * )")
            .end("FROM t_user WHERE age = ?");
        want.number(count).eq(100);

        List<UserEntity> list = mapper.selectList(query);
        db.sqlList().wantSql(1)
            .start("SELECT id,")
            .end("WHERE age = ? ORDER BY user_name LIMIT ?, ?");
        want.list(list).sizeEq(20);
    }
}