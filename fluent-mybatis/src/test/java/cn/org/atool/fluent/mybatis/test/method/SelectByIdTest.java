package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.demo.generate.datamap.EM;
import cn.org.atool.fluent.mybatis.demo.generate.datamap.TM;
import cn.org.atool.fluent.mybatis.demo.generate.entity.NoPrimaryEntity;
import cn.org.atool.fluent.mybatis.demo.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapper.NoPrimaryMapper;
import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author darui.wu
 * @create 2019/10/29 9:33 下午
 */
public class SelectByIdTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Autowired
    private NoPrimaryMapper noPrimaryMapper;

    @Test
    public void test_selectById() throws Exception {
        db.table(t_user).clean().insert(TM.user.createWithInit(3)
            .user_name.values(DataGenerator.increase("username_%d")));
        UserEntity user = mapper.findById(3L);
        db.sqlList().wantFirstSql()
            .where().eq("id=?");
        want.object(user)
            .eqMap(EM.user.create()
                .userName.values("username_3")
            );
    }

    @Test
    public void test_selectById_noPrimary() throws Exception {
        db.table(t_no_primary).clean().insert(TM.no_primary.createWithInit(3)
            .column_1.values(1, 2, 3)
            .column_2.values("c1", "c2", "c3")
        );
        NoPrimaryEntity entity = noPrimaryMapper.findById(3L);
        db.sqlList().wantFirstSql()
            .where().eq("1!=1");
        want.object(entity).isNull();
    }
}