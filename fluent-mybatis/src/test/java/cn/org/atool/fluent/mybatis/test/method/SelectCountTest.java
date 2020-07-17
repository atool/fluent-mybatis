package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.demo.generate.datamap.TM;
import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;

import static cn.org.atool.fluent.mybatis.method.model.XmlConstant.Wrapper_Exists;

/**
 * @author darui.wu
 * @create 2019/10/29 9:33 下午
 */
public class SelectCountTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void test_selectCount_null() throws Exception {
        want.exception(() -> mapper.count(null), MyBatisSystemException.class)
            .contains("Error evaluating expression").contains(Wrapper_Exists);
    }

    @Test
    public void test_selectCount() throws Exception {
        db.table(t_user).clean()
            .insert(TM.user.createWithInit(4)
                .id.values(23, 24, 25, 26)
                .user_name.values("u1", "u2", "u3", "u2")
            );
        UserQuery query = new UserQuery()
            .where.id().eq(24L).end();
        int count = mapper.count(query);
        db.sqlList().wantFirstSql().start("SELECT COUNT( * )").end("FROM t_user WHERE id = ?");
        want.number(count).eq(1);
    }

    @Test
    public void test_selectCount_hasMultiple() throws Exception {
        db.table(t_user).clean()
            .insert(TM.user.createWithInit(4)
                .id.values(23, 24, 25, 26)
                .user_name.values("u1", "u2", "u3", "u2")
            );
        UserQuery query = new UserQuery()
            .select.apply("id").end()
            .where.userName().eq("u2").end();
        int count = mapper.count(query);
        db.sqlList().wantFirstSql().start("SELECT COUNT( id )").end("FROM t_user WHERE user_name = ?");
        want.number(count).eq(2);
    }

    @Test
    public void test_selectCount_limit() throws Exception {
        db.table(t_user).clean()
            .insert(TM.user.createWithInit(4)
                .id.values(23, 24, 25, 26)
                .user_name.values("u1", "u2", "u3", "u2")
            );
        UserQuery query = new UserQuery()
            .select.apply("id")
            .end()
            .where.userName().eq("u2")
            .end()
            .limit.by(2);
        int count = mapper.count(query);
        db.sqlList().wantFirstSql().start("SELECT COUNT( id )").end("FROM t_user WHERE user_name = ? LIMIT ?, ?");
        want.number(count).eq(2);
    }
}