package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.customize.StudentExtDao;
import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;
import org.test4j.tools.datagen.DataGenerator;

public class DeleteByQueryTest extends BaseTest {
    @Autowired
    private StudentExtDao dao;

    @Test
    public void test_deleteByQuery() throws Exception {
        ATM.dataMap.student.initTable(10)
            .userName.values(DataGenerator.increase("username_%d"))
            .env.values("test_env")
            .cleanAndInsert();
        dao.deleteByQuery("username_4", "username_5", "username_7");
        db.table(ATM.table.student).count().eq(7);
        db.sqlList().wantFirstSql()
            .eq("DELETE FROM student " +
                "WHERE is_deleted = ? AND env = ? AND user_name IN (?, ?, ?)", StringMode.SameAsSpace);
    }
}
