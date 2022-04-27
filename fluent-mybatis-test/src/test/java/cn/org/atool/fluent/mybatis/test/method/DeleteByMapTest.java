package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.customize.StudentExtDao;
import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.Ref;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * @author darui.wu 2019/10/29 9:36 下午
 */
@SuppressWarnings("unchecked")
public class DeleteByMapTest extends BaseTest {
    @Autowired
    private StudentExtDao dao;

    @Test
    public void test_deleteByMap() {
        ATM.dataMap.student.initTable(10)
            .userName.values("test1", "test12", "test3", "test12", "tess2")
            .env.values("test_env")
            .cleanAndInsert();
        dao.deleteByMap(new HashMap<String, Object>() {
            {
                this.put(Ref.Field.Student.userName.column, "test12");
            }
        });
        db.sqlList().wantFirstSql().eq("" +
            "DELETE FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? AND `env` = ? AND `user_name` = ?");
        ATM.dataMap.student.countEq(8);
    }

    @Test
    public void test_logicDeleteByMap() {
        dao.logicDeleteByMap(new HashMap<String, Object>() {
            {
                this.put(Ref.Field.Student.userName.column, "test12");
            }
        });
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE fluent_mybatis.student " +
            "SET `gmt_modified` = now(), `is_deleted` = ? " +
            "WHERE `is_deleted` = ? AND `env` = ? AND `user_name` = ?");
        db.sqlList().wantFirstPara().eqList(true, false, "test_env", "test12");
    }
}
