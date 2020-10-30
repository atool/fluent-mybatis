package cn.org.atool.fluent.mybatis.join;

import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.base.JoinBuilder;
import cn.org.atool.fluent.mybatis.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.AddressQuery;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentScoreQuery;
import cn.org.atool.fluent.mybatis.generate.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class JoinQueryTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void test_join() {
        JoinBuilder<UserQuery> query = JoinBuilder
            .from(UserQuery.class, q -> q
                .select.age().end()
                .where.isDeleted().eq(true)
                .and.age().isNull()
                .end()
                .groupBy.age().apply("t1.id").end()
                .having.max.age().gt(1L).end()
                .orderBy.id().desc().end()
            ).join(AddressQuery.class, q -> q
                .select.userId().end()
                .where.isDeleted().eq(true)
                .and.address().like("vas")
                .end()
                .groupBy.userId().end()
                .orderBy.id().asc().end()
            )
            .on(l -> l.where.id(), r -> r.where.id())
            .on(l -> l.where.age(), r -> r.where.userId()).endJoin()
            .limit(20);
        mapper.listMaps(query.build());
        db.sqlList().wantFirstSql().eq(
            "SELECT t1.age, t2.user_id " +
                "FROM t_user t1 " +
                "JOIN address t2 " +
                "ON t1.id = t2.id " +
                "AND t1.age = t2.user_id " +
                "WHERE t1.is_deleted = ? " +
                "AND t1.age IS NULL " +
                "AND t2.is_deleted = ? " +
                "AND t2.address LIKE ? " +
                "GROUP BY t1.age, t1.id, t2.user_id " +
                "HAVING MAX(t1.age) > ? " +
                "ORDER BY t1.id DESC, t2.id ASC " +
                "LIMIT ?, ?");
    }

    @Test
    public void test_left_join() {
        JoinBuilder<UserQuery> query = JoinBuilder
            .from(UserQuery.class, uq -> uq
                .select.age().end()
                .where.isDeleted().eq(true)
                .and.age().isNull()
                .end()
                .groupBy.age().apply("t1.id").end()
                .having.max.age().gt(1L).end()
            ).leftJoin(AddressQuery.class, aq -> aq
                .select.userId().end()
                .where.isDeleted().eq(true)
                .and.address().like("vas")
                .end()
                .groupBy.userId().end()
            )
            .on(l -> l.where.id(), r -> r.where.id())
            .on(l -> l.where.age(), r -> r.where.userId()).endJoin()
            .distinct()
            .limit(20);
        mapper.listMaps(query.build());
        db.sqlList().wantFirstSql().eq(
            "SELECT DISTINCT t1.age, t2.user_id " +
                "FROM t_user t1 " +
                "LEFT JOIN address t2 " +
                "ON t1.id = t2.id " +
                "AND t1.age = t2.user_id " +
                "WHERE t1.is_deleted = ? " +
                "AND t1.age IS NULL " +
                "AND t2.is_deleted = ? " +
                "AND t2.address LIKE ? " +
                "GROUP BY t1.age, t1.id, t2.user_id " +
                "HAVING MAX(t1.age) > ? " +
                "LIMIT ?, ?");
    }

    @Test
    public void test_right_join() {
        JoinBuilder<UserQuery> query = JoinBuilder
            .from(UserQuery.class, uq -> uq
                .where.isDeleted().eq(true)
                .and.age().isNull()
                .end()
            ).rightJoin(AddressQuery.class, aq -> aq
                .where.isDeleted().eq(true)
                .and.address().like("vas")
                .end())
            .on(l -> l.where.id(), r -> r.where.id())
            .endJoin();
        mapper.listMaps(query.build());
        db.sqlList().wantFirstSql()
            .end("FROM t_user t1 RIGHT JOIN address t2 " +
                "ON t1.id = t2.id " +
                "WHERE t1.is_deleted = ? " +
                "AND t1.age IS NULL " +
                "AND t2.is_deleted = ? " +
                "AND t2.address LIKE ?");
    }

    @Test
    void three_join() {
        IQuery query = JoinBuilder.from(UserQuery.class,
            q -> q
                .where.age().eq(3).end())
            .leftJoin(AddressQuery.class, q -> q
                .where.address().like("xxx").end())
            .on(l -> l.where.addressId(), r -> r
                .where.id()).endJoin()
            .leftJoin(StudentScoreQuery.class, q -> q
                .where.subject().in(new String[]{"a", "b", "c"}).end())
            .on(l -> l.where.id(), r -> r.where.studentId()).endJoin()
            .build();
        mapper.listMaps(query);
        db.sqlList().wantFirstSql()
            .contains(new String[]{"t1.id", "t2.id", "t3.id"})
            .end("FROM t_user t1 LEFT JOIN address t2 " +
                "ON t1.address_id = t2.id " +
                "LEFT JOIN student_score t3 ON t1.id = t3.student_id " +
                "WHERE t1.age = ? " +
                "AND t2.address LIKE ? " +
                "AND t3.subject IN (?, ?, ?)");
    }
}