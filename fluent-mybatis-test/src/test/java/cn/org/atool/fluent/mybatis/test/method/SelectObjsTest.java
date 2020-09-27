package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.generate.datamap.TM;
import cn.org.atool.fluent.mybatis.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static cn.org.atool.fluent.mybatis.generate.helper.UserMapping.userName;

/**
 * @author darui.wu
 * @create 2019/10/29 9:33 下午
 */
public class SelectObjsTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void test_selectObjs() throws Exception {
        db.table(t_user).clean()
            .insert(TM.user.createWithInit(4)
                .id.values(23, 24, 25, 26)
                .user_name.values("u1", "u2", "u3", "u2")
            );
        UserQuery query = new UserQuery()
            .select.apply(userName)
            .end()
            .where.id().eq(24L)
            .end();
        List<String> users = mapper.listObjs(query);
        db.sqlList().wantFirstSql().start("SELECT").end("FROM t_user WHERE id = ?");
        want.list(users).eqReflect(new String[]{"u2"});
    }

    @Test
    public void test_selectObjs_hasMultiple() throws Exception {
        db.table(t_user).clean()
            .insert(TM.user.createWithInit(4)
                .id.values(23, 24, 25, 26)
                .user_name.values("u1", "u2", "u3", "u2")
            );
        UserQuery query = new UserQuery()
            .select.apply(userName)
            .end()
            .where.userName().eq("u2")
            .end();
        List<String> users = mapper.listObjs(query);
        db.sqlList().wantFirstSql().start("SELECT").end("FROM t_user WHERE user_name = ?");
        want.list(users).eqReflect(new String[]{"u2", "u2"});
    }
}