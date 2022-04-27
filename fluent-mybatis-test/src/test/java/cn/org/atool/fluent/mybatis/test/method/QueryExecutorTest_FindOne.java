package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.Ref;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@SuppressWarnings("all")
public class QueryExecutorTest_FindOne extends BaseTest {
    @Test
    void findOne() {
        ATM.dataMap.student.table().clean();
        Object o = Ref.Query.student.query().to().findOne().orElse(null);
        want.object(o).isNull();
        db.sqlList().wantFirstSql().end("" +
            "FROM fluent_mybatis.student WHERE `is_deleted` = ? AND `env` = ?");
    }

    @Test
    void findOne_1() {
        ATM.dataMap.student.table(1)
            .userName.values("test1")
            .cleanAndInsert();
        StudentEntity o = Ref.Query.student.emptyQuery()
            .where.userName().eq("test1").end()
            .to().findOne(StudentEntity.class).orElse(null);
        want.object(o.getUserName()).eq("test1");
        db.sqlList().wantFirstSql().end("FROM fluent_mybatis.student WHERE `user_name` = ?");
    }

    @Test
    void findOne_2() {
        ATM.dataMap.student.table(1)
            .userName.values("test1")
            .cleanAndInsert();
        String o = Ref.Query.student.emptyQuery()
            .where.userName().eq("test1").end()
            .to().findOne(StudentEntity::getUserName).orElse(null);
        want.object(o).eq("test1");
        db.sqlList().wantFirstSql().end("FROM fluent_mybatis.student WHERE `user_name` = ?");
    }

    @Test
    void findOne_3() {
        ATM.dataMap.student.table(1)
            .userName.values("test1")
            .cleanAndInsert();
        StudentEntity o = Ref.Query.student.emptyQuery()
            .where.userName().eq("test1").end()
            .to().findOneMap(map -> new StudentEntity()
                .setUserName((String) map.get("user_name"))
            ).orElse(null);

        want.object(o.getUserName()).eq("test1");
        db.sqlList().wantFirstSql().end("FROM fluent_mybatis.student WHERE `user_name` = ?");
    }

    @Autowired
    StudentMapper mapper;

    @Test
    void findOne_33() {
        // 准备测试数据
        ATM.dataMap.student.table(1)
            .userName.values("test1")
            .cleanAndInsert();
        // 构造条件
        StudentQuery query = StudentQuery.emptyQuery()
            .where.userName().eq("test1").end();

        StudentEntity o = mapper.findOne(query,
            map -> new StudentEntity()
                .setUserName((String) map.get("user_name"))
        ).orElse(null);
        // 验证结果
        want.object(o.getUserName()).eq("test1");
        db.sqlList().wantFirstSql().end("FROM fluent_mybatis.student WHERE `user_name` = ?");
    }


    @Test
    void findOne_4() {
        ATM.dataMap.student.table(1)
            .userName.values("test1")
            .cleanAndInsert();
        Map<String, Object> map = Ref.Query.student.emptyQuery()
            .where.userName().eq("test1").end()
            .to().findOneMap().orElse(null);
        want.object(map.get("user_name")).eq("test1");
        db.sqlList().wantFirstSql().end("FROM fluent_mybatis.student WHERE `user_name` = ?");
    }
}
