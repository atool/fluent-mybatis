package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SuppressWarnings("unchecked")
public class InsertBatchTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void testInsertBatch_withoutPk() {
        db.table(ATM.table.student).clean();
        List<StudentEntity> list = list(
            new StudentEntity().setUserName("${G_commodityCodeSub}").setAge(23).setTenant(0L),
            new StudentEntity().setUserName("#{G_commodityCodeSub}").setAge(24).setTenant(0L));
        mapper.insertBatch(list);
        ATM.dataMap.student.countEq(2);
        ATM.dataMap.student.table(2)
            .age.values(23, 24)
            .userName.values("${G_commodityCodeSub}", "#{G_commodityCodeSub}")
            .eqTable();
        want.number(list.get(0).getId()).notNull();
        want.number(list.get(1).getId()).notNull();
        db.sqlList().wantFirstSql().eq("" +
            "INSERT INTO fluent_mybatis.student " +
            "(`age`, `env`, `tenant`, `user_name`, `gmt_created`, `gmt_modified`, `is_deleted`) " +
            "VALUES " +
            "(?, ?, ?, ?, now(), now(), 0), " +
            "(?, ?, ?, ?, now(), now(), 0)");
    }

    @Test
    public void testInsertBatch_WithId() {
        ATM.dataMap.student.cleanTable();
        List<StudentEntity> list = list(
            new StudentEntity().setId(23L).setUserName("name1").setAge(23).setTenant(0L),
            new StudentEntity().setId(24L).setUserName("name2").setAge(24).setTenant(0L));
        mapper.insertBatchWithPk(list);
        ATM.dataMap.student.countEq(2);
        ATM.dataMap.student.table(2)
            .age.values(23, 24)
            .userName.values("name1", "name2")
            .eqTable();
        want.array(list.stream().map(StudentEntity::getId).toArray())
            .eqReflect(new long[]{23, 24});
    }

    @DisplayName("部分id有值, 插入失败")
    @Test
    public void testInsertBatch() {
        ATM.dataMap.student.cleanTable();
        List<StudentEntity> list = list(
            new StudentEntity().setUserName("name1").setAge(23).setId(101L).setTenant(0L),
            new StudentEntity().setUserName("name2").setAge(24).setTenant(0L));
        want.exception(() -> mapper.insertBatch(list), FluentMybatisException.class, MyBatisSystemException.class)
            .contains("The pk of insert entity must be null");
        ATM.dataMap.student.countEq(0);
    }
}
