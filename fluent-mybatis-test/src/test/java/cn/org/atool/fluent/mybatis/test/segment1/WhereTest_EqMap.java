package cn.org.atool.fluent.mybatis.test.segment1;

import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.helper.StudentMapping;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * WhereTest_EqMap
 *
 * @author darui.wu
 * @create 2020/6/21 3:40 下午
 */
public class WhereTest_EqMap extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_eq_map() throws Exception {
        StudentQuery query = new StudentQuery()
            .selectId()
            .where.eqNotNull(new HashMap<String, Object>() {
                {
                    this.put(StudentMapping.userName.column, "user1");
                }
            }).end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT `id` FROM student WHERE `user_name` = ?");
    }

    @Test
    public void test_eq_entity() throws Exception {
        StudentQuery query = new StudentQuery()
            .selectId()
            .where.eqNotNull(new StudentEntity().setUserName("u2")).end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT `id` FROM student WHERE `user_name` = ?");
    }
}
