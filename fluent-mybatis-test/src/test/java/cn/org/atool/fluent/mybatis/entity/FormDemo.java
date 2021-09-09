package cn.org.atool.fluent.mybatis.entity;

import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.refs.FormRef;
import cn.org.atool.fluent.mybatis.refs.Ref;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

public class FormDemo extends BaseTest {
    @Test
    public void formDemo1() {
        ATM.dataMap.student.table().clean();
        StudentEntity student = new StudentEntity()
            .setEnv("test_env")
            .setIsDeleted(false)
            .setUserName("I am FluentMybatis")
            .setAge(2)
            .setAddress("宇宙深处");

        IQuery<StudentEntity> query = Ref.Forms.student.with(student)
            .eq().userName()
            .eq().age()
            .query();
        if (query.to().count() > 0) {
            throw new RuntimeException("出BUG了!");
        }
        student.save();
        want.bool(query.to().count() > 0).is(true);
        Stream.of(new Object[10]).forEach(o -> student.setId(null).save());

        StdPagedList<StudentEntity> list = query
            .limit(10)
            .to().stdPagedEntity();

        want.list(list.getData()).eqDataMap(ATM.dataMap.student.entity(10)
            .userName.values("I am FluentMybatis")
        );
        db.table(ATM.table.student).count().isEqualTo(11);
    }

    @Test
    public void formDemo2() {
        ATM.dataMap.student.table().clean();
        // 新增表单
        StudentEntity student = new StudentEntity()
            .setUserName("I am FluentMybatis")
            .setAge(2)
            .setAddress("宇宙深处");

        List<StudentEntity> students = FormRef.student.with(student)
            .eq(StudentEntity::getUserName)
            .like(StudentEntity::getAddress)
            .query().to().listEntity();
        want.list(students).sizeEq(0);

        IQuery<StudentEntity> query = FormRef.student.with(student)
            .eq().userName()
            .eq().age()
            .query();
        if (query.to().count() > 0) {
            throw new RuntimeException("出BUG了!");
        }
        student.save();
        want.number(query.to().count()).eq(1);
        Stream.of(new Object[10]).forEach(o -> student.setId(null).save());

        StdPagedList<StudentEntity> list = query
            .limit(5)
            .to().stdPagedEntity();

        want.list(list.getData()).eqDataMap(ATM.dataMap.student.entity(5)
            .userName.values("I am FluentMybatis")
        );
        db.table(ATM.table.student).count().isEqualTo(11);
    }
}
