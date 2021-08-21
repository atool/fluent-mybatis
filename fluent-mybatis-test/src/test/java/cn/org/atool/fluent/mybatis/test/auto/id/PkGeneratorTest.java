package cn.org.atool.fluent.mybatis.test.auto.id;

import cn.org.atool.fluent.mybatis.customize.SnowFlakeFake;
import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.HomeAddressEntity;
import cn.org.atool.fluent.mybatis.generate.mapper.HomeAddressMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.annotations.Mocks;

import static cn.org.atool.fluent.mybatis.test.auto.id.SnowFlakeIdTestMocks.mocks;

@Mocks(SnowFlakeFake.class)
public class PkGeneratorTest extends BaseTest {
    @Autowired
    HomeAddressMapper mapper;

    @Test
    void insertWithPk() {
        ATM.dataMap.homeAddress.table().clean();
        mocks.SnowFlakeFake.snowFlakeId.thenReturn(100L);
        mapper.insertWithPk(new HomeAddressEntity()
            .setAddress("add")
            .setStudentId(0L)
        );
        ATM.dataMap.homeAddress.table(1)
            .id.values(100L)
            .address.values("add")
            .eqTable();
    }

    @Test
    void insertBatchWithPk() {
        ATM.dataMap.homeAddress.table().clean();
        mocks.SnowFlakeFake.snowFlakeId.thenReturn(200L, 300L);
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
        mapper.save(new HomeAddressEntity()
            .setAddress("add")
            .setStudentId(0L)
        );

    }
}
