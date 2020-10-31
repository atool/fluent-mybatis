package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
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
public class SelectOneTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_selectOne() throws Exception {
        db.table(ATM.Table.student).clean()
            .insert(ATM.DataMap.student.initTable(4)
                .id.values(23, 24, 25, 26)
                .userName.values("u1", "u2", "u3", "u2")
            );
        StudentQuery query = new StudentQuery()
            .where.id().eq(24L).end();
        StudentEntity student = mapper.findOne(query);
        db.sqlList().wantFirstSql().start("SELECT").end("FROM t_student WHERE id = ?");
        want.object(student).eqDataMap(ATM.DataMap.student.entity(1)
            .userName.values("u2"));
    }

    @Test
    public void test_selectOne_hasMultiple() throws Exception {
        db.table(ATM.Table.student).clean()
            .insert(ATM.DataMap.student.initTable(4)
                .id.values(23, 24, 25, 26)
                .userName.values("u1", "u2", "u3", "u2")
            );
        StudentQuery query = new StudentQuery()
            .where.userName().eq("u2").end();
        want.exception(() -> mapper.findOne(query), MyBatisSystemException.class)
            .contains("Expected one result (or null) to be returned by selectOne(), but found: 2");
        db.sqlList().wantFirstSql().start("SELECT").end("FROM t_student WHERE user_name = ?");
    }
}
