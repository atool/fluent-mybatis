package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.customize.StudentExtDao;
import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author darui.wu
 * @create 2019/10/29 5:38 下午
 */
public class ExistPkTest extends BaseTest {
    @Autowired
    private StudentExtDao dao;

    @Test
    public void test_exist() {
        ATM.dataMap.student.table(2)
            .id.values(1, 3)
            .env.values("test_env")
            .isDeleted.values(0)
            .cleanAndInsert();
        boolean existed = dao.existPk(1);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT COUNT(*) FROM fluent_mybatis.student WHERE `id` = ? LIMIT ?, ?");
        want.bool(existed).is(true);

        existed = dao.existPk(2);
        want.bool(existed).is(false);
    }
}
