package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;

public class SqlLikeTest extends BaseTest {
    @Test
    void like() {
        ATM.dataMap.student.initTable(2)
            .userName.values("user1", "user2")
            .cleanAndInsert();
        want.exception(() -> StudentQuery.emptyQuery()
                .where.userName().like("%").end()
                .to().listEntity(), IllegalArgumentException.class)
            .contains("The like operation cannot be string '%' or empty only");
    }
}
