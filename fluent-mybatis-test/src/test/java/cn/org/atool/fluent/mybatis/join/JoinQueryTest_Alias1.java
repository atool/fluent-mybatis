package cn.org.atool.fluent.mybatis.join;

import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.base.JoinBuilder;
import cn.org.atool.fluent.mybatis.generate.Refs;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.HomeAddressQuery;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentScoreQuery;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class JoinQueryTest_Alias1 extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_join() {
        StudentQuery studentQuery = Refs.Query.student.defaultAliasQuery()
            .select.age().end()
            .where.age().isNull().end()
            .groupBy.age().apply("t1.id").end()
            .having.max.age().gt(1L).end()
            .orderBy.id().desc().end();
        HomeAddressQuery addressQuery = Refs.Query.homeAddress.joinFrom(studentQuery)
            .select.studentId().end()
            .where.address().like("vas").end()
            .groupBy.studentId().end()
            .orderBy.id().asc().end();
        JoinBuilder<StudentQuery> query = JoinBuilder
            .from(studentQuery)
            .join(addressQuery)
            .on(l -> l.where.id(), r -> r.where.id())
            .on(l -> l.where.age(), r -> r.where.studentId()).endJoin()
            .limit(20);
        mapper.listMaps(query.build());
        db.sqlList().wantFirstSql().eq(
            "SELECT t1.age, t2.student_id " +
                "FROM student t1 " +
                "JOIN home_address t2 " +
                "ON t1.id = t2.id " +
                "AND t1.age = t2.student_id " +
                "WHERE t1.is_deleted = ? " +
                "AND t1.env = ? " +
                "AND t1.age IS NULL " +
                "AND t2.is_deleted = ? " +
                "AND t2.env = ? " +
                "AND t2.address LIKE ? " +
                "GROUP BY t1.age, t1.id, t2.student_id " +
                "HAVING MAX(t1.age) > ? " +
                "ORDER BY t1.id DESC, t2.id ASC " +
                "LIMIT ?, ?");
    }

    @Test
    public void test_left_join() {
        StudentQuery studentQuery = Refs.Query.student.defaultQuery("t1")
            .select.age().end()
            .where.age().isNull().end()
            .groupBy.age().apply("t1.id").end()
            .having.max.age().gt(1L).end();
        HomeAddressQuery addressQuery = Refs.Query.homeAddress.defaultQuery("t2", studentQuery)
            .select.studentId().end()
            .where.address().like("vas").end()
            .groupBy.studentId().end();
        JoinBuilder<StudentQuery> query = JoinBuilder
            .from(studentQuery)
            .leftJoin(addressQuery)
            .on((join, l, r) -> join
                .on(l.where.id(), r.where.id())
                .on(l.where.age(), r.where.studentId())
            )
            .distinct()
            .limit(20);
        mapper.listMaps(query.build());
        db.sqlList().wantFirstSql().eq(
            "SELECT DISTINCT t1.age, t2.student_id " +
                "FROM student t1 " +
                "LEFT JOIN home_address t2 " +
                "ON t1.id = t2.id " +
                "AND t1.age = t2.student_id " +
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

    @Test
    public void test_right_join() {
        Parameters parameters = new Parameters();
        JoinBuilder<StudentQuery> query = JoinBuilder
            .from(new StudentQuery("t1", parameters)
                .where.isDeleted().eq(true)
                .and.age().isNull()
                .end()
            ).rightJoin(new HomeAddressQuery("t2", parameters)
                .where.isDeleted().eq(true)
                .and.address().like("vas")
                .end())
            .on(l -> l.where.id(), r -> r.where.id())
            .endJoin();
        mapper.listMaps(query.build());
        db.sqlList().wantFirstSql()
            .end("FROM student t1 RIGHT JOIN home_address t2 " +
                "ON t1.id = t2.id " +
                "WHERE t1.is_deleted = ? " +
                "AND t1.age IS NULL " +
                "AND t2.is_deleted = ? " +
                "AND t2.address LIKE ?");
    }

    @Test
    void three_join() {
        Parameters parameters = new Parameters();
        IQuery query = JoinBuilder
            .from(new StudentQuery("t1", parameters)
                .where.age().eq(3).end())
            .leftJoin(new HomeAddressQuery("t2", parameters)
                .where.address().like("xxx").end())
            .on(l -> l.where.homeAddressId(), r -> r.where.id()).endJoin()
            .leftJoin(new StudentScoreQuery("t3", parameters)
                .where.subject().in(new String[]{"a", "b", "c"}).end())
            .on(l -> l.where.id(), r -> r.where.studentId()).endJoin()
            .build();
        mapper.listMaps(query);
        db.sqlList().wantFirstSql()
            .contains(new String[]{"t1.id", "t2.id", "t3.id"})
            .end("FROM student t1 LEFT JOIN home_address t2 " +
                "ON t1.home_address_id = t2.id " +
                "LEFT JOIN student_score t3 ON t1.id = t3.student_id " +
                "WHERE t1.age = ? " +
                "AND t2.address LIKE ? " +
                "AND t3.subject IN (?, ?, ?)");
    }
}