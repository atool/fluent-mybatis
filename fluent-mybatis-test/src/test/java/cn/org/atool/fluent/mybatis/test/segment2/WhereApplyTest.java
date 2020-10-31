package cn.org.atool.fluent.mybatis.test.segment2;

import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import cn.org.atool.fluent.mybatis.If;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

class WhereApplyTest extends BaseTest {

    @Autowired
    private StudentMapper mapper;

    @Test
    void apply() {
        mapper.listEntity(new StudentQuery()
            .where.age().apply("=1").end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age =1");
    }

    @Test
    void isNull() {
        mapper.listEntity(new StudentQuery()
            .where.age().isNull().end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age IS NULL");
    }

    @Test
    void testIsNull() {
        mapper.listEntity(new StudentQuery()
            .where.age().isNull(true)
            .and.userName().isNull(false).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age IS NULL");
    }

    @Test
    void isNotNull() {
        mapper.listEntity(new StudentQuery()
            .where.age().notNull().end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age IS NOT NULL");
    }

    @Test
    void testIsNotNull() {
        mapper.listEntity(new StudentQuery()
            .where.age().notNull(true)
            .and.userName().notNull(false).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age IS NOT NULL");
    }

    @Test
    void eq() {
        mapper.listEntity(new StudentQuery()
            .where.age().eq(34).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age = ?");
    }

    @Test
    void eq_IfNotNull() {
        mapper.listEntity(new StudentQuery()
            .where.age().eq(34, o -> o != null)
            .and.userName().eq(null, Objects::nonNull).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age = ?");
    }

    @Test
    void ne() {
        mapper.listEntity(new StudentQuery()
            .where.age().ne(34).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age <> ?");
    }

    @Test
    void ne_IfNotNull() {
        mapper.listEntity(new StudentQuery()
            .where.age().ne(null, Objects::nonNull)
            .and.userName().ne("", Objects::nonNull).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name <> ?");
    }

    @Test
    void gt() {
        mapper.listEntity(new StudentQuery()
            .where.age().gt(34).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age > ?");
    }

    @Test
    void gt_IfNotNull() {
        mapper.listEntity(new StudentQuery()
            .where.age().gt(34, Objects::nonNull)
            .and.version().eq(null, Objects::nonNull).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age > ?");
    }

    @Test
    void ge() {
        mapper.listEntity(new StudentQuery()
            .where
            .age().ge(34)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age >= ?");
    }

    @Test
    void ge_IfNotNull() {
        mapper.listEntity(new StudentQuery()
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
        mapper.listEntity(new StudentQuery()
            .where
            .age().lt(34)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age < ?");
    }

    @Test
    void lt_IfNotNull() {
        mapper.listEntity(new StudentQuery()
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
        mapper.listEntity(new StudentQuery()
            .where
            .age().le(34)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age <= ?");
    }

    @Test
    void le_IfNotNull() {
        mapper.listEntity(new StudentQuery()
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
        mapper.listEntity(new StudentQuery()
            .where
            .age().between(34, 45, (v1, v2) -> true)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age BETWEEN ? AND ?");
    }

    @Test
    void notBetween() {
        mapper.listEntity(new StudentQuery()
            .where
            .age().notBetween(34, 45, (v1, v2) -> true)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age NOT BETWEEN ? AND ?");
    }

    @Test
    void eq_IfNotBlank() {
        mapper.listEntity(new StudentQuery()
            .where
            .userName().eq("abc", If::notBlank)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name = ?");
    }

    @Test
    void ne_IfNotBlank() {
        mapper.listEntity(new StudentQuery()
            .where
            .userName().ne("abc", If::notBlank)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name <> ?");
    }

    @Test
    void gt_IfNotBlank() {
        mapper.listEntity(new StudentQuery()
            .where
            .userName().gt("abc", If::notBlank)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name > ?");
    }

    @Test
    void ge_IfNotBlank() {
        mapper.listEntity(new StudentQuery()
            .where
            .userName().ge("abc", If::notBlank)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name >= ?");
    }

    @Test
    void lt_IfNotBlank() {
        mapper.listEntity(new StudentQuery()
            .where
            .userName().lt("abc", If::notBlank)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name < ?");
    }

    @Test
    void le_IfNotBlank() {
        mapper.listEntity(new StudentQuery()
            .where
            .userName().le("abc", If::notBlank)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name <= ?");
    }

    @Test
    void like() {
        mapper.listEntity(new StudentQuery()
            .where
            .userName().like("abc")
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name LIKE ?");
    }

    @Test
    void testLike() {
        mapper.listEntity(new StudentQuery()
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
        mapper.listEntity(new StudentQuery()
            .where
            .userName().like("abc", If::notBlank)
            .userName().like(" ", If::notBlank)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name LIKE ?");
    }

    @Test
    void notLike() {
        mapper.listEntity(new StudentQuery()
            .where
            .userName().notLike("abc")
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name NOT LIKE ?");
    }

    @Test
    void testNotLike() {
        mapper.listEntity(new StudentQuery()
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
        mapper.listEntity(new StudentQuery()
            .where
            .userName().notLike("abc", If::notBlank)
            .userName().notLike(" ", If::notBlank)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name NOT LIKE ?");
    }

    @Test
    void likeLeft() {
        mapper.listEntity(new StudentQuery()
            .where
            .userName().likeLeft("abc")
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name LIKE ?");
    }


    @Test
    void likeLeft_IfNotBlank() {
        mapper.listEntity(new StudentQuery()
            .where
            .userName().likeLeft("abc", If::notBlank)
            .userName().likeLeft(" ", If::notBlank)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name LIKE ?");
    }

    @Test
    void likeRight() {
        mapper.listEntity(new StudentQuery()
            .where
            .userName().likeRight("abc")
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name LIKE ?");
    }

    @Test
    void likeRight_IfNotBlank() {
        mapper.listEntity(new StudentQuery()
            .where
            .userName().likeRight("abc", If::notBlank)
            .userName().likeRight(" ", If::notBlank)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name LIKE ?");
    }

    @Test
    void testApply() {
        mapper.listEntity(new StudentQuery()
            .where
            .userName().apply(SqlOp.EQ, "abc'")
            .age().apply(true, SqlOp.LT, 12)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name = ? AND age < ?");
    }
}
