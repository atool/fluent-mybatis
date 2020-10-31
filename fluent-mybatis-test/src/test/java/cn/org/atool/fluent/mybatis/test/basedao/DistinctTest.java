package cn.org.atool.fluent.mybatis.test.basedao;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

import java.util.List;

import static cn.org.atool.fluent.mybatis.generate.helper.StudentMapping.userName;
import static org.test4j.tools.datagen.AbstractDataGenerator.increase;

/**
 * @author darui.wu
 * @create 2019/10/31 6:18 下午
 */
public class DistinctTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_distinct() {
        ATM.DataMap.student.initTable(10)
            .userName.values(increase(index -> index > 5 ? "user2" : "user1"))
            .age.values(30)
            .cleanAndInsert();
        StudentQuery query = new StudentQuery()
            .distinct()
            .select.apply(userName).end()
            .where.age().eq(30).end();

        List<StudentEntity> users = mapper.listEntity(query);
        db.sqlList().wantFirstSql().eq("SELECT DISTINCT user_name FROM t_student WHERE age = ?", StringMode.SameAsSpace);
        want.list(users).eqDataMap(ATM.DataMap.student.entity(2)
            .userName.values("user1", "user2")
        );
    }
}
