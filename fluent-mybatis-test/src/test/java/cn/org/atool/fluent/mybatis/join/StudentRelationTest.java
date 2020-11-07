package cn.org.atool.fluent.mybatis.join;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.entity.StudentScoreEntity;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentScoreMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class StudentRelationTest extends BaseTest {
    @Autowired
    StudentMapper studentMapper;

    @Autowired
    StudentScoreMapper studentScoreMapper;

    @BeforeEach
    public void setup() {
        ATM.DataMap.student.initTable(1)
            .id.values(1)
            .userName.values("test")
            .env.values("test_env")
            .cleanAndInsert();
        ATM.DataMap.studentScore.initTable(2)
            .id.values(1, 2)
            .studentId.values(1, 1)
            .score.values(70, 80)
            .env.values("test_env")
            .cleanAndInsert();
    }

    @Test
    void testListStudentScore() {
        StudentEntity student = studentMapper.findById(1L);
        want.object(student).notNull();
        List<StudentScoreEntity> scores = student.listStudentScore();
        want.list(scores).eqDataMap(ATM.DataMap.studentScore.entity(2)
            .studentId.values(1)
            .score.values(70, 80)
        );
        List<StudentScoreEntity> scores2 = student.listStudentScore();
        want.bool(scores == scores2).is(true);
    }

    @Test
    void testFindStudent() {
        StudentScoreEntity score = studentScoreMapper.findById(1);
        StudentEntity student = score.findStudent();
        want.object(student).eqDataMap(ATM.DataMap.student.entity(1)
            .id.values(1)
            .userName.values("test")
        );
    }
}