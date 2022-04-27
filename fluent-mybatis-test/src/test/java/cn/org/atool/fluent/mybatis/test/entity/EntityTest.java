package cn.org.atool.fluent.mybatis.test.entity;

import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.utility.RefKit;
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

        StudentEntity student = RefKit.entityKit(StudentEntity.class).toEntity(map);
        want.object(student).eqDataMap(ATM.dataMap.student.entity()
            .userName.values("fluent mybatis")
            .age.values(3)
            .version.values("1.3.0"));
    }

    @Test
    void valueByField() {
        StudentEntity entity = new StudentEntity().setUserName("test1").setAddress("add_123");
        String userName = RefKit.entityKit(StudentEntity.class).valueByField(entity, "userName");
        want.string(userName).eq("test1");
        String address = entity.valueByField("address");
        want.string(address).eq("add_123");
        String age1 = entity.valueByField("age1");
        want.string(age1).isNull();
    }

    @Test
    void valueByColumn() {
        StudentEntity entity = new StudentEntity().setUserName("test1").setAddress("add_123");
        String userName = entity.valueByColumn("user_name");
        want.string(userName).eq("test1");
        String address = entity.valueByColumn("address");
        want.string(address).eq("add_123");
        String age1 = entity.valueByColumn("age1");
        want.string(age1).isNull();
    }
}
