package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.generate.datamap.TM;
import cn.org.atool.fluent.mybatis.generate.entity.NoAutoIdEntity;
import cn.org.atool.fluent.mybatis.generate.entity.NoPrimaryEntity;
import cn.org.atool.fluent.mybatis.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.generate.mapper.NoAutoIdMapper;
import cn.org.atool.fluent.mybatis.generate.mapper.NoPrimaryMapper;
import cn.org.atool.fluent.mybatis.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

public class InsertTest extends BaseTest {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NoAutoIdMapper idMapper;

    @Autowired
    private NoPrimaryMapper noPrimaryMapper;

    @Test
    public void testInsert() {
        db.table(t_user).clean();
        UserEntity user = new UserEntity()
            .setAge(23)
            .setUserName("tom mike");
        userMapper.insert(user);
        user.setId(null);
        userMapper.insert(user);

        want.number(user.getId()).isGt(1L);
        db.table(t_user).query().eqDataMap(TM.user.create(2)
            .age.values(23)
            .user_name.values("tom mike")
        );
    }

    @Test
    public void testInsert_NoAutoId() {
        db.table(t_no_auto_id).clean();
        idMapper.insert(new NoAutoIdEntity()
            .setId("test-id-1")
            .setColumn1("test")
        );
        idMapper.insert(new NoAutoIdEntity()
            .setId("test-id-2")
            .setColumn1("test")
        );
        db.table(t_no_auto_id).query().eqDataMap(TM.no_auto_id.create(2)
            .id.values("test-id-1", "test-id-2")
        );
    }

    @Test
    public void testInsert_NoAutoId_conflict() {
        db.table(t_no_auto_id).clean().insert(TM.no_auto_id.createWithInit(1)
            .id.values("test-id-1")
            .column_1.values("test")
        );
        want.exception(() -> {
            idMapper.insert(new NoAutoIdEntity()
                .setId("test-id-1")
                .setColumn1("test")
            );
        }, DuplicateKeyException.class, MyBatisSystemException.class);
    }

    @Test
    public void test_insert_noPrimary() {
        db.table(t_no_primary).clean();
        noPrimaryMapper.insert(new NoPrimaryEntity()
            .setColumn1(23)
            .setColumn2("test")
        );
        db.table(t_no_primary).query().eqDataMap(TM.no_primary.create(1)
            .column_1.values(23)
            .column_2.values("test")
        );
    }
}