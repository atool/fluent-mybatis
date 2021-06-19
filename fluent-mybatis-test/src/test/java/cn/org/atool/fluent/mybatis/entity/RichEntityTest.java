package cn.org.atool.fluent.mybatis.entity;

import cn.org.atool.fluent.mybatis.base.RichEntity;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RichEntityTest extends BaseTest {

    @Test
    void testSave() {
        ATM.dataMap.student.table(0).clean();
        StudentEntity entity = new StudentEntity().setUserName("FluentMybatis").save();
        ATM.dataMap.student.table(1)
            .userName.values("FluentMybatis")
            .id.values(entity.getId())
            .eqTable();
    }

    @Test
    void testUpdateById() {
        ATM.dataMap.student.table(1)
            .id.values(1)
            .userName.values("test1")
            .cleanAndInsert();
        StudentEntity entity = new StudentEntity().setId(1L).setUserName("test2").updateById();
        db.sqlList().wantFirstSql().eq("UPDATE student SET gmt_modified = now(), user_name = ? WHERE id = ?");
        ATM.dataMap.student.table(1)
            .userName.values("test2")
            .id.values(entity.getId())
            .eqTable();
    }

    @Test
    void testUpdateById_notSetId() {
        want.exception(() ->
                new StudentEntity().setUserName("test2").updateById(),
            RuntimeException.class).contains("the primary of entity can't be null");
    }

    @Test
    void testFindById() {
        ATM.dataMap.student.table(1)
            .id.values(1)
            .userName.values("test1")
            .cleanAndInsert();
        StudentEntity entity = new StudentEntity() {
            {
                this.setId(1L);
            }
        }.findById();
        db.sqlList().wantFirstSql().end("FROM student WHERE id = ?");
        want.object(entity).eqDataMap(ATM.dataMap.student.entity(1).userName.values("test1"));
    }

    @Test
    void testFindById_NotFluentEntity() {
        want.exception(
            () -> new RichEntity() {
            }.listByNotNull(), RuntimeException.class)
            .contains("is not a @FluentMybatis Entity");
    }

    @Test
    void testFindById_notSetId() {
        want.exception(() ->
                new StudentEntity().setUserName("test2").updateById(),
            RuntimeException.class).contains("the primary of entity can't be null");
    }

    @Test
    void testDeleteById() {
        ATM.dataMap.student.table(1)
            .id.values(1)
            .userName.values("test1")
            .cleanAndInsert();
        new StudentEntity().setId(1L).deleteById();
        db.sqlList().wantFirstSql().end("DELETE FROM student WHERE `id` = ?");
        db.table(ATM.table.student).count().isEqualTo(0);
    }

    @Test
    void testLogicDeleteById() {
        new StudentEntity().setId(1L).logicDeleteById();
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE student SET `is_deleted` = true WHERE `id` = ?");
        db.sqlList().wantFirstPara().eq(new Object[]{1L});
    }

    @Test
    void testDeleteById_notSetId() {
        want.exception(() ->
                new StudentEntity().setUserName("test2").deleteById(),
            RuntimeException.class).contains("the primary of entity can't be null");
    }

    @Test
    void testListByNotNull() {
        ATM.dataMap.student.table(3)
            .id.values(3, 4, 6)
            .userName.values("test1")
            .tenant.values(123L)
            .cleanAndInsert();
        List<StudentEntity> list = new StudentEntity().setUserName("test1").setTenant(123L).listByNotNull();
        db.sqlList().wantFirstSql().end("FROM student WHERE user_name = ? AND tenant = ?");
        want.list(list).eqDataMap(ATM.dataMap.student.entity(3)
            .id.values(3, 4, 6)
            .userName.values("test1")
            .tenant.values(123L)
        );
    }

    @Test
    void testListByNotNull_AllNull() {
        want.exception(() -> new StudentEntity().listByNotNull(), FluentMybatisException.class)
            .contains("the property of entity can't be all empty");
    }
}
