package cn.org.atool.fluent.mybatis.test.join;

import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentScoreQuery;
import cn.org.atool.fluent.mybatis.metadata.JoinType;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ISSUE_I4FH25_Test extends BaseTest {
    @Autowired
    StudentMapper mapper;

    /**
     * https://gitee.com/fluent-mybatis/fluent-mybatis/issues/I4FH25
     */
    @DisplayName("生成连表查询语句的时候, 如果有嵌套sql, 嵌套的sql里的查询条件指定表别名")
    @Test
    void test() {
        StudentQuery leftQuery = new StudentQuery()
            .where.status().eq(1)
            .and(q -> q.where.userName().like("xxxx").or.age().eq(23).end())
            .end();
        StudentScoreQuery rightQuery = new StudentScoreQuery()
            .where.subject().like("yuwen")
            .end();

        mapper.listMaps(leftQuery.join(JoinType.LeftJoin, rightQuery)
            .on(l -> l.where.id(), r -> r.where.studentId()).endJoin().build());
        String a1 = leftQuery.getTableAlias();
        db.sqlList().wantFirstSql().contains(String.format("AND (%s.`user_name` LIKE ? OR %s.`age` = ?) AND", a1, a1));
    }
}
