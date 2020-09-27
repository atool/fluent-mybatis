package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.generate.datamap.EM;
import cn.org.atool.fluent.mybatis.generate.datamap.TM;
import cn.org.atool.fluent.mybatis.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author darui.wu
 * @create 2019/10/29 9:33 下午
 */
public class SelectListTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void test_selectList() throws Exception {
        db.table(t_user).clean()
            .insert(TM.user.createWithInit(4)
                .id.values(23, 24, 25, 26)
                .user_name.values("u1", "u2", "u3", "u2")
            );
        UserQuery query = new UserQuery()
            .where.id().eq(24L).end();
        List<UserEntity> users = mapper.listEntity(query);
        db.sqlList().wantFirstSql().start("SELECT").end("FROM t_user WHERE id = ?");
        want.list(users).eqDataMap(EM.user.create(1)
            .userName.values("u2"));
    }

    @Test
    public void test_selectList_hasMultiple() throws Exception {
        db.table(t_user).clean()
            .insert(TM.user.createWithInit(4)
                .id.values(23, 24, 25, 26)
                .user_name.values("u1", "u2", "u3", "u2")
            );
        UserQuery query = new UserQuery()
            .where.userName().eq("u2").end();
        List<UserEntity> users = mapper.listEntity(query);
        db.sqlList().wantFirstSql().start("SELECT").end("FROM t_user WHERE user_name = ?");
        want.list(users).eqDataMap(EM.user.create(2)
            .userName.values("u2"));
    }

    @Test
    public void test_selectList_limit() throws Exception {
        db.table(t_user).clean()
            .insert(TM.user.createWithInit(4)
                .id.values(23, 24, 25, 26)
                .user_name.values("u1", "u2", "u3", "u2")
            );
        UserQuery query = new UserQuery()
            .where.userName().eq("u2").end()
            .limit(2);
        List<UserEntity> users = mapper.listEntity(query);
        want.list(users).eqDataMap(EM.user.create(2)
            .userName.values("u2"));
        db.sqlList().wantFirstSql().start("SELECT").end("FROM t_user WHERE user_name = ? LIMIT ?, ?");
    }

    @Test
    public void test_selectList_limit2() throws Exception {
        db.table(t_user).clean()
            .insert(TM.user.createWithInit(4)
                .id.values(23, 24, 25, 26)
                .user_name.values("u1", "u2", "u3", "u2")
            );
        UserQuery query = new UserQuery()
            .where.userName().eq("u2").end()
            .limit(2, 3);
        List<UserEntity> users = mapper.listEntity(query);
        db.sqlList().wantFirstSql().start("SELECT").end("FROM t_user WHERE user_name = ? LIMIT ?, ?");
        db.sqlList().wantFirstPara().eq(new Object[]{"u2", 2, 3});
    }
}