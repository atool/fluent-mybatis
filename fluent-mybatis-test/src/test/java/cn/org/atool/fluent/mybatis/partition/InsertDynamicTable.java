package cn.org.atool.fluent.mybatis.partition;

import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;

@SuppressWarnings("unchecked")
public class InsertDynamicTable extends BaseTest {
    @Autowired
    StudentMapper mapper;

    @Test
    void insertSelect() {
        want.exception(() -> mapper.insertSelect(new String[]{"user_name"},
            new StudentQuery(() -> "my_student", null).where.id().eq(1L).end())
            , BadSqlGrammarException.class);
        db.sqlList().wantFirstSql().eq("" +
            "INSERT INTO my_student (`user_name`) " +
            "SELECT `user_name` " +
            "FROM my_student " +
            "WHERE `id` = ?");
    }

    @Test
    void insertBatch() {
        want.exception(() ->
            mapper.insertBatch(list(
                new StudentEntity()
                    //.changeTableBelongTo("my_student")
                    .setUserName("name"), new StudentEntity()
                    .changeTableBelongTo("my_student")
                    //.setUserName("name")
                )
            ), BadSqlGrammarException.class);
        db.sqlList().wantFirstSql().start("INSERT INTO my_student(`");
    }

    @Test
    void insert() {
        want.exception(() ->
            mapper.insert(new StudentEntity()
                //.changeTableBelongTo("my_student")
                .setUserName("name")
            ), BadSqlGrammarException.class);
        db.sqlList().wantFirstSql().start("INSERT INTO my_student(`");
    }

    @Test
    void insertWithPk() {
        want.exception(() ->
            mapper.insertWithPk(new StudentEntity()
                //.changeTableBelongTo("my_student")
                .setId(1L)
                .setUserName("name")
            ), BadSqlGrammarException.class);
        db.sqlList().wantFirstSql().start("INSERT INTO my_student(`id`,");
    }
}