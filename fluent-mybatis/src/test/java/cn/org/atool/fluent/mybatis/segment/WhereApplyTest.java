package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

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
            .where
            .age().isNull(true)
            .userName().isNull(false)
            .end()
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
            .where
            .age().isNotNull(true)
            .userName().isNotNull(false)
            .end()
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
            .where
            .age().eq_IfNotNull(34)
            .userName().eq_IfNotNull(null)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age = ?");
    }

    @Test
    void ne() {
        mapper.listEntity(new UserQuery()
            .where
            .age().ne(34)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age <> ?");
    }

    @Test
    void ne_IfNotNull() {
        mapper.listEntity(new UserQuery()
            .where
            .age().ne_IfNotNull(null)
            .userName().ne_IfNotNull("")
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name <> ?");
    }

    @Test
    void gt() {
        mapper.listEntity(new UserQuery()
            .where
            .age().gt(34)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age > ?");
    }

    @Test
    void gt_IfNotNull() {
        mapper.listEntity(new UserQuery()
            .where
            .age().gt_IfNotNull(34)
            .version().gt_IfNotNull(null)
            .end()
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
            .age().ge_IfNotNull(34)
            .userName().ge_IfNotNull(null)
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
            .age().lt_IfNotNull(34)
            .userName().lt_IfNotNull(null)
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
            .age().le_IfNotNull(34)
            .userName().le_IfNotNull(null)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age <= ?");
    }

    @Test
    void between() {
        mapper.listEntity(new UserQuery()
            .where
            .age().between(true, 34, 45)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age BETWEEN ? AND ?");
    }

    @Test
    void notBetween() {
        mapper.listEntity(new UserQuery()
            .where
            .age().notBetween(true, 34, 45)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age NOT BETWEEN ? AND ?");
    }

    @Test
    void eq_IfNotBlank() {
        mapper.listEntity(new UserQuery()
            .where
            .userName().eq_IfNotBlank("abc")
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name = ?");
    }

    @Test
    void ne_IfNotBlank() {
        mapper.listEntity(new UserQuery()
            .where
            .userName().ne_IfNotBlank("abc")
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name <> ?");
    }

    @Test
    void gt_IfNotBlank() {
        mapper.listEntity(new UserQuery()
            .where
            .userName().gt_IfNotBlank("abc")
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name > ?");
    }

    @Test
    void ge_IfNotBlank() {
        mapper.listEntity(new UserQuery()
            .where
            .userName().ge_IfNotBlank("abc")
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name >= ?");
    }

    @Test
    void lt_IfNotBlank() {
        mapper.listEntity(new UserQuery()
            .where
            .userName().lt_IfNotBlank("abc")
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name < ?");
    }

    @Test
    void le_IfNotBlank() {
        mapper.listEntity(new UserQuery()
            .where
            .userName().le_IfNotBlank("abc")
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
            .userName().like(true, "abc")
            .userName().like(false, "abc")
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name LIKE ?");
    }

    @Test
    void like_IfNotBlank() {
        mapper.listEntity(new UserQuery()
            .where
            .userName().like_IfNotBlank("abc")
            .userName().like_IfNotBlank(" ")
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
            .userName().notLike(true, "abc")
            .userName().notLike(false, "abc")
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE user_name NOT LIKE ?");
    }

    @Test
    void notLike_IfNotBlank() {
        mapper.listEntity(new UserQuery()
            .where
            .userName().notLike_IfNotBlank("abc")
            .userName().notLike_IfNotBlank(" ")
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
            .userName().likeLeft_IfNotBlank("abc")
            .userName().likeLeft_IfNotBlank(" ")
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
            .userName().likeRight_IfNotBlank("abc")
            .userName().likeRight_IfNotBlank(" ")
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