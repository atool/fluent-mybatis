package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared1.entity.NoAutoIdEntity;
import cn.org.atool.fluent.mybatis.generator.shared1.entity.NoPrimaryEntity;
import cn.org.atool.fluent.mybatis.generator.shared1.mapper.NoAutoIdMapper;
import cn.org.atool.fluent.mybatis.generator.shared1.mapper.NoPrimaryMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

public class InsertTest extends BaseTest {
    @Autowired
    private StudentMapper userMapper;

    @Autowired
    private NoAutoIdMapper idMapper;

    @Autowired
    private NoPrimaryMapper noPrimaryMapper;

    @Test
    public void testInsert() {
        ATM.dataMap.student.cleanTable();
        StudentEntity student = new StudentEntity()
            .setAge(23)
            .setUserName("tom mike");
        userMapper.insert(student);
        student.setId(null);
        userMapper.insert(student);

        want.number(student.getId()).isGt(1L);
        ATM.dataMap.student.table(2)
            .age.values(23)
            .userName.values("tom mike")
            .eqTable();
    }

    @Test
    public void testInsert_NoIdError() {
        want.exception(() -> idMapper.insert(new NoAutoIdEntity()
                .setId("test-id-1")
                .setColumn1("test")),
            MyBatisSystemException.class
        ).contains("The pk of insert entity must be null");
    }

    @Test
    public void testInsert_NoAutoId() {
        ATM.dataMap.noAutoId.cleanTable();
        idMapper.insertWithPk(new NoAutoIdEntity()
            .setId("test-id-1")
            .setColumn1("test")
        );
        idMapper.insertWithPk(new NoAutoIdEntity()
            .setId("test-id-2")
            .setColumn1("test")
        );
        ATM.dataMap.noAutoId.table(2)
            .id.values("test-id-1", "test-id-2")
            .eqTable();
    }

    @Test
    public void testInsert_NoAutoId_conflict() {
        ATM.dataMap.noAutoId.initTable(1)
            .id.values("test-id-1")
            .column1.values("test")
            .cleanAndInsert();
        want.exception(() -> idMapper.insert(new NoAutoIdEntity()
            .setId("test-id-1")
            .setColumn1("test")
        ), DuplicateKeyException.class, MyBatisSystemException.class);
    }

    @Test
    public void test_insert_noPrimary() {
        ATM.dataMap.noPrimary.cleanTable();
        noPrimaryMapper.insert(new NoPrimaryEntity()
            .setColumn1(23)
            .setColumn2("test")
        );
        ATM.dataMap.noPrimary.table(1)
            .column1.values(23)
            .column2.values("test")
            .eqTable();
    }
}
