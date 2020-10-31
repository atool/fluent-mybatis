package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.customize.StudentExtDao;
import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

/**
 * @author darui.wu
 * @create 2019/10/29 9:35 下午
 */
public class UpdateByEntityIdTest extends BaseTest {
    @Autowired
    private StudentExtDao dao;

    @Test
    public void test_byEntityId() throws Exception {
        ATM.DataMap.student.initTable(5)
            .cleanAndInsert();

        dao.updateById(new StudentEntity().setId(2L).setUserName("test3").setAge(30));
        db.sqlList().wantFirstSql()
            .eq("UPDATE t_student SET gmt_modified = now(), age = ?, user_name = ? WHERE id = ?", StringMode.SameAsSpace);
        db.table(ATM.Table.student).queryWhere("id=2")
            .eqDataMap(ATM.DataMap.student.table(1)
                .userName.values("test3")
                .age.values(30)
            );
    }
}
