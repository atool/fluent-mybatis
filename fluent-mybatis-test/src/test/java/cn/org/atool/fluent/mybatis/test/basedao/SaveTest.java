package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.customize.UserExtDao;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.UserEntity;
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
        db.table(ATM.Table.user).clean();
        dao.save(new UserEntity().setUserName("test name").setAge(43));
        db.table(ATM.Table.user).query().eqDataMap(ATM.DataMap.user.table(1)
            .userName.values("test name")
            .age.values(43)
        );
    }

    @Test
    public void test_save_WithPk() throws Exception {
        db.table(ATM.Table.user).clean();
        dao.save(new UserEntity().setId(4L).setUserName("test name").setAge(43));
        db.sqlList().wantFirstSql().contains("id,");
        db.table(ATM.Table.user).query().eqDataMap(ATM.DataMap.user.table(1)
            .id.values(4)
            .userName.values("test name")
            .age.values(43)
        );
    }

    @Test
    public void test_batchSave_ErrorPk() throws Exception {
        db.table(ATM.Table.user).clean();
        want.exception(() ->
            dao.save(Arrays.asList(
                new UserEntity().setUserName("test name1").setAge(43),
                new UserEntity().setUserName("test name2").setAge(43).setId(5L)
            )), FluentMybatisException.class
        ).eq("The primary key of the list instance must be assigned to all or none");
    }

    @Test
    public void test_batchSave_WithPk() throws Exception {
        db.table(ATM.Table.user).clean();
        dao.save(Arrays.asList(new UserEntity().setId(4L).setUserName("test name1").setAge(43),
            new UserEntity().setId(5L).setUserName("test name2").setAge(43)
        ));
        db.table(ATM.Table.user).query().eqDataMap(ATM.DataMap.user.table(2)
            .id.values(4, 5)
            .userName.values("test name1", "test name2")
            .age.values(43)
        );
    }

    @Test
    public void test_batchSave_NoPk() throws Exception {
        db.table(ATM.Table.user).clean();
        List<UserEntity> list = list(
            new UserEntity().setUserName("test name1").setAge(43),
            new UserEntity().setUserName("test name2").setAge(43)
        );
        dao.save(list);
        db.table(ATM.Table.user).query().eqDataMap(ATM.DataMap.user.table(2)
            .userName.values("test name1", "test name2")
            .age.values(43)
        );
        want.number(list.get(0).getId()).isNull();
        want.number(list.get(1).getId()).isNull();
    }
}
