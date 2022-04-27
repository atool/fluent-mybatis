package cn.org.atool.fluent.mybatis.test.where;

import cn.org.atool.fluent.mybatis.generator.shared2.Ref;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * WhereTest_EqMap
 *
 * @author darui.wu 2020/6/21 3:40 下午
 */
public class WhereTest_EqMap extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_eq_map() {
        StudentQuery query = StudentQuery.emptyQuery()
            .selectId()
            .where.eqNotNull(new HashMap<String, Object>() {
                {
                    this.put(Ref.Field.Student.userName.column, "user1");
                }
            }).end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT `id` FROM fluent_mybatis.student WHERE `user_name` = ?");
    }

    @Test
    public void test_eq_entity() {
        StudentQuery query = StudentQuery.emptyQuery()
            .selectId()
            .where.eqByEntity(new StudentEntity().setUserName("u2")).end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT `id` FROM fluent_mybatis.student WHERE `user_name` = ?");
    }
}
