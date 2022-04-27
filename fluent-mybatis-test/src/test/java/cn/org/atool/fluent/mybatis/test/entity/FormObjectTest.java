package cn.org.atool.fluent.mybatis.test.entity;

import cn.org.atool.fluent.form.FormKit;
import cn.org.atool.fluent.form.annotation.Entry;
import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.form.annotation.MethodType;
import cn.org.atool.fluent.form.meta.ArgumentMeta;
import cn.org.atool.fluent.form.meta.MethodMeta;
import cn.org.atool.fluent.form.registrar.FormServiceKit;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Test;

import java.util.List;

public class FormObjectTest extends BaseTest {
    @Test
    public void testInsert() {
        ArgumentMeta arg = FormKit.argForm(MethodType.Save, Form1.class, 0, null);
        MethodMeta method = FormKit.buildSave(StudentEntity.class, null, StudentEntity.class, arg);
        StudentEntity entity = (StudentEntity) FormServiceKit.save(method, new Form1()
            .setUserName("form test")
            .setAge(23));
        assert entity != null;
        want.number(entity.getId()).isGt(0L);
        db.sqlList().wantFirstSql().eq("" +
            "INSERT INTO fluent_mybatis.student " +
            "(`age`, `env`, `tenant`, `user_name`, `gmt_created`, `gmt_modified`, `is_deleted`) " +
            "VALUES (?, ?, ?, ?, now(), now(), 0)");
        db.sqlList().wantFirstPara().eqList(23, "test_env", 234567L, "form test");
    }

    @Test
    public void testUpdate() {
        ArgumentMeta arg = FormKit.argForm(MethodType.Update, Form2.class, 0, null);
        MethodMeta method = FormKit.buildUpdate(StudentEntity.class, null, int.class, arg);
        FormServiceKit.update(method, this.newForm2()
            .setAges(new Integer[]{12, 56})
            .setAddresses(list("a1", "a2")));
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE fluent_mybatis.student " +
            "SET `gmt_modified` = now(), " +
            "`user_name` = ?, " +
            "`age` = ? " +
            "WHERE `is_deleted` = ? " +
            "AND `env` = ? " +
            "AND (`tenant` = ? " +
            "AND `version` <> ? " +
            "AND `address` IS NULL " +
            "AND `age` BETWEEN ? AND ? " +
            "AND `address` IN (?, ?))");
        db.sqlList().wantFirstPara().eqList(
            "form test", 23, false, "test_env", 45L, "abc", 12, 56, "a1", "a2");
    }

    @Test
    public void testUpdate2() {
        ArgumentMeta arg = FormKit.argForm(MethodType.Update, Form2.class, 0, null);
        MethodMeta method = FormKit.buildUpdate(StudentEntity.class, null, boolean.class, arg);
        FormServiceKit.update(method, this.newForm2().setVersion(null).setAdd("address"));
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE fluent_mybatis.student " +
            "SET `gmt_modified` = now(), " +
            "`user_name` = ?, " +
            "`age` = ? " +
            "WHERE `is_deleted` = ? " +
            "AND `env` = ? " +
            "AND (`tenant` = ? " +
            "AND `address` = ?)");
        db.sqlList().wantFirstPara().eqList("form test", 23, false, "test_env", 45, "address");
    }

    @Test
    public void testQuery() {
        ArgumentMeta arg = FormKit.argForm(MethodType.Query, Form2.class, 0, null);
        MethodMeta method = FormKit.buildList(StudentEntity.class, null, StudentEntity.class, arg);
        FormServiceKit.query(method, this.newForm2().setVersion(null).setAdd("address"));
        db.sqlList().wantFirstSql()
            .start("SELECT")
            .end("WHERE `is_deleted` = ? " +
                "AND `env` = ? " +
                "AND (`tenant` = ? " +
                "AND `address` = ?)");
        db.sqlList().wantFirstPara().eqList(false, "test_env", 45, "address");
    }

    private Form2 newForm2() {
        return (Form2) new Form2()
            .setTenant(45L)
            .setVersion("abc")
            .setUserName("form test")
            .setAge(23);
    }

    @Data
    @Accessors(chain = true)
    public static class Form1 {
        @Entry(type = EntryType.Update)
        private String userName;

        @Entry(type = EntryType.Update)
        private int age;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Accessors(chain = true)
    public static class Form2 extends Form1 {
        @Entry
        private long tenant;

        @Entry(type = EntryType.NE)
        private String version;

        @Entry(value = "address", ignoreNull = false)
        private String add;

        @Entry(value = "age", type = EntryType.Between)
        private Integer[] ages;

        @Entry(value = "address", type = EntryType.IN)
        private List<String> addresses;
    }
}
