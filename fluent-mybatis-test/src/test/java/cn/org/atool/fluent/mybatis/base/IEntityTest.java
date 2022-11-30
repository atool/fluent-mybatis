package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.common.kits.KeyMap;
import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;
import org.test4j.tools.commons.DateHelper;

import static cn.org.atool.fluent.mybatis.generator.shared2.Ref.Field.Student;

class IEntityTest extends Test4J {

    @Test
    void valueByFields() {
        StudentEntity student = new StudentEntity().valueByFields(KeyMap.instance()
            .put(Student.address.name, "hangzhou")
            .put(Student.birthday.name, "2000-01-01")
            .put(Student.age.name, 20)
            .map());
        want.object(student).eqMap(ATM.dataMap.student.entity()
            .address.values("hangzhou")
            .birthday.values("2000-01-01")
            .age.values(20));
    }

    @Test
    void valueByColumns() {
        StudentEntity student = new StudentEntity().valueByColumns(KeyMap.instance()
            .put(Student.address.column, "hangzhou")
            .put(Student.birthday.column, "2000-01-01")
            .put(Student.age.column, 20)
            .map());
        want.object(student).eqMap(ATM.dataMap.student.entity()
            .address.values("hangzhou")
            .birthday.values("2000-01-01")
            .age.values(20));
    }

    @Test
    void copy() {
        StudentEntity student = new StudentEntity().copy(new StudentEntity()
            .setAddress("hangzhou")
            .setBirthday(DateHelper.parse("2000-01-01"))
            .setAge(20));
        want.object(student).eqMap(ATM.dataMap.student.entity()
            .address.values("hangzhou")
            .birthday.values("2000-01-01")
            .age.values(20));
    }
}