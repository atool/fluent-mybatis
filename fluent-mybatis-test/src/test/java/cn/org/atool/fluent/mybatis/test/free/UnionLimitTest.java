package cn.org.atool.fluent.mybatis.test.free;

import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

public class UnionLimitTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    void unionAll() {
        IQuery query = StudentQuery.emptyQuery()
            .selectId()
            .where.id().lt(20).end()
            .limit(100)
            .unionAll(StudentQuery.emptyQuery()
                .select.id().end()
                .where.userName().endWith("2").end()
                .limit(50)
            );
        mapper.listObjs(query);
        db.sqlList().wantFirstSql().eq("" +
            "(SELECT `id` FROM fluent_mybatis.student WHERE `id` < ? LIMIT ?, ?) " +
            "UNION ALL " +
            "(SELECT `id` FROM fluent_mybatis.student WHERE `user_name` LIKE ? LIMIT ?, ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqList(20, 0, 100, "%2", 0, 50);
    }
}
