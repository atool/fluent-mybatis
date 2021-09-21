package cn.org.atool.fluent.mybatis.test.listpojos;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.entity.StudentScoreEntity;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentScoreQuery;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.segment.JoinQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
