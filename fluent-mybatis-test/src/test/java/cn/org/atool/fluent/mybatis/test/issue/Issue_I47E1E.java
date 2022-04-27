package cn.org.atool.fluent.mybatis.test.issue;

import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.test4j.integration.DataProvider;

@SuppressWarnings("unused")
public class Issue_I47E1E extends BaseTest {
    @ParameterizedTest
    @MethodSource("data4hasNextPage")
    void hasNextPage(int form, int limit, boolean expected) {
        ATM.dataMap.student.initTable(5)
            .userName.formatAutoIncrease("student_no_%d")
            .env.values("test_env")
            .cleanAndInsert();

        StdPagedList<StudentEntity> paged = StudentQuery.query().where.id().lt(2000).end()
            .limit(form, limit)
            .to().stdPagedEntity();
        want.bool(paged.hasNext()).is(expected);
    }

    static DataProvider data4hasNextPage() {
        return new DataProvider()
            .data(0, 4, true)
            .data(0, 5, false)
            .data(0, 6, false)
            .data(1, 4, false)
            .data(3, 1, true)
            .data(3, 2, false)
            .data(3, 3, false)
            ;
    }
}
