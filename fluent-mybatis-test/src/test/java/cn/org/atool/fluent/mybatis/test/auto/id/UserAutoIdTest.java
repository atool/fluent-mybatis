package cn.org.atool.fluent.mybatis.test.auto.id;

import cn.org.atool.fluent.mybatis.generate.datamap.ITable;
import cn.org.atool.fluent.mybatis.generate.datamap.TM;
import cn.org.atool.fluent.mybatis.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.generate.entity.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class UserAutoIdTest extends BaseTest {
    @Autowired
    UserMapper mapper;

    @Test
    void saveWithId() {
        db.table(ITable.t_user).clean();
        UserEntity user = new UserEntity()
            .setId(124L)
            .setUserName("fluent mybatis");
        int count = mapper.insert(user);
        want.number(count).isEqualTo(1);
        want.number(user.getId()).eq(124L);
        db.table(ITable.t_user).query().eqDataMap(TM.user.create(1).id.values(124L));
    }

    @Test
    void saveWithoutId() {
        UserEntity user = new UserEntity()
            .setUserName("fluent mybatis");
        db.table(ITable.t_user).clean();
        List<Long> ids = new ArrayList<>();
        for (int index = 0; index < 10; index++) {
            int count = mapper.insert(user.setId(null));
            want.number(count).isEqualTo(1);
            ids.add(user.getId());
        }
        db.table(ITable.t_user).query().eqByProperties("id", ids);
    }
}
