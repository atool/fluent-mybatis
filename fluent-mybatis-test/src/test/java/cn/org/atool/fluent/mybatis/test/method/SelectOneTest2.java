package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author darui.wu
 * @create 2019/10/29 9:33 下午
 */
public class SelectOneTest2 extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_selectOne() throws Exception {
        ATM.dataMap.student.initTable(4)
            .id.values(23, 24, 25, 26)
            .userName.values("u1", "u2", "u3", "u2")
            .cleanAndInsert();
        StudentQuery query = StudentQuery.emptyQuery()
            .where.id().eq(24L).end();
        StudentEntity student = mapper.findOne(query);
        db.sqlList().wantFirstSql().start("SELECT")
            .end("FROM fluent_mybatis.student WHERE `id` = ?");
        want.object(student).eqDataMap(ATM.dataMap.student.entity(1)
            .userName.values("u2"));
    }

    @Test
    void query_for_update() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.id().eq(24L).end()
            .last("for update");
        StudentEntity student = mapper.findOne(query);
        db.sqlList().wantFirstSql()
            .end("FROM fluent_mybatis.student WHERE `id` = ? for update");
    }

    @Test
    public void test_selectOne_hasMultiple() throws Exception {
        ATM.dataMap.student.initTable(4)
            .id.values(23, 24, 25, 26)
            .userName.values("u1", "u2", "u3", "u2")
            .cleanAndInsert();
        StudentQuery query = StudentQuery.emptyQuery()
            .where.userName().eq("u2").end();
        want.exception(() -> mapper.findOne(query), FluentMybatisException.class)
            .contains("Expected one result (or null) to be returned, but found 2 results.");
        db.sqlList().wantFirstSql().start("SELECT")
            .end("FROM fluent_mybatis.student WHERE `user_name` = ?");
    }
}
