package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.NoAutoIdEntity;
import cn.org.atool.fluent.mybatis.generate.entity.NoPrimaryEntity;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.mapper.NoAutoIdMapper;
import cn.org.atool.fluent.mybatis.generate.mapper.NoPrimaryMapper;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
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
        db.table(ATM.table.student).clean();
        StudentEntity student = new StudentEntity()
            .setAge(23)
            .setUserName("tom mike");
        userMapper.insert(student);
        student.setId(null);
        userMapper.insert(student);

        want.number(student.getId()).isGt(1L);
        db.table(ATM.table.student).query().eqDataMap(ATM.dataMap.student.table(2)
            .age.values(23)
            .userName.values("tom mike")
        );
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
        db.table(ATM.table.noAutoId).clean();
        idMapper.insertWithPk(new NoAutoIdEntity()
            .setId("test-id-1")
            .setColumn1("test")
        );
        idMapper.insertWithPk(new NoAutoIdEntity()
            .setId("test-id-2")
            .setColumn1("test")
        );
        db.table(ATM.table.noAutoId).query().eqDataMap(ATM.dataMap.noAutoId.table(2)
            .id.values("test-id-1", "test-id-2")
        );
    }

    @Test
    public void testInsert_NoAutoId_conflict() {
        db.table(ATM.table.noAutoId).clean().insert(ATM.dataMap.noAutoId.initTable(1)
            .id.values("test-id-1")
            .column1.values("test")
        );
        want.exception(() -> idMapper.insert(new NoAutoIdEntity()
            .setId("test-id-1")
            .setColumn1("test")
        ), DuplicateKeyException.class, MyBatisSystemException.class);
    }

    @Test
    public void test_insert_noPrimary() {
        db.table(ATM.table.noPrimary).clean();
        noPrimaryMapper.insert(new NoPrimaryEntity()
            .setColumn1(23)
            .setColumn2("test")
        );
        db.table(ATM.table.noPrimary).query().eqDataMap(ATM.dataMap.noPrimary.table(1)
            .column1.values(23)
            .column2.values("test")
        );
    }
}
