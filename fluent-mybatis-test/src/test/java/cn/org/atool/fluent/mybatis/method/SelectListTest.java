package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @author darui.wu
 * @create 2019/10/29 9:33 下午
 */
public class SelectListTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_selectList() throws Exception {
        db.table(ATM.Table.student).clean()
            .insert(ATM.DataMap.student.initTable(4)
                .id.values(23, 24, 25, 26)
                .userName.values("u1", "u2", "u3", "u2")
            );
        StudentQuery query = new StudentQuery()
            .where.id().eq(24L).end();
        List<StudentEntity> users = mapper.listEntity(query);
        db.sqlList().wantFirstSql().start("SELECT").end("FROM t_student WHERE id = ?");
        want.list(users).eqDataMap(ATM.DataMap.student.entity(1)
            .userName.values("u2"));
    }

    @Test
    public void test_selectList_hasMultiple() throws Exception {
        db.table(ATM.Table.student).clean()
            .insert(ATM.DataMap.student.initTable(4)
                .id.values(23, 24, 25, 26)
                .userName.values("u1", "u2", "u3", "u2")
            );
        StudentQuery query = new StudentQuery()
            .where.userName().eq("u2").end();
        List<StudentEntity> users = mapper.listEntity(query);
        db.sqlList().wantFirstSql().start("SELECT").end("FROM t_student WHERE user_name = ?");
        want.list(users).eqDataMap(ATM.DataMap.student.entity(2)
            .userName.values("u2"));
    }

    @Test
    public void test_selectList_limit() throws Exception {
        db.table(ATM.Table.student).clean()
            .insert(ATM.DataMap.student.initTable(4)
                .id.values(23, 24, 25, 26)
                .userName.values("u1", "u2", "u3", "u2")
            );
        StudentQuery query = new StudentQuery()
            .where.userName().eq("u2").end()
            .limit(2);
        List<StudentEntity> users = mapper.listEntity(query);
        want.list(users).eqDataMap(ATM.DataMap.student.entity(2)
            .userName.values("u2"));
        db.sqlList().wantFirstSql().start("SELECT").end("FROM t_student WHERE user_name = ? LIMIT ?, ?");
    }

    @Test
    public void test_selectList_limit2() throws Exception {
        db.table(ATM.Table.student).clean()
            .insert(ATM.DataMap.student.initTable(4)
                .id.values(23, 24, 25, 26)
                .userName.values("u1", "u2", "u3", "u2")
            );
        StudentQuery query = new StudentQuery()
            .where.userName().eq("u2").end()
            .limit(2, 3);
        List<StudentEntity> users = mapper.listEntity(query);
        db.sqlList().wantFirstSql().start("SELECT").end("FROM t_student WHERE user_name = ? LIMIT ?, ?");
        db.sqlList().wantFirstPara().eq(new Object[]{"u2", 2, 3});
    }

    @Test
    public void test_selectList_withTimeRange() throws Exception {
        db.table(ATM.Table.student).clean()
            .insert(ATM.DataMap.student.initTable(4)
                .id.values(23, 24, 25, 26)
                .gmtCreated.values(new Date(1604140000000L), new Date(1604150000000L), new Date(1604160000000L), new Date(1604170000000L))
            );
        StudentQuery query = new StudentQuery()
            .where.gmtCreated().gt(new Date(1604140000000L))
            .and.gmtCreated().le(new Date(1604170000000L)).end();
        List<StudentEntity> users = mapper.listEntity(query);
        db.sqlList().wantFirstSql().start("SELECT").end("FROM t_student WHERE gmt_created > ? AND gmt_created <= ?");
        want.list(users).eqDataMap(ATM.DataMap.student.entity(3));
    }
}
