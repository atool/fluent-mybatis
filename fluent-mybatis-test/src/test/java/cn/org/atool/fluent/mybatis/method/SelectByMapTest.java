package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.helper.StudentMapping;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
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
    private StudentMapper mapper;

    @Test
    public void test_selectByMap() throws Exception {
        ATM.dataMap.student.initTable(4)
            .userName.values("u1", "u2", "u3", "u2")
            .cleanAndInsert();

        List<StudentEntity> users = mapper.listByMap(new HashMap<String, Object>() {
            {
                this.put(StudentMapping.userName.column, "u2");
            }
        });
        db.sqlList().wantFirstSql()
            .start("SELECT")
            .end("FROM student WHERE `user_name` = ?");
        want.list(users).eqDataMap(ATM.dataMap.student.entity(2)
            .userName.values("u2"));
    }
}
