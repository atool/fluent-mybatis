package cn.org.atool.fluent.mybatis.entity;

import cn.org.atool.fluent.mybatis.generate.Refs;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.refs.FieldRef;
import cn.org.atool.fluent.mybatis.generate.refs.FormRef;
import cn.org.atool.fluent.mybatis.model.Form;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.model.TagPagedList;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;

public class FormQueryTest extends BaseTest {
    @Test
    public void testStdPaged() {
        StdPagedList<StudentEntity> paged = new Form()
            .add.eq(FieldRef.Student.userName, "xx")
            .add.between(FieldRef.Student.age, 12, 40)
            .setCurrPage(0)
            .to(StudentEntity.class).stdPagedEntity();

        db.sqlList().wantFirstSql().end("WHERE is_deleted = ? " +
            "AND env = ? " +
            "AND user_name = ? " +
            "AND age BETWEEN ? AND ?");
        db.sqlList().wantSql(1).end("WHERE is_deleted = ? " +
            "AND env = ? " +
            "AND user_name = ? " +
            "AND age BETWEEN ? AND ? " +
            "LIMIT ?, ?");
    }

    @Test
    public void testTagPaged() {
        TagPagedList<StudentEntity> paged = new Form()
            .add.eq(Refs.Field.Student.userName, "xx")
            .add.between(Refs.Field.Student.age, 12, 40)
            .setNextId(0)
            .add(FormRef.student, new StudentEntity().setAddress("kkk"))
            .leftLike().address()
            .query().to().tagPagedEntity();

        db.sqlList().wantFirstSql().end("FROM student " +
            "WHERE is_deleted = ? " +
            "AND env = ? " +
            "AND user_name = ? " +
            "AND age BETWEEN ? AND ? " +
            "AND address LIKE ? " +
            "AND id >= ? " +
            "LIMIT ?, ?");
    }
}