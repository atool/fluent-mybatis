package cn.org.atool.fluent.mybatis.join;

import cn.org.atool.fluent.mybatis.base.crud.JoinBuilder;
import cn.org.atool.fluent.mybatis.functions.QFunction;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.HomeAddressQuery;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentScoreQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static java.lang.String.format;

public class JoinQueryTest_Lambda1 extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_join() {
        QFunction<StudentQuery> studentQuery = q -> q
            .select.age().end()
            .where.isDeleted().eq(true)
            .and.age().isNull()
            .end()
            .groupBy.age().apply("id").end()
            .having.max.age().gt(1L).end()
            .orderBy.id().desc().end();
        QFunction<HomeAddressQuery> addressQuery = q -> q
            .select.studentId().end()
            .where.isDeleted().eq(true)
            .and.address().like("vas")
            .end()
            .groupBy.studentId().end()
            .orderBy.id().asc().end();
        JoinBuilder<StudentQuery> query = JoinBuilder
            .from(StudentQuery.class, studentQuery)
            .join(HomeAddressQuery.class, addressQuery)
            .on(l -> l.where.id(), r -> r.where.id())
            .on(l -> l.where.age(), r -> r.where.studentId()).endJoin()
            .limit(20);
        mapper.listMaps(query.build());
        String[] a = query.getAlias();
        db.sqlList().wantFirstSql().eq(
            format("SELECT %s.age, %s.student_id ", a[0], a[1]) +
                format("FROM student %s ", a[0]) +
                format("JOIN home_address %s ", a[1]) +
                format("ON %s.id = %s.id ", a[0], a[1]) +
                format("AND %s.age = %s.student_id ", a[0], a[1]) +
                format("WHERE %s.is_deleted = ? ", a[0]) +
                format("AND %s.age IS NULL ", a[0]) +
                format("AND %s.is_deleted = ? ", a[1]) +
                format("AND %s.address LIKE ? ", a[1]) +
                format("GROUP BY %s.age, %s.id, %s.student_id ", a[0], a[0], a[1]) +
                format("HAVING MAX(%s.age) > ? ", a[0]) +
                format("ORDER BY %s.id DESC, %s.id ASC ", a[0], a[1]) +
                "LIMIT ?, ?");
    }

    @Test
    public void test_left_join() {
        QFunction<StudentQuery> uq = q -> q
            .select.age().end()
            .where.isDeleted().eq(true)
            .and.age().isNull()
            .end()
            .groupBy.age().apply("id").end()
            .having.max.age().gt(1L).end();
        QFunction<HomeAddressQuery> aq = q -> q
            .select.studentId().end()
            .where.isDeleted().eq(true)
            .and.address().like("vas")
            .end()
            .groupBy.studentId().end();
        JoinBuilder<StudentQuery> query = JoinBuilder
            .from(StudentQuery.class, uq)
            .leftJoin(HomeAddressQuery.class, aq)
            .on(l -> l.where.id(), r -> r.where.id())
            .on(l -> l.where.age(), r -> r.where.studentId()).endJoin()
            .distinct()
            .limit(20);
        mapper.listMaps(query.build());
        String[] a = query.getAlias();
        db.sqlList().wantFirstSql().eq(
            format("SELECT DISTINCT %s.age, %s.student_id ", a[0], a[1]) +
                format("FROM student %s ", a[0]) +
                format("LEFT JOIN home_address %s ", a[1]) +
                format("ON %s.id = %s.id ", a[0], a[1]) +
                format("AND %s.age = %s.student_id ", a[0], a[1]) +
                format("WHERE %s.is_deleted = ? ", a[0]) +
                format("AND %s.age IS NULL ", a[0]) +
                format("AND %s.is_deleted = ? ", a[1]) +
                format("AND %s.address LIKE ? ", a[1]) +
                format("GROUP BY %s.age, %s.id, %s.student_id ", a[0], a[0], a[1]) +
                format("HAVING MAX(%s.age) > ? ", a[0]) +
                "LIMIT ?, ?");
    }

    @Test
    public void test_right_join() {
        QFunction<StudentQuery> uq = q -> q
            .where.isDeleted().eq(true)
            .and.age().isNull()
            .end();
        QFunction<HomeAddressQuery> aq = q -> q
            .where.isDeleted().eq(true)
            .and.address().like("vas")
            .end();
        JoinBuilder<StudentQuery> query = JoinBuilder
            .from(StudentQuery.class, uq)
            .rightJoin(HomeAddressQuery.class, aq)
            .on(l -> l.where.id(), r -> r.where.id())
            .endJoin();
        mapper.listMaps(query.build());
        String[] a = query.getAlias();
        db.sqlList().wantFirstSql()
            .end(format("FROM student %s RIGHT JOIN home_address %s ", a[0], a[1]) +
                format("ON %s.id = %s.id ", a[0], a[1]) +
                format("WHERE %s.is_deleted = ? ", a[0]) +
                format("AND %s.age IS NULL ", a[0]) +
                format("AND %s.is_deleted = ? ", a[1]) +
                format("AND %s.address LIKE ?", a[1]));
    }

    @Test
    void three_join() {
        QFunction<StudentQuery> uq = q -> q
            .where.age().eq(3).end();
        QFunction<HomeAddressQuery> aq = q -> q
            .where.address().like("xxx").end();
        JoinBuilder query = JoinBuilder
            .from(StudentQuery.class, uq)
            .leftJoin(HomeAddressQuery.class, aq)
            .on(l -> l.where.homeAddressId(), r -> r.where.id()).endJoin()
            .leftJoin(StudentScoreQuery.class, q -> q
                .where.subject().in(new String[]{"a", "b", "c"}).end())
            .on(l -> l.where.id(), r -> r.where.studentId()).endJoin();
        mapper.listMaps(query.build());
        String[] a = query.getAlias();
        db.sqlList().wantFirstSql()
            .contains(new String[]{a[0] + ".id", a[1] + ".id", a[2] + ".id"})
            .end(format("FROM student %s LEFT JOIN home_address %s ", a[0], a[1]) +
                format("ON %s.home_address_id = %s.id ", a[0], a[1]) +
                format("LEFT JOIN student_score %s ON %s.id = %s.student_id ", a[2], a[0], a[2]) +
                format("WHERE %s.age = ? ", a[0]) +
                format("AND %s.address LIKE ? ", a[1]) +
                format("AND %s.subject IN (?, ?, ?)", a[2]));
    }
}
