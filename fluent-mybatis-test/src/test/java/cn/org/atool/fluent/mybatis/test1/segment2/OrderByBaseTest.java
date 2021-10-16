package cn.org.atool.fluent.mybatis.test1.segment2;

import cn.org.atool.fluent.mybatis.generator.shared2.Ref;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test1.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderByBaseTest extends BaseTest {

    @Autowired
    private StudentMapper mapper;

    @Test
    void orderBy() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .orderBy
            .id().asc()
            .desc(Ref.Field.Student.userName).end()
        );
        db.sqlList().wantFirstSql()
            .end("FROM fluent_mybatis.student ORDER BY `id` ASC, `user_name` DESC");
    }
}
