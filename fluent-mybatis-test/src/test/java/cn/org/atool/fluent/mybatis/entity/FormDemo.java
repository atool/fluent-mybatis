package cn.org.atool.fluent.mybatis.entity;

import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.generate.Refs;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.model.Form;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;

public class FormDemo extends BaseTest {
    @Test
    public void formDemo() {
        // 新增表单
        StudentEntity student = new StudentEntity()
            .setUserName("user1")
            .setAge(30)
            .setAddress("宇宙深处");
        IQuery query = Form.by(Refs.Setter.student, student)
            .eq().userName()
            .eq().age();
        if (student.existsBy(query)) {
            throw new RuntimeException("xxx");
        }
        student.save();
        if (student.existsBy(query)) {
            throw new RuntimeException("xxx");
        }
    }
}