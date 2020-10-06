package cn.org.atool.fluent.mybatis.test.segment2;

import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.generate.entity.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.generate.entity.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import cn.org.atool.fluent.mybatis.utility.Predicates;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

class WhereApplyTest extends BaseTest {

    @Autowired
    private UserMapper mapper;

    @Test
    void apply() {
        mapper.listEntity(new UserQuery()
            .where.age().apply("=1").end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age =1");
    }

    @Test
    void isNull() {
        mapper.listEntity(new UserQuery()
            .where.age().isNull().end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age IS NULL");
    }

    @Test
    void testIsNull() {
        mapper.listEntity(new UserQuery()
            .where.age().isNull(true)
            .and.userName().isNull(false).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age IS NULL");
    }

    @Test
    void isNotNull() {
        mapper.listEntity(new UserQuery()
            .where.age().isNotNull().end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age IS NOT NULL");
    }

    @Test
    void testIsNotNull() {
        mapper.listEntity(new UserQuery()
            .where.age().isNotNull(true)
            .and.userName().isNotNull(false).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age IS NOT NULL");
    }

    @Test
    void eq() {
        mapper.listEntity(new UserQuery()
            .where.age().eq(34).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age = ?");
    }

    @Test
    void eq_IfNotNull() {
        mapper.listEntity(new UserQuery()
            .where.age().eq(34, o -> o != null)
            .and.userName().eq(null, Objects::nonNull).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age = ?");
    }

    @Test
    void ne() {
        mapper.listEntity(new UserQuery()
            .where.age().ne(34).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age <> ?");
    }

    @Test
    void ne_IfNotNull() {
        mapper.listEntity(new UserQuery()
            .where.age().ne(null, Objects::nonNull)
            .and.userName().ne("", Objects::nonNull).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name <> ?");
    }

    @Test
    void gt() {
        mapper.listEntity(new UserQuery()
            .where.age().gt(34).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age > ?");
    }

    @Test
    void gt_IfNotNull() {
        mapper.listEntity(new UserQuery()
            .where.age().gt(34, Objects::nonNull)
            .and.version().eq(null, Objects::nonNull).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age > ?");
    }

    @Test
    void ge() {
        mapper.listEntity(new UserQuery()
            .where
            .age().ge(34)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age >= ?");
    }

    @Test
    void ge_IfNotNull() {
        mapper.listEntity(new UserQuery()
            .where
            .age().ge(34, Objects::nonNull)
            .userName().eq(null, Objects::nonNull)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age >= ?");
    }

    @Test
    void lt() {
        mapper.listEntity(new UserQuery()
            .where
            .age().lt(34)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age < ?");
    }

    @Test
    void lt_IfNotNull() {
        mapper.listEntity(new UserQuery()
            .where
            .age().lt(34, Objects::nonNull)
            .userName().eq(null, Objects::nonNull)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age < ?");
    }

    @Test
    void le() {
        mapper.listEntity(new UserQuery()
            .where
            .age().le(34)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age <= ?");
    }

    @Test
    void le_IfNotNull() {
        mapper.listEntity(new UserQuery()
            .where
            .age().le(34, Objects::nonNull)
            .userName().eq(null, Objects::nonNull)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age <= ?");
    }

    @Test
    void between() {
        mapper.listEntity(new UserQuery()
            .where
            .age().between(34, 45, (v1, v2) -> true)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age BETWEEN ? AND ?");
    }

    @Test
    void notBetween() {
        mapper.listEntity(new UserQuery()
            .where
            .age().notBetween(34, 45, (v1, v2) -> true)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age NOT BETWEEN ? AND ?");
    }

    @Test
    void eq_IfNotBlank() {
        mapper.listEntity(new UserQuery()
            .where
            .userName().eq("abc", Predicates::notBlank)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name = ?");
    }

    @Test
    void ne_IfNotBlank() {
        mapper.listEntity(new UserQuery()
            .where
            .userName().ne("abc", Predicates::notBlank)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name <> ?");
    }

    @Test
    void gt_IfNotBlank() {
        mapper.listEntity(new UserQuery()
            .where
            .userName().gt("abc", Predicates::notBlank)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name > ?");
    }

    @Test
    void ge_IfNotBlank() {
        mapper.listEntity(new UserQuery()
            .where
            .userName().ge("abc", Predicates::notBlank)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name >= ?");
    }

    @Test
    void lt_IfNotBlank() {
        mapper.listEntity(new UserQuery()
            .where
            .userName().lt("abc", Predicates::notBlank)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name < ?");
    }

    @Test
    void le_IfNotBlank() {
        mapper.listEntity(new UserQuery()
            .where
            .userName().le("abc", Predicates::notBlank)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name <= ?");
    }

    @Test
    void like() {
        mapper.listEntity(new UserQuery()
            .where
            .userName().like("abc")
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name LIKE ?");
    }

    @Test
    void testLike() {
        mapper.listEntity(new UserQuery()
            .where
            .userName().like("abc", o -> true)
            .userName().like("abc", o -> false)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name LIKE ?");
    }

    @Test
    void like_IfNotBlank() {
        mapper.listEntity(new UserQuery()
            .where
            .userName().like("abc", Predicates::notBlank)
            .userName().like(" ", Predicates::notBlank)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name LIKE ?");
    }

    @Test
    void notLike() {
        mapper.listEntity(new UserQuery()
            .where
            .userName().notLike("abc")
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name NOT LIKE ?");
    }

    @Test
    void testNotLike() {
        mapper.listEntity(new UserQuery()
            .where
            .userName().notLike("abc", o -> true)
            .userName().notLike("abc", o -> false)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name NOT LIKE ?");
    }

    @Test
    void notLike_IfNotBlank() {
        mapper.listEntity(new UserQuery()
            .where
            .userName().notLike("abc", Predicates::notBlank)
            .userName().notLike(" ", Predicates::notBlank)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name NOT LIKE ?");
    }

    @Test
    void likeLeft() {
        mapper.listEntity(new UserQuery()
            .where
            .userName().likeLeft("abc")
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name LIKE ?");
    }


    @Test
    void likeLeft_IfNotBlank() {
        mapper.listEntity(new UserQuery()
            .where
            .userName().likeLeft("abc", Predicates::notBlank)
            .userName().likeLeft(" ", Predicates::notBlank)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name LIKE ?");
    }

    @Test
    void likeRight() {
        mapper.listEntity(new UserQuery()
            .where
            .userName().likeRight("abc")
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name LIKE ?");
    }

    @Test
    void likeRight_IfNotBlank() {
        mapper.listEntity(new UserQuery()
            .where
            .userName().likeRight("abc", Predicates::notBlank)
            .userName().likeRight(" ", Predicates::notBlank)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name LIKE ?");
    }

    @Test
    void testApply() {
        mapper.listEntity(new UserQuery()
            .where
            .userName().apply(SqlOp.EQ, "abc'")
            .age().apply(true, SqlOp.LT, 12)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name = ? AND age < ?");
    }
}