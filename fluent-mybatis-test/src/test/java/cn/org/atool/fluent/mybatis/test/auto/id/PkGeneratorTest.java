package cn.org.atool.fluent.mybatis.test.auto.id;

import cn.org.atool.fluent.mybatis.customize.SnowFlakeGenerator;
import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.HomeAddressEntity;
import cn.org.atool.fluent.mybatis.generate.mapper.HomeAddressMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.annotations.Mocks;

import static cn.org.atool.fluent.mybatis.test.auto.id.PkGeneratorTestMocks.mocks;

@Mocks(SnowFlakeGenerator.class)
public class PkGeneratorTest extends BaseTest {
    @Autowired
    HomeAddressMapper mapper;

    @Test
    void insertWithPk() {
        ATM.dataMap.homeAddress.table().clean();
        // mock, 模拟雪花id为100
        mocks.SnowFlakeGenerator().uuid.thenReturn(100L);

        mapper.insertWithPk(new HomeAddressEntity()
            .setAddress("add")
            .setStudentId(0L)
        );
        // 验证
        ATM.dataMap.homeAddress.table(1)
            .id.values(100L)
            .address.values("add")
            .eqTable();
    }

    @Test
    void insertBatchWithPk() {
        ATM.dataMap.homeAddress.table().clean();
        mocks.SnowFlakeGenerator().uuid.thenReturn(200L, 300L);
        mocks.SnowFlakeGenerator().fake.thenReturn(345L);
        mapper.insertBatchWithPk(list(
            new HomeAddressEntity()
                .setAddress("add")
                .setStudentId(0L),
            new HomeAddressEntity()
                .setAddress("add")
                .setStudentId(0L)
        ));
        ATM.dataMap.homeAddress.table(2)
            .id.values(200L, 300L)
            .address.values("add")
            .eqTable();
        want.number(SnowFlakeGenerator.fake()).eq(345L);
    }

    @Test
    void insertBatch() {
        want.exception(() ->
            mapper.insertBatch(list(
                new HomeAddressEntity()
                    .setAddress("add")
                    .setStudentId(0L),
                new HomeAddressEntity()
                    .setAddress("add")
                    .setStudentId(0L)
            )), Exception.class)
            .contains("The pk of insert entity must be null");
    }

    @Test
    void insert() {
        want.exception(() ->
                mapper.insert(new HomeAddressEntity()
                    .setAddress("add")
                    .setStudentId(0L)
                ),
            Exception.class)
            .contains("The pk of insert entity must be null");
    }

    @Test
    void save() {
        mocks.SnowFlakeGenerator().uuid.thenReturn(400L);
        mapper.save(new HomeAddressEntity()
            .setAddress("add")
            .setStudentId(0L)
        );
        db.sqlList().wantFirstSql().eq("" +
            "INSERT INTO `home_address`(`id`, `gmt_created`, `gmt_modified`, `is_deleted`, `address`, `env`, `student_id`, `tenant`) " +
            "VALUES (?, now(), now(), 0, ?, ?, ?, ?)");
        db.sqlList().wantFirstPara().eqList(400L, "add", "test_env", 0L, 234567L);
    }
}
