package cn.org.atool.fluent.mybatis.test.join;

import cn.org.atool.fluent.mybatis.base.crud.JoinBuilder;
import cn.org.atool.fluent.mybatis.generator.shared2.Ref;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.HomeAddressQuery;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static java.lang.String.format;

public class JoinApplyTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_join() {
        StudentQuery studentQuery = Ref.Query.student.alias();
        String alias1 = studentQuery.getTableAlias();
        studentQuery.select.age().end()
            .where.age().isNull().end()
            .groupBy.age().apply(alias1 + ".id").end()
            .having.max.age().gt(1L).end();
        HomeAddressQuery addressQuery = Ref.Query.homeAddress.alias()
            .select.studentId().end()
            .where.address().like("vas").end()
            .groupBy.studentId().end();

        String alias2 = addressQuery.getTableAlias();

        JoinBuilder<StudentQuery> query = JoinBuilder
            .from(studentQuery)
            .join(addressQuery)
            .onApply(format("%s.id = %s.id OR %s.age = %s.student_id", alias1, alias2, alias1, alias2))
            .endJoin()
            .limit(20);
        mapper.listMaps(query.build());
        db.sqlList().wantFirstSql().eq(
            format("SELECT %s.`age`, %s.`student_id` ", alias1, alias2) +
                format("FROM fluent_mybatis.student %s ", alias1) +
                format("JOIN `home_address` %s ", alias2) +
                format("ON %s.id = %s.id ", alias1, alias2) +
                format("OR %s.age = %s.student_id ", alias1, alias2) +
                format("WHERE %s.`is_deleted` = ? ", alias1) +
                format("AND %s.`env` = ? ", alias1) +
                format("AND %s.`age` IS NULL ", alias1) +
                format("AND %s.`is_deleted` = ? ", alias2) +
                format("AND %s.`env` = ? ", alias2) +
                format("AND %s.`address` LIKE ? ", alias2) +
                format("GROUP BY %s.`age`, %s.id, %s.`student_id` ", alias1, alias1, alias2) +
                format("HAVING MAX(%s.`age`) > ? ", alias1) +
                "LIMIT ?, ?");
    }
}
