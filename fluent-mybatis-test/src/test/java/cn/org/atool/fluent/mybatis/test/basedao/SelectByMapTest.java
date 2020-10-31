package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.customize.StudentExtDao;
import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.helper.StudentMapping;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
 * @author darui.wu
 * @create 2019/10/29 9:33 下午
 */
public class SelectByMapTest extends BaseTest {
    @Autowired
    private StudentExtDao dao;

    @Test
    public void test_selectByMap() throws Exception {
        ATM.DataMap.student.initTable(10)
            .userName.values(DataGenerator.increase("username_%d"))
            .env.values("test_env")
            .cleanAndInsert();

        List<StudentEntity> users = dao.selectByMap(new HashMap<String, Object>() {
            {
                this.put(StudentMapping.userName.column, "username_4");
            }
        });
        db.sqlList().wantFirstSql().start("SELECT")
            .end("FROM t_student WHERE is_deleted = ? AND env = ? AND user_name = ?");
        want.list(users).eqDataMap(ATM.DataMap.student.entity(1)
            .userName.values("username_4"));
    }
}
