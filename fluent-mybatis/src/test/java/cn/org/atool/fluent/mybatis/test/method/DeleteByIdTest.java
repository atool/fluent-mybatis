package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.demo.generate.ITable;
import cn.org.atool.fluent.mybatis.demo.generate.datamap.TM;
import cn.org.atool.fluent.mybatis.demo.generate.entity.NoPrimaryEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapper.NoPrimaryMapper;
import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

public class DeleteByIdTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Autowired
    private NoPrimaryMapper noPrimaryMapper;

    @Test
    public void testDeleteById() {
        db.table(ITable.t_user).clean().insert(TM.user.createWithInit(2)
            .id.values(23L, 24L)
            .user_name.values("user1", "user2")
        );
        mapper.deleteById(24);
        db.sqlList().wantFirstSql()
            .eq("DELETE FROM t_user WHERE id=?", StringMode.SameAsSpace);
        db.table(ITable.t_user).query().eqDataMap(TM.user.create(1)
            .id.values(23L)
            .user_name.values("user1")
        );
    }

    @Test
    public void test_selectById_noPrimary() throws Exception {
        db.table(t_no_primary).clean().insert(TM.no_primary.createWithInit(3)
            .column_1.values(1, 2, 3)
            .column_2.values("c1", "c2", "c3")
        );
        int result = noPrimaryMapper.deleteById(3L);
        db.sqlList().wantFirstSql().where().eq("1!=1");
        want.number(result).eq(0);
    }
}
