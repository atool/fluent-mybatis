package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared1.mapper.NoPrimaryMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.tools.datagen.DataGenerator;

/**
 * @author darui.wu
 * @create 2019/10/29 9:33 下午
 */
public class SelectByIdTest2 extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Autowired
    private NoPrimaryMapper noPrimaryMapper;

    @Test
    public void test_selectById() throws Exception {
        ATM.dataMap.student.initTable(3)
            .userName.values(DataGenerator.increase("username_%d"))
            .cleanAndInsert();
        StudentEntity student = mapper.findById(3L);
        db.sqlList().wantFirstSql()
            .where().eq("`id` = ?");
        want.object(student)
            .eqMap(ATM.dataMap.student.entity()
                .userName.values("username_3")
            );
    }

    @Test
    public void test_selectById_noPrimary() throws Exception {
        ATM.dataMap.noPrimary.initTable(3)
            .column1.values(1, 2, 3)
            .column2.values("c1", "c2", "c3")
            .cleanAndInsert();
        want.exception(() -> noPrimaryMapper.findById(3L), FluentMybatisException.class)
            .contains("primary not found");
    }
}
