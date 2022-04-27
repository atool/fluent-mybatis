package cn.org.atool.fluent.mybatis.test.join;

import cn.org.atool.fluent.mybatis.base.crud.JoinBuilder;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.HomeAddressQuery;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentScoreQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static java.lang.String.format;

public class JoinQueryTest_Lambda1 extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_join() {
        StudentQuery studentQuery = StudentQuery.emptyQuery("t1")
            .select.age().end()
            .where.isDeleted().eq(true)
            .and.age().isNull()
            .end()
            .groupBy.age().apply("id").end()
            .having.max.age().gt(1L).end()
            .orderBy.id().desc().end();
        HomeAddressQuery addressQuery = HomeAddressQuery.emptyQuery("t2")
            .select.studentId().end()
            .where.isDeleted().eq(true)
            .and.address().like("vas")
            .end()
            .groupBy.studentId().end()
            .orderBy.id().asc().end();
        JoinBuilder<StudentQuery> query = JoinBuilder
            .from(studentQuery)
            .join(addressQuery)
            .on(l -> l.where.id(), r -> r.where.id())
            .on(l -> l.where.age(), r -> r.where.studentId())
            .endJoin()
            .limit(20);
        mapper.listMaps(query.build());
        String[] a = query.getAlias();
        db.sqlList().wantFirstSql().eq(
            format("SELECT %s.`age`, %s.`student_id` ", a[0], a[1]) +
                format("FROM fluent_mybatis.student %s ", a[0]) +
                format("JOIN `home_address` %s ", a[1]) +
                format("ON %s.`id` = %s.`id` ", a[0], a[1]) +
                format("AND %s.`age` = %s.`student_id` ", a[0], a[1]) +
                format("WHERE %s.`is_deleted` = ? ", a[0]) +
                format("AND %s.`age` IS NULL ", a[0]) +
                format("AND %s.`is_deleted` = ? ", a[1]) +
                format("AND %s.`address` LIKE ? ", a[1]) +
                format("GROUP BY %s.`age`, %s.`id`, %s.`student_id` ", a[0], a[0], a[1]) +
                format("HAVING MAX(%s.`age`) > ? ", a[0]) +
                format("ORDER BY %s.`id` DESC, %s.`id` ASC ", a[0], a[1]) +
                "LIMIT ?, ?");
    }

    @Test
    public void test_left_join() {
        StudentQuery uq = StudentQuery.emptyQuery("t1")
            .select.age().end()
            .where.isDeleted().eq(true)
            .and.age().isNull()
            .end()
            .groupBy.age().apply("id").end()
            .having.max.age().gt(1L).end();
        HomeAddressQuery aq = HomeAddressQuery.emptyQuery("t2")
            .select.studentId().end()
            .where.isDeleted().eq(true)
            .and.address().like("vas")
            .end()
            .groupBy.studentId().end();
        JoinBuilder<StudentQuery> query = JoinBuilder
            .from(uq)
            .leftJoin(aq)
            .on(l -> l.where.id(), r -> r.where.id())
            .on(l -> l.where.age(), r -> r.where.studentId()).endJoin()
            .distinct()
            .limit(20);
        mapper.listMaps(query.build());
        String[] a = query.getAlias();
        db.sqlList().wantFirstSql().eq(
            format("SELECT DISTINCT %s.`age`, %s.`student_id` ", a[0], a[1]) +
                format("FROM fluent_mybatis.student %s ", a[0]) +
                format("LEFT JOIN `home_address` %s ", a[1]) +
                format("ON %s.`id` = %s.`id` ", a[0], a[1]) +
                format("AND %s.`age` = %s.`student_id` ", a[0], a[1]) +
                format("WHERE %s.`is_deleted` = ? ", a[0]) +
                format("AND %s.`age` IS NULL ", a[0]) +
                format("AND %s.`is_deleted` = ? ", a[1]) +
                format("AND %s.`address` LIKE ? ", a[1]) +
                format("GROUP BY %s.`age`, %s.`id`, %s.`student_id` ", a[0], a[0], a[1]) +
                format("HAVING MAX(%s.`age`) > ? ", a[0]) +
                "LIMIT ?, ?");
    }

    @Test
    public void test_right_join() {
        StudentQuery uq = StudentQuery.emptyQuery("t1")
            .where.isDeleted().eq(true)
            .and.age().isNull()
            .end();
        HomeAddressQuery aq = HomeAddressQuery.emptyQuery("t2")
            .where.isDeleted().eq(true)
            .and.address().like("vas")
            .end();
        JoinBuilder<StudentQuery> query = JoinBuilder
            .from(uq)
            .rightJoin(aq)
            .on(l -> l.where.id(), r -> r.where.id())
            .endJoin();
        mapper.listMaps(query.build());
        String[] a = query.getAlias();
        db.sqlList().wantFirstSql()
            .end(format("FROM fluent_mybatis.student %s RIGHT JOIN `home_address` %s ", a[0], a[1]) +
                format("ON %s.`id` = %s.`id` ", a[0], a[1]) +
                format("WHERE %s.`is_deleted` = ? ", a[0]) +
                format("AND %s.`age` IS NULL ", a[0]) +
                format("AND %s.`is_deleted` = ? ", a[1]) +
                format("AND %s.`address` LIKE ?", a[1]));
    }

    @Test
    void three_join() {
        StudentQuery uq = StudentQuery.emptyQuery("t1")
            .selectId()
            .where.age().eq(3).end();
        HomeAddressQuery aq = HomeAddressQuery.emptyQuery("t2")
            .selectId()
            .where.address().like("xxx").end();
        JoinBuilder query = JoinBuilder
            .from(uq)
            .leftJoin(aq)
            .on(l -> l.where.homeAddressId(), r -> r.where.id()).endJoin()
            .leftJoin(StudentScoreQuery.emptyQuery("t3")
                .selectId()
                .where.subject().in(new String[]{"a", "b", "c"}).end())
            .on(l -> l.where.id(), r -> r.where.studentId()).endJoin();
        mapper.listMaps(query.build());
        String[] a = query.getAlias();
        db.sqlList().wantFirstSql().eq("" +
            format("SELECT t1.`id`, t2.`id`, t3.`id` ") +
            format("FROM fluent_mybatis.student %s LEFT JOIN `home_address` %s ", a[0], a[1]) +
            format("ON %s.`home_address_id` = %s.`id` ", a[0], a[1]) +
            format("LEFT JOIN `student_score` %s ON %s.`id` = %s.`student_id` ", a[2], a[0], a[2]) +
            format("WHERE %s.`age` = ? ", a[0]) +
            format("AND %s.`address` LIKE ? ", a[1]) +
            format("AND %s.`subject` IN (?, ?, ?)", a[2]));
    }
}
