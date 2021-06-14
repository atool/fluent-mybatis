package cn.org.atool.fluent.mybatis.customized;

import cn.org.atool.fluent.mybatis.base.splice.FreeQuery;
import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CustomizedQueryTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    void test_count() {
        FreeQuery query = new FreeQuery(null)
            .customizedByPlaceholder("" +
                "select count(1) from student " +
                "where id < #{value}", 10);
        mapper.count(query);
    }

    @Test
    void test_list_entity() {
        ATM.dataMap.student.table(3)
            .userName.values("xyz1", "xyz2", "xyz3")
            .age.values(19, 45, 55)
            .cleanAndInsert();
        FreeQuery query = new FreeQuery(null)
            .customizedByPlaceholder("" +
                    "select * from student " +
                    "where user_name like #{userName} " +
                    "and age > #{age}",
                new StudentEntity()
                    .setUserName("xyz%")
                    .setAge(20));
        List<StudentEntity> list = mapper.listEntity(query);
        db.sqlList().wantFirstSql().eq("" +
            "select * from student where user_name like ? and age > ?");
        db.sqlList().wantFirstPara().eq(new Object[]{"xyz%", 20});
        want.list(list).eqByProperties("userName",
            new String[]{"xyz2", "xyz3"});
    }

    @Test
    void test_list_maps() {
        FreeQuery query = new FreeQuery(null)
            .customizedByQuestion("" +
                    "select * from student " +
                    "where user_name like ? " +
                    "and age > ?",
                "xyz%", 20);
        mapper.listMaps(query);
        db.sqlList().wantFirstSql().eq("" +
            "select * from student where user_name like ? and age > ?");
        db.sqlList().wantFirstPara().eq(new Object[]{"xyz%", 20});
    }

    @Test
    void test_list_objs() {
        FreeQuery query = new FreeQuery(null)
            .customizedByQuestion("" +
                    "select * from student " +
                    "where user_name like ? " +
                    "and age > ?",
                "xyz%", 20);
        mapper.listObjs(query);
        db.sqlList().wantFirstSql().eq("" +
            "select * from student where user_name like ? and age > ?");
        db.sqlList().wantFirstPara().eq(new Object[]{"xyz%", 20});
    }


    @Test
    void test_findOne() {
        FreeQuery query = new FreeQuery(null)
            .customizedByQuestion("" +
                    "select * from student " +
                    "where user_name like ? " +
                    "and age > ?",
                "xyz%", 20);
        mapper.findOne(query);
        db.sqlList().wantFirstSql().eq("" +
            "select * from student where user_name like ? and age > ?");
        db.sqlList().wantFirstPara().eq(new Object[]{"xyz%", 20});
    }
}
