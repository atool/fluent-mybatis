package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EqByEntityTest_BiPredicate extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_eq_entity() {
        StudentEntity entity = new StudentEntity().setUserName("u2");
        StudentQuery query = StudentQuery.emptyQuery()
            .selectId()
            .where.eqByEntity(entity, (column, value) -> {
                /* 可以根据 column, value自行做判断是否当做条件字段 */
                return column.equals("user_name") || column.equals("version");
            })
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT `id` " +
            "FROM fluent_mybatis.student " +
            "WHERE `user_name` = ? " +
            "AND `version` IS NULL");
    }
}
