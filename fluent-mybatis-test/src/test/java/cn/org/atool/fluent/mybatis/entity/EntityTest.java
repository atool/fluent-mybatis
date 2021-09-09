package cn.org.atool.fluent.mybatis.entity;

import cn.org.atool.fluent.mybatis.base.IRef;
import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.refs.Ref;
import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

import java.util.HashMap;
import java.util.Map;

public class EntityTest extends Test4J {
    @Test
    void copy() {
        StudentEntity student = new StudentEntity()
            .setUserName("fluent mybatis")
            .setAge(3)
            .setVersion("1.3.0")
            .copy();
        want.object(student).eqDataMap(ATM.dataMap.student.entity()
            .userName.values("fluent mybatis")
            .age.values(3)
            .version.values("1.3.0"));
    }

    @Test
    void toEntityMap() {
        Map<String, Object> student = new StudentEntity()
            .setUserName("fluent mybatis")
            .setAge(3)
            .setVersion("1.3.0")
            .toEntityMap();
        want.object(student).eqDataMap(ATM.dataMap.student.entity()
            .userName.values("fluent mybatis")
            .age.values(3)
            .version.values("1.3.0"));
    }

    @Test
    void toColumnMap() {
        Map<String, Object> student = new StudentEntity()
            .setUserName("fluent mybatis")
            .setAge(3)
            .setVersion("1.3.0")
            .toColumnMap();
        want.object(student).eqDataMap(ATM.dataMap.student.table()
            .userName.values("fluent mybatis")
            .age.values(3)
            .version.values("1.3.0"));
    }

    @Test
    void toEntity() {
        Map<String, Object> map = new HashMap<>();
        map.put("userName", "fluent mybatis");
        map.put("age", 3);
        map.put("version", "1.3.0");

        StudentEntity student = IRef.entityKit(StudentEntity.class).toEntity(map);
        want.object(student).eqDataMap(ATM.dataMap.student.entity()
            .userName.values("fluent mybatis")
            .age.values(3)
            .version.values("1.3.0"));
    }

    @Test
    void valueByField() {
        StudentEntity entity = new StudentEntity().setUserName("test1").setAddress("add_123");
        String userName = Ref.entityKit(StudentEntity.class).valueByField(entity, "userName");
        want.string(userName).eq("test1");
        String address = Ref.entityKit(StudentEntity.class).valueByField(entity, "address");
        want.string(address).eq("add_123");
        String age1 = Ref.entityKit(StudentEntity.class).valueByField(entity, "age1");
        want.string(age1).isNull();
    }

    @Test
    void valueByColumn() {
        StudentEntity entity = new StudentEntity().setUserName("test1").setAddress("add_123");
        String userName = Ref.entityKit(StudentEntity.class).valueByColumn(entity, "user_name");
        want.string(userName).eq("test1");
        String address = Ref.entityKit(StudentEntity.class).valueByColumn(entity, "address");
        want.string(address).eq("add_123");
        String age1 = Ref.entityKit(StudentEntity.class).valueByColumn(entity, "age1");
        want.string(age1).isNull();
    }
}
