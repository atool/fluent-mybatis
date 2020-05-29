package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.demo.generate.datamap.TM;
import cn.org.atool.fluent.mybatis.demo.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class InsertBatchTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void testInsertBatch_withoutPk() {
        db.table(t_user).clean();
        List<UserEntity> list = list(
            new UserEntity().setUserName("name1").setAge(23),
            new UserEntity().setUserName("name2").setAge(24));
        mapper.insertBatch(list);
        db.table(t_user).count().eq(2);
        db.table(t_user).query().print()
            .eqDataMap(TM.user.create(2)
                .age.values(23, 24)
                .user_name.values("name1", "name2")
            );
        want.number(list.get(0).getId()).isGt(0L);
        want.number(list.get(1).getId()).isGt(1L);
    }

    @Test
    public void testInsertBatch_WithId() {
        db.table(t_user).clean();
        List<UserEntity> list = list(
            new UserEntity().setId(23L).setUserName("name1").setAge(23),
            new UserEntity().setId(24L).setUserName("name2").setAge(24));
        mapper.insertBatch(list);
        db.table(t_user).count().eq(2);
        db.table(t_user).query().print()
            .eqDataMap(TM.user.create(2)
                .age.values(23, 24)
                .user_name.values("name1", "name2")
            );
        want.array(list.stream().map(UserEntity::getId).toArray()).eqReflect(new long[]{23, 24});
    }

    @DisplayName("部分id有值，导致严重的后果，实体id被篡改")
    @Test
    public void testInsertBatch() {
        db.table(t_user).clean();
        List<UserEntity> list = list(
            new UserEntity().setUserName("name1").setAge(23).setId(101L),
            new UserEntity().setUserName("name2").setAge(24));
        mapper.insertBatch(list);
        db.table(t_user).count().eq(2);
        db.table(t_user).query().print()
            .eqDataMap(TM.user.create(2)
                .age.values(23, 24)
                .user_name.values("name1", "name2")
            );
        want.number(list.get(1).getId()).isNull();
        want.exception(() ->
            want.number(list.get(0).getId()).eq(101L), AssertionError.class);
    }
}