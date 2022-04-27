package cn.org.atool.fluent.mybatis.test.join;

import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.JoinBuilder;
import cn.org.atool.fluent.mybatis.generator.shared2.Ref;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.HomeAddressEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.HomeAddressQuery;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentScoreQuery;
import cn.org.atool.fluent.mybatis.metadata.JoinType;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;
import org.test4j.module.database.IDatabase;

import static java.lang.String.format;

@SuppressWarnings("rawtypes")
public class JoinQueryTest_Alias1 extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_join() {
        StudentQuery studentQuery = Ref.Query.student.alias()
            .select.age().end()
            .where.age().isNull().end()
            .groupBy.age().apply("id").end()
            .having.max.age().gt(1L).end()
            .orderBy.id().desc().end();
        HomeAddressQuery addressQuery = Ref.Query.homeAddress.alias()
            .select.studentId().end()
            .where.address().like("vas").end()
            .groupBy.studentId().end()
            .orderBy.id().asc().end();
        JoinBuilder<StudentQuery> query = JoinBuilder
            .from(studentQuery)
            .join(addressQuery)
            .on(l -> l.where.id(), r -> r.where.id())
            .onLeft(l -> l.where.id().eq(1L).age().eq(1L))
            .onLeft(l -> l.where.userName().eq("张三"))
            .onRight(r -> r.where.id().eq(1L).studentId().eq(1L))
            .onApply(format("%s.`id` > ?", studentQuery.getTableAlias()), 1)
            .onEq("age", "student_id").endJoin()
            .limit(20);
        this.mapper.listMaps(query.build());
        String a1 = studentQuery.getTableAlias();
        String a2 = addressQuery.getTableAlias();
        IDatabase.db.sqlList().wantFirstSql().eq(
            format("SELECT %s.`age`, %s.`student_id` ", a1, a2) +
                format("FROM fluent_mybatis.student %s ", a1) +
                format("JOIN `home_address` %s ", a2) +
                format("ON %s.`id` = %s.`id` ", a1, a2) +
                format("AND %s.`id` = ? AND %s.`age` = ? ", a1, a1) +
                format("AND %s.`user_name` = ? ", a1) +
                format("AND %s.`id` = ? AND %s.`student_id` = ? ", a2, a2) +
                format("AND %s.`id` > ? ", studentQuery.getTableAlias()) +
                format("AND %s.`age` = %s.`student_id` ", a1, a2) +
                format("WHERE %s.`is_deleted` = ? ", a1) +
                format("AND %s.`env` = ? ", a1) +
                format("AND %s.`age` IS NULL ", a1) +
                format("AND %s.`is_deleted` = ? ", a2) +
                format("AND %s.`env` = ? ", a2) +
                format("AND %s.`address` LIKE ? ", a2) +
                format("GROUP BY %s.`age`, %s.`id`, %s.`student_id` ", a1, a1, a2) +
                format("HAVING MAX(%s.`age`) > ? ", a1) +
                format("ORDER BY %s.`id` DESC, %s.`id` ASC ", a1, a2) +
                "LIMIT ?, ?", StringMode.SameAsSpace);
    }

    @Test
    public void test_left_join() {
        StudentQuery studentQuery = Ref.Query.student.query("t1")
            .select.age().end()
            .where.age().isNull().end()
            .groupBy.age().apply("id").end()
            .having.max.age().gt(1L).end();
        HomeAddressQuery addressQuery = Ref.Query.homeAddress.query("t2")
            .select.studentId().end()
            .where.address().like("vas").end()
            .groupBy.studentId().end();
        JoinBuilder<StudentQuery> query = JoinBuilder
            .from(studentQuery)
            .leftJoin(addressQuery)
            .onEq(StudentEntity::getId, HomeAddressEntity::getId)
            .onEq(StudentEntity::getAge, HomeAddressEntity::getStudentId)
            .endJoin()
            .distinct()
            .limit(20);
        this.mapper.listMaps(query.build());
        IDatabase.db.sqlList().wantFirstSql().eq(
            "SELECT DISTINCT t1.`age`, t2.`student_id` " +
                "FROM fluent_mybatis.student t1 " +
                "LEFT JOIN `home_address` t2 " +
                "ON t1.`id` = t2.`id` " +
                "AND t1.`age` = t2.`student_id` " +
                "WHERE t1.`is_deleted` = ? " +
                "AND t1.`env` = ? " +
                "AND t1.`age` IS NULL " +
                "AND t2.`is_deleted` = ? " +
                "AND t2.`env` = ? " +
                "AND t2.`address` LIKE ? " +
                "GROUP BY t1.`age`, t1.`id`, t2.`student_id` " +
                "HAVING MAX(t1.`age`) > ? " +
                "LIMIT ?, ?");
        this.mapper.listMaps(query.build());
        IDatabase.db.sqlList().wantFirstSql().eq(
            "SELECT DISTINCT t1.`age`, t2.`student_id` " +
                "FROM fluent_mybatis.student t1 " +
                "LEFT JOIN `home_address` t2 " +
                "ON t1.`id` = t2.`id` " +
                "AND t1.`age` = t2.`student_id` " +
                "WHERE t1.`is_deleted` = ? " +
                "AND t1.`env` = ? " +
                "AND t1.`age` IS NULL " +
                "AND t2.`is_deleted` = ? " +
                "AND t2.`env` = ? " +
                "AND t2.`address` LIKE ? " +
                "GROUP BY t1.`age`, t1.`id`, t2.`student_id` " +
                "HAVING MAX(t1.`age`) > ? " +
                "LIMIT ?, ?");
        this.mapper.listMaps(query.build());
        IDatabase.db.sqlList().wantFirstSql().eq(
            "SELECT DISTINCT t1.`age`, t2.`student_id` " +
                "FROM fluent_mybatis.student t1 " +
                "LEFT JOIN `home_address` t2 " +
                "ON t1.`id` = t2.`id` " +
                "AND t1.`age` = t2.`student_id` " +
                "WHERE t1.`is_deleted` = ? " +
                "AND t1.`env` = ? " +
                "AND t1.`age` IS NULL " +
                "AND t2.`is_deleted` = ? " +
                "AND t2.`env` = ? " +
                "AND t2.`address` LIKE ? " +
                "GROUP BY t1.`age`, t1.`id`, t2.`student_id` " +
                "HAVING MAX(t1.`age`) > ? " +
                "LIMIT ?, ?");
        this.mapper.listMaps(query.build());
        IDatabase.db.sqlList().wantFirstSql().eq(
            "SELECT DISTINCT t1.`age`, t2.`student_id` " +
                "FROM fluent_mybatis.student t1 " +
                "LEFT JOIN `home_address` t2 " +
                "ON t1.`id` = t2.`id` " +
                "AND t1.`age` = t2.`student_id` " +
                "WHERE t1.`is_deleted` = ? " +
                "AND t1.`env` = ? " +
                "AND t1.`age` IS NULL " +
                "AND t2.`is_deleted` = ? " +
                "AND t2.`env` = ? " +
                "AND t2.`address` LIKE ? " +
                "GROUP BY t1.`age`, t1.`id`, t2.`student_id` " +
                "HAVING MAX(t1.`age`) > ? " +
                "LIMIT ?, ?");
    }

    @Test
    public void test_right_join() {
        IQuery query = StudentQuery.emptyQuery("t1")
            .where.isDeleted().eq(true)
            .and.age().isNull()
            .end()
            .join(JoinType.RightJoin, HomeAddressQuery.emptyQuery("t2")
                .where.isDeleted().eq(true)
                .and.address().like("vas")
                .end())
            .on(l -> l.where.id(), r -> r.where.id())
            .endJoin()
            .build();
        this.mapper.listMaps(query);
        IDatabase.db.sqlList().wantFirstSql()
            .end("FROM fluent_mybatis.student t1 RIGHT JOIN `home_address` t2 " +
                "ON t1.`id` = t2.`id` " +
                "WHERE t1.`is_deleted` = ? " +
                "AND t1.`age` IS NULL " +
                "AND t2.`is_deleted` = ? " +
                "AND t2.`address` LIKE ?");
    }

    @Test
    void three_join() {
        StudentQuery query1 = StudentQuery.emptyQuery("t1")
            .where.age().eq(3).end();
        HomeAddressQuery query2 = HomeAddressQuery.emptyQuery("t2")
            .where.address().like("xxx").end();
        StudentScoreQuery query3 = StudentScoreQuery.emptyQuery("t3")
            .where.subject().in(new String[]{"a", "b", "c"}).end();
        IQuery query = JoinBuilder
            .from(query1)
            .leftJoin(query2)
            .on(l -> l.where.homeAddressId(), r -> r.where.id()).endJoin()
            .leftJoin(query3)
            .on(l -> l.where.id(), r -> r.where.studentId()).endJoin()
            .build();
        this.mapper.listMaps(query);
        IDatabase.db.sqlList().wantFirstSql()
            .contains(new String[]{"t1.`id`", "t2.`id`", "t3.`id`"})
            .end("FROM fluent_mybatis.student t1 LEFT JOIN `home_address` t2 " +
                "ON t1.`home_address_id` = t2.`id` " +
                "LEFT JOIN `student_score` t3 ON t1.`id` = t3.`student_id` " +
                "WHERE t1.`age` = ? " +
                "AND t2.`address` LIKE ? " +
                "AND t3.`subject` IN (?, ?, ?)", StringMode.SameAsSpace);
    }
}
