package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.generate.datamap.ITable;
import cn.org.atool.fluent.mybatis.generate.datamap.TM;
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
        db.table(ITable.t_user).clean().insert(TM.user.createWithInit(5)
            .id.values(23L, 24L, 25L, 26L, 27L)
            .user_name.values("user1", "user2")
        );
        mapper.deleteByIds(Arrays.asList(24, 27, 25));
        db.sqlList().wantFirstSql()
            .eq("DELETE FROM t_user WHERE id IN (?, ?, ?)", StringMode.SameAsSpace);
        db.table(ITable.t_user).query().eqDataMap(TM.user.create(2)
            .id.values(23L, 26L)
            .user_name.values("user1", "user2")
        );
    }
}