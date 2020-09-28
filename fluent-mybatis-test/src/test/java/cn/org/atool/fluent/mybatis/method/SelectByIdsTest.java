package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.generate.datamap.EM;
import cn.org.atool.fluent.mybatis.generate.datamap.TM;
import cn.org.atool.fluent.mybatis.generate.entity.NoPrimaryEntity;
import cn.org.atool.fluent.mybatis.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.generate.mapper.NoPrimaryMapper;
import cn.org.atool.fluent.mybatis.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * @author darui.wu
 * @create 2019/10/29 9:33 下午
 */
public class SelectByIdsTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Autowired
    private NoPrimaryMapper noPrimaryMapper;

    @Test
    public void test_selectById() throws Exception {
        db.table(t_user).clean().insert(TM.user.createWithInit(3)
            .user_name.values(DataGenerator.increase("username_%d")));
        List<UserEntity> users = mapper.listByIds(Arrays.asList(3L, 1L));
        db.sqlList().wantFirstSql()
            .where().eq("id IN ( ? , ? )");
        want.list(users)
            .eqMap(EM.user.create(2)
                .userName.values("username_1", "username_3")
            );
    }

    @Test
    public void test_selectById_noPrimary() throws Exception {
        db.table(t_no_primary).clean().insert(TM.no_primary.createWithInit(3)
            .column_1.values(1, 2, 3)
            .column_2.values("c1", "c2", "c3")
        );
        List<NoPrimaryEntity> entities = noPrimaryMapper.listByIds(Arrays.asList(3L));
        db.sqlList().wantFirstSql()
            .where().eq("1!=1");
        want.list(entities).sizeEq(0);
    }
}