package cn.org.atool.fluent.mybatis.test.segment;

import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

/**
 * OrderByTest
 *
 * @author darui.wu
 * @create 2020/6/20 4:49 下午
 */
public class OrderByTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_order() throws Exception {
        StudentQuery query = StudentQuery.emptyQuery()
            .selectId()
            .where.id().eq(24L)
            .end()
            .orderBy.id().asc().age().desc()
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT `id` FROM fluent_mybatis.student WHERE `id` = ? " +
                "ORDER BY `id` ASC, `age` DESC", StringMode.SameAsSpace);
    }
}
