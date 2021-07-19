package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.mapper.NoPrimaryMapper;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.tools.datagen.DataGenerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author darui.wu 2019/10/29 9:33 下午
 */
@SuppressWarnings({"unchecked"})
public class SelectByIdsTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Autowired
    private NoPrimaryMapper noPrimaryMapper;

    @Test
    public void test_selectById() {
        ATM.dataMap.student.initTable(3)
            .userName.values(DataGenerator.increase("username_%d"))
            .cleanAndInsert();

        List<StudentEntity> users = mapper.listByIds(Arrays.asList(3L, 1L));
        db.sqlList().wantFirstSql()
            .where().eq("`id` IN (?, ?)");
        want.list(users)
            .eqMap(ATM.dataMap.student.entity(2)
                .userName.values("username_1", "username_3")
            );
    }

    @Test
    public void test_selectById_noPrimary() {
        db.table(ATM.table.noPrimary).clean().insert(ATM.dataMap.noPrimary.initTable(3)
            .column1.values(1, 2, 3)
            .column2.values("c1", "c2", "c3")
        );
        want.exception(() -> noPrimaryMapper.listByIds(Collections.singletonList(3L)),
            MyBatisSystemException.class);
    }
}