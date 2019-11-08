package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.demo.datamap.table.UserTableMap;
import cn.org.atool.fluent.mybatis.demo.entity.UserEntity;
import cn.org.atool.fluent.mybatis.demo.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
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
