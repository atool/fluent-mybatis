package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.customize.StudentExtDao;
import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

/**
 * @author darui.wu
 * @create 2019/10/29 9:36 下午
 */
public class DeleteByEntityIdsTest extends BaseTest {
    @Autowired
    private StudentExtDao dao;

    @Test
    public void test_deleteByEntityIds() throws Exception {
        ATM.DataMap.student.initTable(10).cleanAndInsert();
        dao.deleteByEntityIds(Arrays.asList(new StudentEntity().setId(1L), new StudentEntity().setId(5L)));
        db.sqlList().wantFirstSql().eq("DELETE FROM student WHERE id IN (?, ?)");
        db.table(ATM.Table.student).count().isEqualTo(8);
    }
}
