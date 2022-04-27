package cn.org.atool.fluent.mybatis.test.where.and;

import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * AndObjectTest_Exists
 *
 * @author darui.wu
 * @create 2020/6/18 5:39 下午
 */
public class WhereObjectTest_Exists extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    void test_exists() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.isDeleted().eq(true)
            .and.exists("select 1 FROM fluent_mybatis.student where age=?", 34)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT COUNT(*) " +
                "FROM fluent_mybatis.student " +
                "WHERE `is_deleted` = ? AND EXISTS (select 1 FROM fluent_mybatis.student where age=?)");
    }

    @Test
    void test_not_exists() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.isDeleted().eq(true)
            .and.notExists("select 1 FROM fluent_mybatis.student where age=?", 34)
            .end();
        mapper.count(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT COUNT(*) FROM fluent_mybatis.student " +
                "WHERE `is_deleted` = ? " +
                "AND NOT EXISTS (select 1 FROM fluent_mybatis.student where age=?)");
    }
}
