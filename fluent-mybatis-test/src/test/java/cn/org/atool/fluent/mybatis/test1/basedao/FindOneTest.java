package cn.org.atool.fluent.mybatis.test1.basedao;

import cn.org.atool.fluent.mybatis.generator.shared2.dao.intf.StudentDao;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test1.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FindOneTest extends BaseTest {
    @Autowired
    StudentDao dao;

    @Test
    void findOne() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.id().eq(2).end();
        dao.findOne(query).orElse(null);
    }
}
