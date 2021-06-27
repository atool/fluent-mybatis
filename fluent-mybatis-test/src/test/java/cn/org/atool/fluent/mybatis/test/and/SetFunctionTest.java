package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentUpdate;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * SetFunctionTest
 *
 * @author darui.wu
 * @create 2020/6/4 11:31 上午
 */
public class SetFunctionTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    void setFunction() {
        ATM.dataMap.student.initTable(1)
            .id.values(1)
            .userName.values("test")
            .age.values(23)
            .cleanAndInsert();
        StudentUpdate update = new StudentUpdate()
            .update.userName().applyFunc("concat(user_name, ?)", "_aaa")
            .set.age().applyFunc("age+1").end()
            .where.id().eq(1L).end();
        mapper.updateBy(update);
        ATM.dataMap.student.table(1)
            .userName.values("test_aaa")
            .age.values(24)
            .eqTable();
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE student " +
            "SET gmt_modified = now(), " +
            "user_name = concat(user_name, ?), " +
            "age = age+1 " +
            "WHERE id = ?");
    }
}
