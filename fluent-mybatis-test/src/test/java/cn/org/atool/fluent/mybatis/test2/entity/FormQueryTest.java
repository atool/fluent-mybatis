package cn.org.atool.fluent.mybatis.test2.entity;

import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.model.Form;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.model.TagPagedList;
import cn.org.atool.fluent.mybatis.test1.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static cn.org.atool.fluent.mybatis.generator.shared2.Ref.Forms.student;
import static cn.org.atool.fluent.mybatis.generator.shared2.helper.StudentMapping.age;
import static cn.org.atool.fluent.mybatis.generator.shared2.helper.StudentMapping.userName;

@SuppressWarnings("unused")
public class FormQueryTest extends BaseTest {
    @DisplayName("当总记录数为0时, 只执行count查询，不执行query查询")
    @Test
    public void testStdPaged() {
        ATM.dataMap.student.table().clean();
        StdPagedList<StudentEntity> paged = new Form()
            .add.eq(userName, "xx")
            .add.between(age, 12, 40)
            .setCurrPage(0)
            .to(StudentEntity.class).stdPagedEntity();

        db.sqlList().wantFirstSql().end("WHERE `is_deleted` = ? " +
            "AND `env` = ? " +
            "AND `user_name` = ? " +
            "AND `age` BETWEEN ? AND ?");
    }

    @Test
    public void testTagPaged() {
        TagPagedList<StudentEntity> paged = new Form()
            .add.eq(userName, "xx")
            .add.between(age, 12, 40)
            .setNextId("0")
            .add(student, new StudentEntity().setAddress("kkk"))
            .likeLeft().address()
            .query().to().tagPagedEntity();

        db.sqlList().wantFirstSql().end("FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? " +
            "AND `env` = ? " +
            "AND `user_name` = ? " +
            "AND `age` BETWEEN ? AND ? " +
            "AND `address` LIKE ? " +
            "AND `id` >= ? " +
            "LIMIT ?, ?");
    }

    @Test
    public void testTagPaged_Json() {
        TagPagedList<StudentEntity> paged = Form.with(StudentEntity.class, ("" +
                "{`items`:" +
                "   [{   `key`: `userName`," +
                "        `value`: [`xx`]" +
                "    }," +
                "    {   `key`: `age`," +
                "        `op`: `BETWEEN`," +
                "        `value`: [12, 40]" +
                "    }," +
                "    {   `key`: `address`," +
                "        `op`: `LEFT_LIKE`," +
                "        `value`: [`kkk`]" +
                "    }], " +
                "`nextId`:`0`}").replace('`', '"'))
            .query().to().tagPagedEntity();

        db.sqlList().wantFirstSql().end("FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? " +
            "AND `env` = ? " +
            "AND `user_name` = ? " +
            "AND `age` BETWEEN ? AND ? " +
            "AND `address` LIKE ? " +
            "AND `id` >= ? " +
            "LIMIT ?, ?");
    }
}