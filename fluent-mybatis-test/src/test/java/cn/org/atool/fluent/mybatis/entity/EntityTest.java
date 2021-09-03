package cn.org.atool.fluent.mybatis.entity;

import cn.org.atool.fluent.mybatis.base.IRef;
import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
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

        StudentEntity student = IRef.findEntityHelper(StudentEntity.class).toEntity(map);
        want.object(student).eqDataMap(ATM.dataMap.student.entity()
            .userName.values("fluent mybatis")
            .age.values(3)
            .version.values("1.3.0"));
    }
}