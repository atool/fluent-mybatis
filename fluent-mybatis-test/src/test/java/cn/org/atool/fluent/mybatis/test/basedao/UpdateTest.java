package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.customize.StudentExtDao;
import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TODO:类说明
 *
 * @author darui.wu
 * @create 2019/10/31 11:16 上午
 */
public class UpdateTest extends BaseTest {
    @Autowired
    private StudentExtDao dao;

    @Test
    public void test_update() throws Exception {
        ATM.DataMap.student.initTable(5)
            .userName.values(DataGenerator.increase("username_%d"))
            .env.values("test_env")
            .cleanAndInsert();

        dao.updateUserNameById("new_user_name", 4L);
        db.sqlList().wantFirstSql().eq("UPDATE t_student " +
            "SET gmt_modified = now(), user_name = ? " +
            "WHERE is_deleted = ? AND env = ? AND id = ?");
        db.table(ATM.Table.student).queryWhere("id=4")
            .eqDataMap(ATM.DataMap.student.table(1)
                .userName.values("new_user_name")
            );
    }
}
