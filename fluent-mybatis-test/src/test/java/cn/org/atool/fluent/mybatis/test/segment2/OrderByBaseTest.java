package cn.org.atool.fluent.mybatis.test.segment2;

import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.generate.helper.StudentMapping;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderByBaseTest extends BaseTest {

    @Autowired
    private StudentMapper mapper;

    @Test
    void orderBy() {
        mapper.listEntity(new StudentQuery()
            .orderBy
            .id().asc()
            .desc(StudentMapping.userName).end()
        );
        db.sqlList().wantFirstSql()
            .end("FROM student ORDER BY id ASC, user_name DESC");
    }
}
