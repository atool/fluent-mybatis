package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.customize.SnowFlakeGenerator;
import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.dao.intf.StudentDao;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.HomeAddressEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.HomeAddressMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.annotations.Mock;
import org.test4j.mock.MockUp;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("unused")
public class InsertDefaultTest extends BaseTest {
    @Autowired
    private StudentDao dao;

    @Autowired
    private HomeAddressMapper addressMapper;

    @BeforeEach
    public void setup() {
        ATM.dataMap.homeAddress.table().clean();
        AtomicLong id = new AtomicLong(200);
        new MockUp<SnowFlakeGenerator>() {
            @Mock
            public long uuid() {
                return id.incrementAndGet();
            }
        };
    }

    @DisplayName("dao插入默认值")
    @Test
    void testDefaultInsert() {
        ATM.dataMap.student.table().clean();
        new StudentEntity().setUserName("FluentMybatis").save();
        ATM.dataMap.student.table(1)
            .userName.values("FluentMybatis")
            .env.values("test_env")
            .tenant.values(234567L)
            .isDeleted.values(0)
            .eqTable();
        db.sqlList().wantFirstSql().eq(
            "INSERT INTO fluent_mybatis.student (`env`, `tenant`, `user_name`, `gmt_created`, `gmt_modified`, `is_deleted`) " +
                "VALUES (?, ?, ?, now(), now(), 0)");
    }

    @DisplayName("save操作pkGenerator验证")
    @Test
    void saveWithPkGenerate() {
        new HomeAddressEntity().setProvince("test").setCity("test").setStudentId(1L).save();
        db.sqlList().wantFirstSql().eq("INSERT INTO `home_address` " +
            "(`id`, `city`, `env`, `province`, `student_id`, `tenant`, `gmt_created`, `gmt_modified`, `is_deleted`) " +
            "VALUES (?, ?, ?, ?, ?, ?, now(), now(), 0)");
        db.sqlList().wantFirstPara().eqList(201L, "test", "test_env", "test", 1, 234567L);
    }

    @DisplayName("save操作pkGenerator验证")
    @Test
    void saveBatchWithPkGenerate() {
        HomeAddressEntity entity1 = new HomeAddressEntity().setProvince("test").setCity("test").setStudentId(1L);
        HomeAddressEntity entity2 = new HomeAddressEntity().setProvince("test").setCity("test").setStudentId(1L);
        addressMapper.save(list(entity1, entity2));
        db.sqlList().wantFirstSql().eq("" +
            "INSERT INTO `home_address` " +
            "(`id`, `city`, `env`, `province`, `student_id`, `tenant`, `gmt_created`, `gmt_modified`, `is_deleted`) " +
            "VALUES " +
            "(?, ?, ?, ?, ?, ?, now(), now(), 0), " +
            "(?, ?, ?, ?, ?, ?, now(), now(), 0)");
        db.sqlList().wantFirstPara().eqList(
            201L, "test", "test_env", "test", 1, 234567L,
            202L, "test", "test_env", "test", 1, 234567L);
    }

    @DisplayName("batchInsert操作pkGenerator验证")
    @Test
    void batchInsertWithPkGenerate() {
        HomeAddressEntity entity1 = new HomeAddressEntity().setProvince("test").setCity("test").setStudentId(1L);
        HomeAddressEntity entity2 = new HomeAddressEntity().setProvince("test").setCity("test").setStudentId(1L);
        addressMapper.insertBatchWithPk(list(entity1, entity2));

        db.sqlList().wantFirstSql().eq("" +
            "INSERT INTO `home_address` " +
            "(`id`, `city`, `env`, `province`, `student_id`, `tenant`, `gmt_created`, `gmt_modified`, `is_deleted`) " +
            "VALUES " +
            "(?, ?, ?, ?, ?, ?, now(), now(), 0), " +
            "(?, ?, ?, ?, ?, ?, now(), now(), 0)");
        db.sqlList().wantFirstPara().eqList(
            201L, "test", "test_env", "test", 1, 234567L,
            202L, "test", "test_env", "test", 1, 234567L);
    }

    @DisplayName("dao批量插入默认值")
    @Test
    void testDefaultBatchInsert() {
        ATM.dataMap.student.table().clean();
        dao.save(Collections.singletonList(new StudentEntity().setUserName("FluentMybatis")));
        ATM.dataMap.student.table(1)
            .userName.values("FluentMybatis")
            .env.values("test_env")
            .tenant.values(234567L)
            .isDeleted.values(0)
            .eqTable();
    }
}
