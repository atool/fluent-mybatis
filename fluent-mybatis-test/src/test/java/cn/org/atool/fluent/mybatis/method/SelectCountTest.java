package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author darui.wu
 * @create 2019/10/29 9:33 下午
 */
public class SelectCountTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_selectCount_null() throws Exception {
        want.exception(() -> mapper.count(null), MyBatisSystemException.class)
            .contains("param[ew] not found");
    }

    @Test
    public void test_selectCount() throws Exception {
        ATM.DataMap.student.initTable(4)
            .id.values(23, 24, 25, 26)
            .userName.values("u1", "u2", "u3", "u2")
            .cleanAndInsert();
        StudentQuery query = new StudentQuery()
            .where.id().eq(24L).end();
        int count = mapper.count(query);
        db.sqlList().wantFirstSql().start("SELECT COUNT(*)").end("FROM student WHERE id = ?");
        want.number(count).eq(1);
    }

    @Test
    public void test_selectCount_hasMultiple() throws Exception {
        ATM.DataMap.student.initTable(4)
            .id.values(23, 24, 25, 26)
            .userName.values("u1", "u2", "u3", "u2")
            .cleanAndInsert();
        StudentQuery query = new StudentQuery()
            .selectId()
            .where.userName().eq("u2").end();
        int count = mapper.count(query);
        db.sqlList().wantFirstSql().start("SELECT COUNT(id)").end("FROM student WHERE user_name = ?");
        want.number(count).eq(2);
    }

    @Test
    public void test_selectCount_limit() throws Exception {
        ATM.DataMap.student.initTable(4)
            .id.values(23, 24, 25, 26)
            .userName.values("u1", "u2", "u3", "u2")
            .cleanAndInsert();
        StudentQuery query = new StudentQuery()
            .selectId()
            .where.userName().eq("u2").end()
            .limit(2);
        int count = mapper.count(query);
        db.sqlList().wantFirstSql().start("SELECT COUNT(id)").end("FROM student WHERE user_name = ? LIMIT ?, ?");
        want.number(count).eq(2);
    }
}
