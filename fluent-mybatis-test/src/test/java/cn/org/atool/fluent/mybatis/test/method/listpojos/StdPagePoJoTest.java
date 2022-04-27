package cn.org.atool.fluent.mybatis.test.method.listpojos;

import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentScoreEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentScoreQuery;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.segment.JoinQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class StdPagePoJoTest extends BaseTest {
    @Autowired
    StudentMapper mapper;

    @BeforeEach
    void setup() {
        ATM.dataMap.student.table(1)
            .id.values(10)
            .userName.values("user1")
            .env.values("test_env")
            .cleanAndInsert();
        ATM.dataMap.studentScore.table(1)
            .studentId.values(10)
            .env.values("test_env")
            .cleanAndInsert();
    }

    @DisplayName("验证toPoJo时, 驼峰转换处理")
    @Test
    void listPoJo() {
        List<StudentEntity> student = new StudentQuery().select.userName("userName").end()
            .to().listPoJo(StudentEntity.class);
        want.list(student).eqByProperties("userName", new String[]{"user1"});
    }

    @DisplayName("join查询, 自动设置别名")
    @Test
    void stdPaged() {
        JoinQuery query = new StudentQuery().join(new StudentScoreQuery())
            .onEq(StudentEntity::getId, StudentScoreEntity::getStudentId)
            .endJoin().build();
        StdPagedList list = mapper.stdPagedPoJo(query, this::getMap);
        want.number(list.getTotal()).eq(1);
    }

    Object getMap(Map map) {
        return map;
    }
}
