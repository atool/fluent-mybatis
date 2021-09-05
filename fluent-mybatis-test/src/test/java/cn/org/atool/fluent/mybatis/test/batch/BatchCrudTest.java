package cn.org.atool.fluent.mybatis.test.batch;

import cn.org.atool.fluent.mybatis.base.BatchCrud;
import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.HomeAddressEntity;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.HomeAddressQuery;
import cn.org.atool.fluent.mybatis.generate.wrapper.HomeAddressUpdate;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentUpdate;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

public class BatchCrudTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @BeforeEach
    void setup() {
        ATM.dataMap.student.initTable(2)
            .id.values(23L, 24L)
            .userName.values("user")
            .cleanAndInsert();
        ATM.dataMap.homeAddress.initTable(2)
            .id.values(23, 24)
            .address.values("address")
            .cleanAndInsert();
    }

    @DisplayName("批量更新不同表")
    @Test
    public void testUpdateBatch_different() {
        StudentUpdate update1 = new StudentUpdate()
            .set.userName().is("user name23").end()
            .where.id().eq(23L).end();
        HomeAddressUpdate update2 = new HomeAddressUpdate()
            .set.address().is("address 24").end()
            .where.id().eq(24L).end();
        mapper.batchCrud(BatchCrud.batch()
            .setDbType(DbType.MYSQL)
            .addInsert(
                new StudentEntity().setId(100L).setUserName("user 100"),
                new HomeAddressEntity().setAddress("address 100").setStudentId(100L))
            .addUpdate(update1, update2)
            .addDelete(HomeAddressQuery.emptyQuery().where.id().ge(24).end())
        );
        db.sqlList().wantFirstSql()
            .eq("" +
                    "INSERT INTO fluent_mybatis.student(`id`, `gmt_created`, `gmt_modified`, `is_deleted`, `env`, `tenant`, `user_name`) " +
                    "VALUES (?, now(), now(), 0, ?, ?, ?); " +
                    "INSERT INTO `home_address`(`id`, `gmt_created`, `gmt_modified`, `is_deleted`, `address`, `env`, `student_id`, `tenant`) " +
                    "VALUES (?, now(), now(), 0, ?, ?, ?, ?); " +
                    "UPDATE fluent_mybatis.student SET `gmt_modified` = now(), `user_name` = ? WHERE `id` = ?; " +
                    "UPDATE `home_address` SET `gmt_modified` = now(), `address` = ? WHERE `id` = ?; " +
                    "DELETE FROM `home_address` WHERE `id` >= ?"
                , StringMode.SameAsSpace);
        db.table(ATM.table.student).query().eqDataMap(ATM.dataMap.student.table(3)
            .id.values(23L, 24L, 100L)
            .userName.values("user name23", "user", "user 100")
        );
        db.table(ATM.table.homeAddress).query().eqDataMap(ATM.dataMap.homeAddress.table(1)
            .id.values(23)
            .address.values("address")
        );
    }
}