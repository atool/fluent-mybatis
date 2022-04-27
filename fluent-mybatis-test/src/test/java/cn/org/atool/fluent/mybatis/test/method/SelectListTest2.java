package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @author darui.wu
 * @create 2019/10/29 9:33 下午
 */
public class SelectListTest2 extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_selectList() throws Exception {
        ATM.dataMap.student.initTable(4)
            .id.values(23, 24, 25, 26)
            .userName.values("u1", "u2", "u3", "u2")
            .cleanAndInsert();
        StudentQuery query = StudentQuery.emptyQuery()
            .where.id().eq(24L).end();
        List<StudentEntity> users = mapper.listEntity(query);
        db.sqlList().wantFirstSql().start("SELECT")
            .end("FROM fluent_mybatis.student WHERE `id` = ?");
        want.list(users).eqDataMap(ATM.dataMap.student.entity(1)
            .userName.values("u2"));
    }

    @Test
    public void test_selectList_hasMultiple() throws Exception {
        ATM.dataMap.student.initTable(4)
            .id.values(23, 24, 25, 26)
            .userName.values("u1", "u2", "u3", "u2")
            .cleanAndInsert();
        StudentQuery query = StudentQuery.emptyQuery()
            .where.userName().eq("u2").end();
        List<StudentEntity> users = mapper.listEntity(query);
        db.sqlList().wantFirstSql().start("SELECT")
            .end("FROM fluent_mybatis.student WHERE `user_name` = ?");
        want.list(users).eqDataMap(ATM.dataMap.student.entity(2)
            .userName.values("u2"));
    }

    @Test
    public void test_selectList_limit() throws Exception {
        ATM.dataMap.student.initTable(4)
            .id.values(23, 24, 25, 26)
            .userName.values("u1", "u2", "u3", "u2")
            .cleanAndInsert();
        StudentQuery query = StudentQuery.emptyQuery()
            .where.userName().eq("u2").end()
            .limit(2);
        List<StudentEntity> users = mapper.listEntity(query);
        want.list(users).eqDataMap(ATM.dataMap.student.entity(2)
            .userName.values("u2"));
        db.sqlList().wantFirstSql().start("SELECT")
            .end("FROM fluent_mybatis.student WHERE `user_name` = ? LIMIT ?, ?");
    }

    @Test
    public void test_selectList_limit2() throws Exception {
        ATM.dataMap.student.initTable(4)
            .id.values(23, 24, 25, 26)
            .userName.values("u1", "u2", "u3", "u2")
            .cleanAndInsert();
        StudentQuery query = StudentQuery.emptyQuery()
            .where.userName().eq("u2").end()
            .limit(2, 3);
        List<StudentEntity> users = mapper.listEntity(query);
        db.sqlList().wantFirstSql().start("SELECT")
            .end("FROM fluent_mybatis.student WHERE `user_name` = ? LIMIT ?, ?");
        db.sqlList().wantFirstPara().eq(new Object[]{"u2", 2, 3});
    }

    @Test
    public void test_selectList_withTimeRange() throws Exception {
        ATM.dataMap.student.initTable(4)
            .id.values(23, 24, 25, 26)
            .gmtCreated.values(new Date(1604140000000L), new Date(1604150000000L), new Date(1604160000000L), new Date(1604170000000L))
            .cleanAndInsert();
        StudentQuery query = StudentQuery.emptyQuery()
            .where.gmtCreated().gt(new Date(1604140000000L))
            .and.gmtCreated().le(new Date(1604170000000L)).end();
        List<StudentEntity> users = mapper.listEntity(query);
        db.sqlList().wantFirstSql().start("SELECT")
            .end("FROM fluent_mybatis.student WHERE `gmt_created` > ? AND `gmt_created` <= ?");
        want.list(users).eqDataMap(ATM.dataMap.student.entity(3));
    }
}
