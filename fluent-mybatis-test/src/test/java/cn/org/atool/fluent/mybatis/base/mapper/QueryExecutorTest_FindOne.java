package cn.org.atool.fluent.mybatis.base.mapper;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.refs.QueryRef;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class QueryExecutorTest_FindOne extends BaseTest {
    @Test
    void findOne() {
        ATM.dataMap.student.table().clean();
        Object o = QueryRef.student.defaultQuery().to().findOne().orElse(null);
        want.object(o).isNull();
        db.sqlList().wantFirstSql().end("FROM student WHERE is_deleted = ? AND env = ?");
    }

    @Test
    void findOne_1() {
        ATM.dataMap.student.table(1)
            .userName.values("test1")
            .cleanAndInsert();
        StudentEntity o = QueryRef.student.query()
            .where.userName().eq("test1").end()
            .to().findOne(StudentEntity.class).orElse(null);
        want.object(o.getUserName()).eq("test1");
        db.sqlList().wantFirstSql().end("FROM student WHERE user_name = ?");
    }

    @Test
    void findOne_2() {
        ATM.dataMap.student.table(1)
            .userName.values("test1")
            .cleanAndInsert();
        String o = QueryRef.student.query()
            .where.userName().eq("test1").end()
            .to().findOne(StudentEntity::getUserName).orElse(null);
        want.object(o).eq("test1");
        db.sqlList().wantFirstSql().end("FROM student WHERE user_name = ?");
    }

    @Test
    void findOne_3() {
        ATM.dataMap.student.table(1)
            .userName.values("test1")
            .cleanAndInsert();
        StudentEntity o = QueryRef.student.query()
            .where.userName().eq("test1").end()
            .to().findOneMap(map -> new StudentEntity()
                .setUserName((String) map.get("user_name"))
            ).orElse(null);
        want.object(o.getUserName()).eq("test1");
        db.sqlList().wantFirstSql().end("FROM student WHERE user_name = ?");
    }

    @Test
    void findOne_4() {
        ATM.dataMap.student.table(1)
            .userName.values("test1")
            .cleanAndInsert();
        Map<String, Object> map = QueryRef.student.query()
            .where.userName().eq("test1").end()
            .to().findOneMap().orElse(null);
        want.object(map.get("user_name")).eq("test1");
        db.sqlList().wantFirstSql().end("FROM student WHERE user_name = ?");
    }
}