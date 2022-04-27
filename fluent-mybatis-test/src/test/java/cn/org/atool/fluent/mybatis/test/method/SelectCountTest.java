package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author darui.wu 2019/10/29 9:33 下午
 */
public class SelectCountTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_selectCount_null() {
        want.exception(() -> mapper.count(null), MyBatisSystemException.class)
            .contains("param[ew] not found");
    }

    @Test
    public void test_selectCount() {
        ATM.dataMap.student.initTable(4)
            .id.values(23, 24, 25, 26)
            .userName.values("u1", "u2", "u3", "u2")
            .cleanAndInsert();
        StudentQuery query = StudentQuery.emptyQuery()
            .where.id().eq(24L).end();
        int count = mapper.count(query);
        db.sqlList().wantFirstSql().start("SELECT COUNT(*)").end("FROM fluent_mybatis.student WHERE `id` = ?");
        want.number(count).eq(1);
    }

    @Test
    public void test_selectCount_hasMultiple() {
        ATM.dataMap.student.initTable(4)
            .id.values(23, 24, 25, 26)
            .userName.values("u1", "u2", "u3", "u2")
            .cleanAndInsert();
        StudentQuery query = StudentQuery.emptyQuery()
            .select.id().userName().end()
            .where.userName().eq("u2").end();
        int count = mapper.count(query);
        db.sqlList().wantFirstSql()
            .start("SELECT COUNT(*)")
            .end("FROM fluent_mybatis.student WHERE `user_name` = ?");
        want.number(count).eq(2);
    }

    @Test
    public void test_selectCount_limit() {
        ATM.dataMap.student.initTable(4)
            .id.values(23, 24, 25, 26)
            .userName.values("u1", "u2", "u3", "u2")
            .cleanAndInsert();
        StudentQuery query = StudentQuery.emptyQuery()
            .selectId()
            .where.userName().eq("u2").end()
            .limit(2);
        int count = mapper.count(query);
        db.sqlList().wantFirstSql()
            .start("SELECT COUNT(`id`)")
            .end("FROM fluent_mybatis.student WHERE `user_name` = ? LIMIT ?, ?");
        want.number(count).eq(2);
    }
}
