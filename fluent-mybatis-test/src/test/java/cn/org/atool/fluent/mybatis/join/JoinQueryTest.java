package cn.org.atool.fluent.mybatis.join;

import cn.org.atool.fluent.mybatis.base.IJoinQuery;
import cn.org.atool.fluent.mybatis.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.AddressQuery;
import cn.org.atool.fluent.mybatis.generate.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.segment.JoinQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class JoinQueryTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void test_join() {
        IJoinQuery<UserQuery> query = JoinQuery
            .from(UserQuery.class, uq -> uq
                .select.age().end()
                .where.isDeleted().eq(true)
                .and.age().isNull()
                .end()
                .groupBy.age().apply("t1.id").end()
                .having.max.age().gt(1L).end()
                .orderBy.id().desc().end());
        query.join(AddressQuery.class
            , aq -> aq
                .select.userId().end()
                .where.isDeleted().eq(true)
                .and.address().like("vas")
                .end()
                .groupBy.userId().end()
                .orderBy.id().asc().end()
            , (join, q1, q2) -> join
                .on(q1.where.id(), q2.where.id())
                .on(q1.where.age(), q2.where.userId())
        );
        query.limit(20);
        mapper.listMaps(query.build());
        db.sqlList().wantFirstSql().eq("SELECT t1.age, t2.user_id " +
            "FROM t_user AS t1 " +
            "JOIN address AS t2 " +
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
        IJoinQuery<UserQuery> query = JoinQuery
            .from(UserQuery.class, uq -> uq
                .select.age().end()
                .where.isDeleted().eq(true)
                .and.age().isNull()
                .end()
                .groupBy.age().apply("t1.id").end()
                .having.max.age().gt(1L).end()
            );
        query.join(AddressQuery.class, aq -> aq
                .select.userId().end()
                .where.isDeleted().eq(true)
                .and.address().like("vas")
                .end()
                .groupBy.userId().end()
            , (join, q1, q2) -> join
                .on(q1.where.id(), q2.where.id())
                .on(q1.where.age(), q2.where.userId()));
        query.distinct().limit(20);
        mapper.listMaps(query.build());
        db.sqlList().wantFirstSql().eq("SELECT DISTINCT t1.age, t2.user_id " +
            "FROM t_user AS t1 " +
            "LEFT JOIN address AS t2 " +
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
        IJoinQuery<UserQuery> query = JoinQuery
            .from(UserQuery.class, uq -> uq
                .where.isDeleted().eq(true)
                .and.age().isNull()
                .end()
            );
        query.rightJoin(AddressQuery.class, aq -> aq
                .where.isDeleted().eq(true)
                .and.address().like("vas")
                .end()
            , (join, q1, q2) -> join
                .on(q1.where.id(), q2.where.id())
        );
        mapper.listMaps(query.build());
        db.sqlList().wantFirstSql().end("FROM t_user AS t1 RIGHT JOIN address AS t2 ON t1.id = t2.id WHERE t1.is_deleted = ? AND t1.age IS NULL AND t2.is_deleted = ? AND t2.address LIKE ?");
    }
}