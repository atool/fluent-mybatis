package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.Ref;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
 * @author darui.wu 2019/10/29 9:33 下午
 */
public class SelectByMapTest2 extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_selectByMap() {
        ATM.dataMap.student.initTable(4)
            .userName.values("u1", "u2", "u3", "u2")
            .env.values("test_env")
            .cleanAndInsert();

        List<StudentEntity> users = mapper.listByMap(true, new HashMap<String, Object>() {
            {
                this.put(Ref.Field.Student.userName.column, "u2");
            }
        });
        db.sqlList().wantFirstSql()
            .start("SELECT")
            .end("FROM fluent_mybatis.student " +
                "WHERE `is_deleted` = ? AND `env` = ? AND `user_name` = ?");
        want.list(users).eqDataMap(ATM.dataMap.student.entity(2)
            .userName.values("u2"));
    }
}
