package cn.org.atool.fluent.mybatis.test.partition;

import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;

public class InsertDynamicTable extends BaseTest {
    @Autowired
    StudentMapper mapper;

    @Test
    void insertSelect() {
        want.exception(() -> mapper.insertSelect(new String[]{"user_name"},
                StudentQuery.emptyQuery(() -> "my_student").where.id().eq(1L).end())
            , BadSqlGrammarException.class);
        db.sqlList().wantFirstSql().eq("" +
            "INSERT INTO `my_student` (`user_name`) " +
            "SELECT `user_name` " +
            "FROM `my_student` " +
            "WHERE `id` = ?");
    }

    @Test
    void insertBatch() {
        want.exception(() ->
            mapper.insertBatch(list(
                    new StudentEntity()
                        .setUserName("name")
                        .tableSupplier("my_student"),
                    new StudentEntity()
                        .setUserName("name")
                        .tableSupplier("my_student")
                )
            ), BadSqlGrammarException.class);
        db.sqlList().wantFirstSql().start("INSERT INTO my_student (`");
    }

    @Test
    void insert() {
        want.exception(() ->
            mapper.insert((StudentEntity) new StudentEntity()
                .setUserName("name")
                .tableSupplier("my_student")), BadSqlGrammarException.class);
        db.sqlList().wantFirstSql().start("INSERT INTO my_student (`");
    }

    @Test
    void insertWithPk() {
        want.exception(() ->
            mapper.insertWithPk(new StudentEntity()
                .setId(1L)
                .setUserName("name")
                .tableSupplier("my_student")
            ), BadSqlGrammarException.class);
        db.sqlList().wantFirstSql().start("INSERT INTO my_student (`id`,");
    }
}
