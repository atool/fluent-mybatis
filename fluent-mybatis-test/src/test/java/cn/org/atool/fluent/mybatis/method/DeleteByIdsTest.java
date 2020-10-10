package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.generate.ITable;
import cn.org.atool.fluent.mybatis.generate.DM;
import cn.org.atool.fluent.mybatis.generate.entity.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

import java.util.Arrays;

public class DeleteByIdsTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void testDeleteByIds() {
        db.table(ITable.t_user).clean().insert(DM.user.initTable(5)
            .id.values(23L, 24L, 25L, 26L, 27L)
            .userName.values("user1", "user2")
        );
        mapper.deleteByIds(Arrays.asList(24, 27, 25));
        db.sqlList().wantFirstSql()
            .eq("DELETE FROM t_user WHERE id IN (?, ?, ?)", StringMode.SameAsSpace);
        db.table(ITable.t_user).query().eqDataMap(DM.user.table(2)
            .id.values(23L, 26L)
            .userName.values("user1", "user2")
        );
    }
}
