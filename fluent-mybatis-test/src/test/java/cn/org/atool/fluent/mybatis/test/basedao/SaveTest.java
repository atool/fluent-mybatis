package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.generate.datamap.TM;
import cn.org.atool.fluent.mybatis.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.customize.UserExtDao;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * SaveTest
 *
 * @author darui.wu
 * @create 2019/10/31 11:17 上午
 */
public class SaveTest extends BaseTest {
    @Autowired
    private UserExtDao dao;

    @Test
    public void test_save_noPk() throws Exception {
        db.table(t_user).clean();
        dao.save(new UserEntity().setUserName("test name").setAge(43));
        db.table(t_user).query().eqDataMap(TM.user.create(1)
            .user_name.values("test name")
            .age.values(43)
        );
    }

    @Test
    public void test_save_WithPk() throws Exception {
        db.table(t_user).clean();
        dao.save(new UserEntity().setId(4L).setUserName("test name").setAge(43));
        db.sqlList().wantFirstSql().contains("id,");
        db.table(t_user).query().eqDataMap(TM.user.create(1)
            .id.values(4)
            .user_name.values("test name")
            .age.values(43)
        );
    }

    @Test
    public void test_batchSave_ErrorPk() throws Exception {
        db.table(t_user).clean();
        want.exception(() ->
            dao.save(Arrays.asList(
                new UserEntity().setUserName("test name1").setAge(43),
                new UserEntity().setUserName("test name2").setAge(43).setId(5L)
            )), FluentMybatisException.class
        ).eq("The primary key of the list instance must be assigned to all or none");
    }

    @Test
    public void test_batchSave_WithPk() throws Exception {
        db.table(t_user).clean();
        dao.save(Arrays.asList(new UserEntity().setId(4L).setUserName("test name1").setAge(43),
            new UserEntity().setId(5L).setUserName("test name2").setAge(43)
        ));
        db.table(t_user).query().eqDataMap(TM.user.create(2)
            .id.values(4, 5)
            .user_name.values("test name1", "test name2")
            .age.values(43)
        );
    }

    @Test
    public void test_batchSave_NoPk() throws Exception {
        db.table(t_user).clean();
        List<UserEntity> list = list(
            new UserEntity().setUserName("test name1").setAge(43),
            new UserEntity().setUserName("test name2").setAge(43)
        );
        dao.save(list);
        db.table(t_user).query().eqDataMap(TM.user.create(2)
            .user_name.values("test name1", "test name2")
            .age.values(43)
        );
        want.number(list.get(0).getId()).isNull();
        want.number(list.get(1).getId()).isNull();
    }
}