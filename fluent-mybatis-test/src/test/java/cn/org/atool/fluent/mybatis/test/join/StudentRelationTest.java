package cn.org.atool.fluent.mybatis.test.join;

import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentScoreEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentScoreMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class StudentRelationTest extends BaseTest {
    @Autowired
    StudentMapper studentMapper;

    @Autowired
    StudentScoreMapper studentScoreMapper;

    public void setup() {
        ATM.dataMap.student.initTable(1)
            .id.values(1)
            .userName.values("test")
            .env.values("test_env")
            .gender.values("1")
            .cleanAndInsert();
        ATM.dataMap.studentScore.initTable(2)
            .id.values(1, 2)
            .studentId.values(1, 1)
            .score.values(70, 80)
            .gender.values(1)
            .env.values("test_env")
            .cleanAndInsert();
    }

    @Test
    void testListStudentScore() {
        this.setup();
        StudentEntity student = studentMapper.findById(1L);
        want.object(student).notNull();
        List<StudentScoreEntity> scores = student.findStudentScoreList();
        want.list(scores).eqDataMap(ATM.dataMap.studentScore.entity(2)
            .studentId.values(1)
            .score.values(70, 80)
        );
        List<StudentScoreEntity> scores2 = student.findStudentScoreList();
        want.bool(scores == scores2).is(true);
    }

    @Test
    void testFindStudent() {
        this.setup();
        StudentScoreEntity score = studentScoreMapper.findById(1);
        StudentEntity student = score.findStudent();
        want.object(student).eqDataMap(ATM.dataMap.student.entity(1)
            .id.values(1)
            .userName.values("test")
        );
    }

    @Test
    void testFindStudent_returnNull() {
        ATM.dataMap.student.table().clean();
        ATM.dataMap.studentScore.initTable(2)
            .id.values(1, 2)
            .studentId.values(1, 1)
            .score.values(70, 80)
            .env.values("test_env")
            .gender.values(1)
            .cleanAndInsert();
        StudentScoreEntity score = studentScoreMapper.findById(1);
        StudentEntity student = score.findStudent();
        want.object(student).isNull();
    }
}
