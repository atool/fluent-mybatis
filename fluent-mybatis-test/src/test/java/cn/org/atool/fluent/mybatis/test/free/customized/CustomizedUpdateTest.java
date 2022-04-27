package cn.org.atool.fluent.mybatis.test.free.customized;

import cn.org.atool.fluent.mybatis.base.free.FreeQuery;
import cn.org.atool.fluent.mybatis.base.free.FreeUpdate;
import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.modes.EqMode;

public class CustomizedUpdateTest extends BaseTest {
    @Autowired
    StudentMapper mapper;

    @Test
    void test_insert() {
        ATM.dataMap.student.table().clean();
        FreeUpdate updater = new FreeUpdate(null)
            .customizedByPlaceholder("insert into student " +
                    "(`user_name`, `age`) " +
                    "values(#{userName}, #{age})",
                new StudentEntity().setUserName("test").setAge(25));
        mapper.updateBy(updater);
        db.sqlList().wantFirstSql()
            .eq("insert into student (`user_name`, `age`) values(?, ?)");
        db.sqlList().wantFirstPara().eq(new Object[]{"test", 25});
        ATM.dataMap.student.table(1)
            .userName.values("test")
            .age.values(25)
            .eqTable(EqMode.EQ_STRING);
    }

    @Test
    void test_insert_select() {
        ATM.dataMap.student.table(3)
            .address.values("address1", "address2", "address3")
            .age.values(34, 45, 55)
            .cleanAndInsert();
        FreeQuery query = new FreeQuery(null)
            .customizedByPlaceholder("" +
                    "SELECT address, age FROM fluent_mybatis.student " +
                    "WHERE id IN (#{value[0]}, #{value[1]}, #{value[2]})",
                new long[]{1, 2, 3});

        mapper.insertSelect(new String[]{"address", "age"}, query);
        db.sqlList().wantFirstSql().eq("" +
            "INSERT INTO fluent_mybatis.student (`address`, `age`) " +
            "SELECT address, age " +
            "FROM fluent_mybatis.student " +
            "WHERE id IN (?, ?, ?)");
        db.sqlList().wantFirstPara().eqList(1L, 2L, 3L);
    }

    @Test
    void test_updateBy() {
        FreeUpdate updater = new FreeUpdate(null)
            .customizedByPlaceholder("UPDATE fluent_mybatis.student " +
                    "set user_name=#{userName}, " +
                    "age=#{age} " +
                    "where id=#{id}",
                new StudentEntity().setUserName("test").setAge(25).setId(3L));
        mapper.updateBy(updater);
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE fluent_mybatis.student set user_name=?, age=? where id=?");
        db.sqlList().wantFirstPara().eq(new Object[]{"test", 25, 3L});
    }

    @Test
    void test_updateBy2() {
        FreeUpdate updater1 = new FreeUpdate(null)
            .customizedByPlaceholder("UPDATE fluent_mybatis.student " +
                    "set user_name=#{userName}, " +
                    "age=#{age} " +
                    "where id=#{id}",
                new StudentEntity().setUserName("test").setAge(25).setId(3L));
        FreeUpdate updater2 = new FreeUpdate(null)
            .customizedByQuestion("UPDATE fluent_mybatis.student " +
                    "set user_name=?, " +
                    "address=? " +
                    "where id=?",
                "test", "test", 4L);
        mapper.updateBy(updater1, updater2);
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE fluent_mybatis.student set user_name=?, age=? where id=?; " +
            "UPDATE fluent_mybatis.student set user_name=?, address=? where id=?");
        db.sqlList().wantFirstPara().eqList("test", 25, 3L, "test", "test", 4L);
    }

    @Test
    void test_delete() {
        FreeQuery query = new FreeQuery(null)
            .customizedByPlaceholder("" +
                    "delete FROM fluent_mybatis.student " +
                    "WHERE id IN (#{value[0]}, #{value[1]}, #{value[2]})",
                new long[]{1, 2, 3});
        mapper.delete(query);
        db.sqlList().wantFirstSql().eq("delete FROM fluent_mybatis.student WHERE id IN (?, ?, ?)");
        db.sqlList().wantFirstPara().eq(new Object[]{1L, 2L, 3L});
    }
}
