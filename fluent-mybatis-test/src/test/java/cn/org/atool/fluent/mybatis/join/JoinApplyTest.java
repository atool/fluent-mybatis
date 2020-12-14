package cn.org.atool.fluent.mybatis.join;

import cn.org.atool.fluent.mybatis.base.crud.JoinBuilder;
import cn.org.atool.fluent.mybatis.generate.Refs;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.HomeAddressQuery;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class JoinApplyTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_join() {
        StudentQuery studentQuery = Refs.Query.student.aliasQuery()
            .select.age().end()
            .where.age().isNull().end()
            .groupBy.age().apply("t1.id").end()
            .having.max.age().gt(1L).end();
        HomeAddressQuery addressQuery = Refs.Query.homeAddress.aliasWith(studentQuery)
            .select.studentId().end()
            .where.address().like("vas").end()
            .groupBy.studentId().end();
        JoinBuilder<StudentQuery> query = JoinBuilder
            .from(studentQuery)
            .join(addressQuery)
            .on("t1.id = t2.id OR t1.age = t2.student_id")
            .limit(20);
        mapper.listMaps(query.build());
        db.sqlList().wantFirstSql().eq(
            "SELECT t1.age, t2.student_id " +
                "FROM student t1 " +
                "JOIN home_address t2 " +
                "ON t1.id = t2.id " +
                "OR t1.age = t2.student_id " +
                "WHERE t1.is_deleted = ? " +
                "AND t1.env = ? " +
                "AND t1.age IS NULL " +
                "AND t2.is_deleted = ? " +
                "AND t2.env = ? " +
                "AND t2.address LIKE ? " +
                "GROUP BY t1.age, t1.id, t2.student_id " +
                "HAVING MAX(t1.age) > ? " +
                "LIMIT ?, ?");
    }
}