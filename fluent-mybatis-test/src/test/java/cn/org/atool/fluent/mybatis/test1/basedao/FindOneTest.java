package cn.org.atool.fluent.mybatis.test1.basedao;

import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.dao.intf.StudentDao;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test1.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.modes.EqMode;

public class FindOneTest extends BaseTest {
    @Autowired
    StudentDao dao;

    @Test
    void findOne() {
        ATM.dataMap.student.table().clean();
        StudentEntity.builder()
            .id(2L).userName("my.name").env("test_env")
            .address("address").bonusPoints(34L)
            .build().save();
        StudentQuery query = StudentQuery.emptyQuery()
            .where.id().eq(2).end();
        StudentEntity student = dao.findOne(query).orElse(null);
        want.object(student).eqReflect(StudentEntity.builder()
            .id(2L).userName("my.name").env("test_env")
            .address("address").bonusPoints(34L)
            .build(), EqMode.IGNORE_DEFAULTS);
    }
}
