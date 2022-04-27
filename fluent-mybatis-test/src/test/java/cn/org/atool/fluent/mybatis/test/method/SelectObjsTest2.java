package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.Ref;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author darui.wu 2019/10/29 9:33 下午
 */
public class SelectObjsTest2 extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_selectObjs() {
        ATM.dataMap.student.initTable(4)
            .id.values(23, 24, 25, 26)
            .userName.values("u1", "u2", "u3", "u2")
            .cleanAndInsert();
        StudentQuery query = StudentQuery.emptyQuery()
            .select.apply(Ref.Field.Student.userName)
            .end()
            .where.id().eq(24L)
            .end();
        List<String> users = mapper.listObjs(query);
        db.sqlList().wantFirstSql().start("SELECT")
            .end("FROM fluent_mybatis.student WHERE `id` = ?");
        want.list(users).eqReflect(new String[]{"u2"});
    }

    @Test
    public void test_selectObjs_hasMultiple() {
        ATM.dataMap.student.initTable(4)
            .id.values(23, 24, 25, 26)
            .userName.values("u1", "u2", "u3", "u2")
            .cleanAndInsert();
        StudentQuery query = StudentQuery.emptyQuery()
            .select.userName().age().end()
            .where.userName().eq("u2")
            .end();
        List<String> users = mapper.listObjs(query);
        db.sqlList().wantFirstSql().start("SELECT")
            .end("FROM fluent_mybatis.student WHERE `user_name` = ?");
        want.list(users).eqReflect(new String[]{"u2", "u2"});
    }
}
