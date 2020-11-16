package cn.org.atool.fluent.mybatis.entity;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.refs.FormRef;
import cn.org.atool.fluent.mybatis.model.IFormQuery;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

public class FormDemo extends BaseTest {
    @Test
    public void formDemo() {
        ATM.DataMap.student.table().clean();
        // 新增表单
        StudentEntity student = new StudentEntity()
            .setUserName("I am FluentMybatis")
            .setAge(2)
            .setAddress("宇宙深处");
        IFormQuery<StudentEntity, ?> query = FormRef.student.apply(student)
            .eq().userName()
            .eq().age()
            ;
        if (query.exists()) {
            throw new RuntimeException("出BUG了!");
        }
        student.save();
        want.bool(query.exists()).is(true);
        Stream.of(new Object[10]).forEach(o -> student.setId(null).save());

        StdPagedList<StudentEntity> list = query.limit(10).paged();
        want.list(list.getData()).eqDataMap(ATM.DataMap.student.entity(10)
            .userName.values("I am FluentMybatis")
        );
        db.table(ATM.Table.student).count().isEqualTo(11);
    }
}