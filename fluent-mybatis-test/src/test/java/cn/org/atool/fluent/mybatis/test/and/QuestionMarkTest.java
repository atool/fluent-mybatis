package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentUpdate;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * QuestionMarkTest
 *
 * @author darui.wu 2020/6/20 3:57 下午
 */
public class QuestionMarkTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @DisplayName("?反义处理")
    @Test
    void test() {
        ATM.dataMap.student.initTable(1)
            .id.values(1)
            .userName.values("test")
            .age.values(23)
            .cleanAndInsert();
        StudentUpdate update = new StudentUpdate()
            .set.userName().applyFunc("concat(user_name, concat('_\\\\\\?', ? ))", "_aaa")
            .set.age().applyFunc("age+1").end()
            .where.id().eq(1L).end();
        mapper.updateBy(update);
        ATM.dataMap.student.table(1)
            .userName.values("test_\\?_aaa")
            .age.values(24)
            .eqTable();
        db.sqlList().wantFirstSql()
            .eq("UPDATE student " +
                "SET `gmt_modified` = now(), " +
                "`user_name` = concat(user_name, concat('_\\\\?', ? )), " +
                "`age` = age+1 " +
                "WHERE `id` = ?");
    }
}
