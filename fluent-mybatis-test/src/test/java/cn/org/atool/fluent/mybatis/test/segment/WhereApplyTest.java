package cn.org.atool.fluent.mybatis.test.segment;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

class WhereApplyTest extends BaseTest {

    @Autowired
    private StudentMapper mapper;

    @Test
    void apply() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where.age().apply("=1").end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `age` =1");
    }

    @Test
    void isNull() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where.age().isNull().end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `age` IS NULL");
    }

    @Test
    void testIsNull() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where.age().isNull(true)
            .and.userName().isNull(false).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `age` IS NULL");
    }

    @Test
    void isNotNull() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where.age().notNull().end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `age` IS NOT NULL");
    }

    @Test
    void testIsNotNull() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where.age().notNull(true)
            .and.userName().notNull(false).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `age` IS NOT NULL");
    }

    @Test
    void eq() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where.age().eq(34).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `age` = ?");
    }

    @Test
    void eq_IfNotNull() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where.age().eq(34, Objects::nonNull)
            .and.userName().eq(null, Objects::nonNull).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `age` = ?");
    }

    @Test
    void ne() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where.age().ne(34).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `age` <> ?");
    }

    @Test
    void ne_IfNotNull() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where.age().ne(null, Objects::nonNull)
            .and.userName().ne("", Objects::nonNull).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `user_name` <> ?");
    }

    @Test
    void gt() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where.age().gt(34).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `age` > ?");
    }

    @Test
    void gt_IfNotNull() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where.age().gt(34, Objects::nonNull)
            .and.version().eq(null, Objects::nonNull).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `age` > ?");
    }

    @Test
    void ge() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where
            .age().ge(34)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `age` >= ?");
    }

    @Test
    void ge_IfNotNull() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where
            .age().ge(34, Objects::nonNull)
            .userName().eq(null, Objects::nonNull)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `age` >= ?");
    }

    @Test
    void lt() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where
            .age().lt(34)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `age` < ?");
    }

    @Test
    void lt_IfNotNull() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where
            .age().lt(34, Objects::nonNull)
            .userName().eq(null, Objects::nonNull)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `age` < ?");
    }

    @Test
    void le() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where
            .age().le(34)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `age` <= ?");
    }

    @Test
    void le_IfNotNull() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where
            .age().le(34, Objects::nonNull)
            .userName().eq(null, Objects::nonNull)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `age` <= ?");
    }

    @Test
    void between() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where
            .age().between(34, 45, (v1, v2) -> true)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `age` BETWEEN ? AND ?");
    }

    @Test
    void notBetween() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where
            .age().notBetween(34, 45, (v1, v2) -> true)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `age` NOT BETWEEN ? AND ?");
    }

    @Test
    void eq_IfNotBlank() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where
            .userName().eq("abc", If::notBlank)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `user_name` = ?");
    }

    @Test
    void ne_IfNotBlank() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where
            .userName().ne("abc", If::notBlank)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `user_name` <> ?");
    }

    @Test
    void gt_IfNotBlank() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where
            .userName().gt("abc", If::notBlank)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `user_name` > ?");
    }

    @Test
    void ge_IfNotBlank() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where
            .userName().ge("abc", If::notBlank)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `user_name` >= ?");
    }

    @Test
    void lt_IfNotBlank() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where
            .userName().lt("abc", If::notBlank)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `user_name` < ?");
    }

    @Test
    void le_IfNotBlank() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where
            .userName().le("abc", If::notBlank)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `user_name` <= ?");
    }

    @Test
    void like() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where
            .userName().like("abc")
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `user_name` LIKE ?");
    }

    @Test
    void testLike() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where
            .userName().like("abc", o -> true)
            .userName().like("abc", o -> false)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `user_name` LIKE ?");
    }

    @Test
    void like_IfNotBlank() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where
            .userName().like("abc", If::notBlank)
            .userName().like(" ", If::notBlank)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `user_name` LIKE ?");
    }

    @Test
    void notLike() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where
            .userName().notLike("abc")
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `user_name` NOT LIKE ?");
    }

    @Test
    void testNotLike() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where
            .userName().notLike("abc", o -> true)
            .userName().notLike("abc", o -> false)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `user_name` NOT LIKE ?");
    }

    @Test
    void notLike_IfNotBlank() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where
            .userName().notLike("abc", If::notBlank)
            .userName().notLike(" ", If::notBlank)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `user_name` NOT LIKE ?");
    }

    @Test
    void likeLeft() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where
            .userName().startWith("abc")
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `user_name` LIKE ?");
    }


    @Test
    void likeLeft_IfNotBlank() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where
            .userName().startWith("abc", true)
            .userName().startWith(" ", If::notBlank)
            .end()
        );
        db.sqlList().wantFirstSql().end("WHERE `user_name` LIKE ?");
        db.sqlList().wantFirstPara().eqList("abc%");
    }

    @Test
    void likeRight() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where
            .userName().endWith("abc")
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `user_name` LIKE ?");
    }

    @Test
    void likeRight_IfNotBlank() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where
            .userName().endWith("abc", If::notBlank)
            .userName().endWith(" ", If::notBlank)
            .userName().endWith("xyz", false)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `user_name` LIKE ?");
        db.sqlList().wantFirstPara().eqList("%abc");
    }

    @Test
    void testApply2() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where
            .userName().apply(SqlOp.EQ, "abc'")
            .age().apply(args -> (int) args[0] == 12, SqlOp.LT, 12)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `user_name` = ? AND `age` < ?");
    }

    @Test
    void testApply() {
        mapper.listEntity(StudentQuery.emptyQuery()
            .where
            .userName().apply(SqlOp.EQ, "abc'")
            .age().apply(SqlOp.BETWEEN, 12, 50)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `user_name` = ? " +
                "AND `age` BETWEEN ? AND ?");
    }
}
