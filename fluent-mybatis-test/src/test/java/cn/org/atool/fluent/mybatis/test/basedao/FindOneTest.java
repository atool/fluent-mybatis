package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.generate.dao.intf.StudentDao;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
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
