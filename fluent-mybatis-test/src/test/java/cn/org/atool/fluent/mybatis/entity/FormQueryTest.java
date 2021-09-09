package cn.org.atool.fluent.mybatis.entity;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.model.Form;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.model.TagPagedList;
import cn.org.atool.fluent.mybatis.refs.FieldRef;
import cn.org.atool.fluent.mybatis.refs.FormRef;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("unused")
public class FormQueryTest extends BaseTest {
    @DisplayName("当总记录数为0时, 只执行count查询，不执行query查询")
    @Test
    public void testStdPaged() {
        ATM.dataMap.student.table().clean();
        StdPagedList<StudentEntity> paged = new Form()
            .add.eq(FieldRef.Student.userName, "xx")
            .add.between(FieldRef.Student.age, 12, 40)
            .setCurrPage(0)
            .to(StudentEntity.class).stdPagedEntity();

        db.sqlList().wantFirstSql().end("WHERE `is_deleted` = ? " +
            "AND `env` = ? " +
            "AND `user_name` = ? " +
            "AND `age` BETWEEN ? AND ?");
        want.number(db.sqlList().size()).eq(1);
    }

    @Test
    public void testTagPaged() {
        TagPagedList<StudentEntity> paged = new Form()
            .add.eq(FieldRef.Student.userName, "xx")
            .add.between(FieldRef.Student.age, 12, 40)
            .setNextId(0)
            .add(FormRef.student, new StudentEntity().setAddress("kkk"))
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
}
