package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @author darui.wu
 * @create 2019/10/29 9:33 下午
 */
public class SelectMapsTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void test_selectMaps() throws Exception {
        db.table(ATM.Table.user).clean()
            .insert(ATM.DataMap.user.initTable(4)
                .id.values(23, 24, 25, 26)
                .userName.values("u1", "u2", "u3", "u2")
            );
        UserQuery query = new UserQuery()
            .where.id().eq(24L).end();
        List<Map<String, Object>> users = mapper.listMaps(query);
        db.sqlList().wantFirstSql().start("SELECT").end("FROM t_user WHERE id = ?");
        want.list(users).eqDataMap(ATM.DataMap.user.table(1)
            .userName.values("u2"));
    }

    @Test
    public void test_selectMaps_hasMultiple() throws Exception {
        db.table(ATM.Table.user).clean()
            .insert(ATM.DataMap.user.initTable(4)
                .id.values(23, 24, 25, 26)
                .userName.values("u1", "u2", "u3", "u2")
            );
        UserQuery query = new UserQuery()
            .where.userName().eq("u2").end();
        List<Map<String, Object>> users = mapper.listMaps(query);
        db.sqlList().wantFirstSql().start("SELECT").end("FROM t_user WHERE user_name = ?");
        want.list(users).eqDataMap(ATM.DataMap.user.table(2)
            .userName.values("u2"));
    }
}
