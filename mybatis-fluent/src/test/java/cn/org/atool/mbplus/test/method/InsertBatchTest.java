package cn.org.atool.mbplus.test.method;

import cn.org.atool.mbplus.demo.dm.table.UserTableMap;
import cn.org.atool.mbplus.demo.entity.UserEntity;
import cn.org.atool.mbplus.demo.mapper.UserMapper;
import cn.org.atool.mbplus.test.BaseTest;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class InsertBatchTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void testInsertBatch() {
        db.table(t_user).clean();
        mapper.insertBatch(Lists.newArrayList(
                new UserEntity().setUserName("name1").setAge(23),
                new UserEntity().setUserName("name2").setAge(24)));
        db.table(t_user).count().eq(2L);
        db.table(t_user).query().print().eqDataMap(new UserTableMap(2)
                .age.values(23, 24)
                .user_name.values("name1", "name2"));
    }

    @Test
    public void testInsertBatchWithId() {
        db.table(t_user).clean();
        mapper.insertBatch(Lists.newArrayList(
                new UserEntity().setId(23L).setUserName("name1").setAge(23),
                new UserEntity().setId(24L).setUserName("name2").setAge(24)));
        db.table(t_user).count().eq(2L);
        db.table(t_user).query().print().eqDataMap(new UserTableMap(2)
                .age.values(23, 24)
                .user_name.values("name1", "name2"));
    }
}
