package cn.org.atool.fluent.mybatis.test.free;

import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.free.FreeQuery;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.GE;
import static cn.org.atool.fluent.mybatis.base.model.SqlOp.LIKE;

@SuppressWarnings("rawtypes")
public class UnionQueryTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    void union() {
        IQuery query = new FreeQuery("student")
            .select.apply("id", "user_name").end()
            .where.and(q -> q
                .where.apply("user_name", LIKE, "1%")
                .or.apply("age", GE, 20).end()
            ).end()
            .union(
                StudentQuery.emptyQuery().select.id().userName().end()
                    .where.userName().endWith("2").end()
            );
        mapper.listObjs(query);
        db.sqlList().wantFirstSql().eq("" +
                "(SELECT `id`, `user_name` FROM `student` WHERE (`user_name` LIKE ? OR `age` >= ?)) " +
                "UNION " +
                "(SELECT `id`, `user_name` FROM fluent_mybatis.student WHERE `user_name` LIKE ?)"
            , StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eq(new Object[]{"1%", 20, "%2"});
    }

    @Test
    void unionAll() {
        IQuery query = new FreeQuery("student")
            .select.apply("id", "user_name").end()
            .where.and(q -> q
                .where.apply("user_name", LIKE, "1%")
                .or.apply("age", GE, 20).end()
            ).end()
            .unionAll(
                StudentQuery.emptyQuery().select.id().userName().end()
                    .where.userName().endWith("2").end()
            );
        mapper.listObjs(query);
        db.sqlList().wantFirstSql().eq("" +
            "(SELECT `id`, `user_name` FROM `student` WHERE (`user_name` LIKE ? OR `age` >= ?)) " +
            "UNION ALL " +
            "(SELECT `id`, `user_name` FROM fluent_mybatis.student WHERE `user_name` LIKE ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqList("1%", 20, "%2");
    }
}
