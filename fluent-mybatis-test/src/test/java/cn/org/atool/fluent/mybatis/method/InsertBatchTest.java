package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class InsertBatchTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void testInsertBatch_withoutPk() {
        db.table(ATM.table.student).clean();
        List<StudentEntity> list = list(
            new StudentEntity().setUserName("name1").setAge(23).setTenant(0L),
            new StudentEntity().setUserName("name2").setAge(24).setTenant(0L));
        mapper.insertBatch(list);
        db.table(ATM.table.student).count().eq(2);
        db.table(ATM.table.student).query().print()
            .eqDataMap(ATM.dataMap.student.table(2)
                .age.values(23, 24)
                .userName.values("name1", "name2")
            );
        want.number(list.get(0).getId()).notNull();
        want.number(list.get(1).getId()).notNull();
    }

    @Test
    public void testInsertBatch_WithId() {
        db.table(ATM.table.student).clean();
        List<StudentEntity> list = list(
            new StudentEntity().setId(23L).setUserName("name1").setAge(23).setTenant(0L),
            new StudentEntity().setId(24L).setUserName("name2").setAge(24).setTenant(0L));
        mapper.insertBatchWithPk(list);
        db.table(ATM.table.student).count().eq(2);
        db.table(ATM.table.student).query().print()
            .eqDataMap(ATM.dataMap.student.table(2)
                .age.values(23, 24)
                .userName.values("name1", "name2")
            );
        want.array(list.stream().map(StudentEntity::getId).toArray())
            .eqReflect(new long[]{23, 24});
    }

    @DisplayName("部分id有值, 插入失败")
    @Test
    public void testInsertBatch() {
        db.table(ATM.table.student).clean();
        List<StudentEntity> list = list(
            new StudentEntity().setUserName("name1").setAge(23).setId(101L).setTenant(0L),
            new StudentEntity().setUserName("name2").setAge(24).setTenant(0L));
        want.exception(() -> mapper.insertBatch(list), FluentMybatisException.class, MyBatisSystemException.class)
            .contains("the pk of insert entity must be null.");
        db.table(ATM.table.student).count().eq(0);
    }
}
