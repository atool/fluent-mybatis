package cn.org.atool.fluent.mybatis.test.entity;

import cn.org.atool.fluent.form.Form;
import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.model.TagPagedList;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static cn.org.atool.fluent.mybatis.generator.shared2.helper.StudentMapping.age;
import static cn.org.atool.fluent.mybatis.generator.shared2.helper.StudentMapping.userName;

@SuppressWarnings("unused")
public class FormQueryTest extends BaseTest {
    @DisplayName("当总记录数为0时, 只执行count查询，不执行query查询")
    @Test
    public void testStdPaged() {
        ATM.dataMap.student.table().clean();
        StdPagedList<StudentEntity> paged = new Form(StudentEntity.class)
            .and.eq(userName, "xx")
            .and.between(age, 12, 40)
            .orderBy(userName, false)
            .setCurrPage(0)
            .stdPage();

        db.sqlList().wantFirstSql().end("WHERE `is_deleted` = ? " +
            "AND `env` = ? " +
            "AND `user_name` = ? " +
            "AND `age` BETWEEN ? AND ?");
    }

    @DisplayName("使用字段硬编码")
    @Test
    public void testStdPaged_FieldName() {
        ATM.dataMap.student.table().clean();
        StdPagedList<StudentEntity> paged = new Form(StudentEntity.class)
            .and.eq("userName", "xx")
            .and.between("age", 12, 40)
            .setPage(1, 10)
            .stdPage();

        db.sqlList().wantFirstSql().end("WHERE `is_deleted` = ? " +
            "AND `env` = ? " +
            "AND `user_name` = ? " +
            "AND `age` BETWEEN ? AND ?");
    }

    @Test
    public void testTagPaged() {
        TagPagedList<StudentEntity> paged = new Form(StudentEntity.class)
            .and.eq(userName, "xx")
            .and.between(age, 12, 40)
            .setId(0)
            .apply(new StudentEntity().setAddress("kkk"), apply -> apply
                .likeLeft("address")
            )
            .orderBy(userName, false)
            .tagPage();

        db.sqlList().wantFirstSql().end("FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? " +
            "AND `env` = ? " +
            "AND `user_name` = ? " +
            "AND `age` BETWEEN ? AND ? " +
            "AND `address` LIKE ? " +
            "AND `id` >= ? " +
            "ORDER BY `user_name` DESC " +
            "LIMIT ?, ?");
    }

    @Test
    public void testTagPaged_Json() {
        TagPagedList<StudentEntity> paged = Form.with(StudentEntity.class, ("" +
                "{`where`: [" +
                "    {   `field`: `userName`," +
                "        `value`: [`xx`]" +
                "    }," +
                "    {   `field`: `age`," +
                "        `op`: `BETWEEN`," +
                "        `value`: [12, 40]" +
                "    }," +
                "    {   `field`: `birthday`" +
                "    }," +
                "    {   `field`: `address`," +
                "        `op`: `LIKE_LEFT`," +
                "        `value`: [`kkk`]" +
                "    }], " +
                " `order`: [{`field`: `userName`, `asc`: false}], " +
                " `id`: `0`" +
                "}").replace('`', '"'))
            .tagPage();

        db.sqlList().wantFirstSql().end("FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? " +
            "AND `env` = ? " +
            "AND `user_name` = ? " +
            "AND `age` BETWEEN ? AND ? " +
            "AND `address` LIKE ? " +
            "AND `id` >= ? " +
            "ORDER BY `user_name` DESC " +
            "LIMIT ?, ?");
    }
}
