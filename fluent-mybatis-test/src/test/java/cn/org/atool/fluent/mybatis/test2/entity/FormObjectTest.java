package cn.org.atool.fluent.mybatis.test2.entity;

import cn.org.atool.fluent.form.FormItem;
import cn.org.atool.fluent.form.IForm;
import cn.org.atool.fluent.form.ItemType;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.form.FormKit;
import cn.org.atool.fluent.mybatis.test1.BaseTest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Test;

import java.util.List;

public class FormObjectTest extends BaseTest {
    @Test
    public void testInsert() {
        StudentEntity entity = FormKit.newEntity(StudentEntity.class, new Form1()
            .setUserName("form test")
            .setAge(23)
        ).save();
        want.number(entity.getId()).isGt(0L);
        db.sqlList().wantFirstSql().eq("" +
            "INSERT INTO fluent_mybatis.student " +
            "(`gmt_created`, `gmt_modified`, `is_deleted`, `age`, `env`, `tenant`, `user_name`) " +
            "VALUES (now(), now(), 0, ?, ?, ?, ?)");
        db.sqlList().wantFirstPara().eqList(23, "test_env", 234567L, "form test");
    }

    @Test
    public void testUpdate() {
        Form2 form = this.newForm2()
            .setAges(new Integer[]{12, 56})
            .setAddresses(list("a1", "a2"));
        FormKit.newUpdate(StudentEntity.class, form).to().updateBy();
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE fluent_mybatis.student " +
            "SET `gmt_modified` = now(), " +
            "`user_name` = ?, " +
            "`age` = ? " +
            "WHERE `is_deleted` = ? " +
            "AND `env` = ? " +
            "AND `tenant` = ? " +
            "AND `version` <> ? " +
            "AND `address` IS NULL " +
            "AND `age` BETWEEN ? AND ? " +
            "AND `address` IN (?, ?)");
        db.sqlList().wantFirstPara().eqList(
            "form test", 23, false, "test_env", 45L, "abc", 12, 56, "a1", "a2");
    }

    @Test
    public void testUpdate2() {
        Form2 form = newForm2();
        FormKit.newUpdate(StudentEntity.class, form.setVersion(null).setAdd("address")).to().updateBy();
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE fluent_mybatis.student " +
            "SET `gmt_modified` = now(), " +
            "`user_name` = ?, " +
            "`age` = ? " +
            "WHERE `is_deleted` = ? " +
            "AND `env` = ? " +
            "AND `tenant` = ? " +
            "AND `address` = ?");
        db.sqlList().wantFirstPara().eqList("form test", 23, false, "test_env", 45, "address");
    }

    @Test
    public void testQuery() {
        Form2 form = newForm2();
        FormKit.newQuery(StudentEntity.class, form.setVersion(null).setAdd("address")).to().listEntity();
        db.sqlList().wantFirstSql()
            .start("SELECT")
            .end("WHERE `is_deleted` = ? " +
                "AND `env` = ? " +
                "AND `tenant` = ? " +
                "AND `address` = ?");
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
    public static class Form1 implements IForm {
        @FormItem(type = ItemType.UPDATE)
        private String userName;

        @FormItem(type = ItemType.UPDATE)
        private int age;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Accessors(chain = true)
    public static class Form2 extends Form1 {
        @FormItem
        private long tenant;

        @FormItem(type = ItemType.NE)
        private String version;

        @FormItem(field = "address", ignoreNull = false)
        private String add;

        @FormItem(field = "age", type = ItemType.BETWEEN)
        private Integer[] ages;

        @FormItem(field = "address", type = ItemType.IN)
        private List<String> addresses;
    }
}