package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.generate.datamap.TM;
import cn.org.atool.fluent.mybatis.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.generate.entity.mapper.UserMapper;
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
        want.number(list.get(0).getId()).isNull();
        want.number(list.get(1).getId()).isNull();
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
        want.array(list.stream().map(UserEntity::getId).toArray())
            .eqReflect(new long[]{23, 24});
    }

    @DisplayName("部分id有值，实体id不会回写")
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
    }
}