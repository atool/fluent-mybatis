package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

import java.util.Date;

/**
 * @author darui.wu
 * @create 2020/1/2 4:37 下午
 */
public class UpdateByIdTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void testUpdate() {
        ATM.dataMap.student.initTable(2)
            .id.values(23L, 24L)
            .userName.values("user1", "user2")
            .cleanAndInsert();

        StudentEntity update = new StudentEntity()
            .setAge(45)
            .setUserName("test name")
            .setIsDeleted(true)
            .setId(24L);

        mapper.updateById(update);
        db.sqlList().wantFirstSql()
            .eq("UPDATE fluent_mybatis.student " +
                "SET `gmt_modified` = now(), " +
                "`is_deleted` = ?, " +
                "`age` = ?, `user_name` = ? " +
                "WHERE `id` = ?", StringMode.SameAsSpace);

        db.table(ATM.table.student).query().eqDataMap(ATM.dataMap.student.table(2)
            .id.values(23L, 24L)
            .userName.values("user1", "test name")
            .age.values((Object) null, 45)
        );
    }

    @Test
    public void testUpdate_gmtCreate() {
        ATM.dataMap.student.initTable(2)
            .id.values(23L, 24L)
            .userName.values("user1", "user2")
            .cleanAndInsert();

        StudentEntity update = new StudentEntity()
            .setAge(45)
            .setUserName("test name")
            .setIsDeleted(true)
            .setId(24L)
            .setGmtCreated(new Date());

        mapper.updateById(update);
        db.sqlList().wantFirstSql()
            .eq("UPDATE fluent_mybatis.student " +
                "SET `gmt_created` = ?, `gmt_modified` = now(), " +
                "`is_deleted` = ?, `age` = ?, `user_name` = ? " +
                "WHERE `id` = ?", StringMode.SameAsSpace);

        db.table(ATM.table.student).query().eqDataMap(ATM.dataMap.student.table(2)
            .id.values(23L, 24L)
            .userName.values("user1", "test name")
            .age.values((Object) null, 45)
        );
    }

    @Test
    public void testUpdate2() {
        ATM.dataMap.student.initTable(2)
            .id.values(23L, 24L)
            .userName.values("user1", "user2")
            .cleanAndInsert();

        StudentEntity update = new StudentEntity()
            .setAge(45)
            .setUserName("test name")
            .setIsDeleted(true)
            .setId(24L)
            .setGmtModified(new Date())
            .setGmtCreated(new Date());

        mapper.updateById(update);
        db.sqlList().wantFirstSql()
            .eq("UPDATE fluent_mybatis.student " +
                "SET `gmt_created` = ?, `gmt_modified` = ?, " +
                "`is_deleted` = ?, `age` = ?, `user_name` = ? " +
                "WHERE `id` = ?", StringMode.SameAsSpace);
        db.table(ATM.table.student).query().eqDataMap(ATM.dataMap.student.table(2)
            .id.values(23L, 24L)
            .userName.values("user1", "test name")
            .age.values((Object) null, 45)
        );
    }
}
