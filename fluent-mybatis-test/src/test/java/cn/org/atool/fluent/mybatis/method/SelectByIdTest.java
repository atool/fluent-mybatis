package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.generate.DM;
import cn.org.atool.fluent.mybatis.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.generate.entity.mapper.NoPrimaryMapper;
import cn.org.atool.fluent.mybatis.generate.entity.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.MyBatisSystemException;
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
        db.table(t_user).clean().insert(DM.user.initTable(3)
            .userName.values(DataGenerator.increase("username_%d")));
        UserEntity user = mapper.findById(3L);
        db.sqlList().wantFirstSql()
            .where().eq("id = ?");
        want.object(user)
            .eqMap(DM.user.entity()
                .userName.values("username_3")
            );
    }

    @Test
    public void test_selectById_noPrimary() throws Exception {
        db.table(t_no_primary).clean().insert(DM.noPrimary.initTable(3)
            .column1.values(1, 2, 3)
            .column2.values("c1", "c2", "c3")
        );
        want.exception(() -> noPrimaryMapper.findById(3L), MyBatisSystemException.class);
    }
}
