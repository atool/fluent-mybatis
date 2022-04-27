package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.Ref;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

import java.util.List;

import static org.test4j.tools.datagen.DataGenerator.increase;

/**
 * @author darui.wu 2019/10/31 6:18 下午
 */
public class DistinctTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_distinct() {
        ATM.dataMap.student.initTable(10)
            .userName.values(increase(index -> index > 5 ? "user2" : "user1"))
            .age.values(30)
            .cleanAndInsert();
        StudentQuery query = StudentQuery.emptyQuery()
            .distinct()
            .select.apply(Ref.Field.Student.userName).end()
            .where.age().eq(30).end();

        List<StudentEntity> users = mapper.listEntity(query);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT DISTINCT `user_name` FROM fluent_mybatis.student WHERE `age` = ?", StringMode.SameAsSpace);
        want.list(users).eqDataMap(ATM.dataMap.student.entity(2)
            .userName.values("user1", "user2")
        );
    }
}
