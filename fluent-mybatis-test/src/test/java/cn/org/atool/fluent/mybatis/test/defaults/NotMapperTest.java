package cn.org.atool.fluent.mybatis.test.defaults;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class NotMapperTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void existPK() {
        ATM.DataMap.student.table().clean();
        want.bool(mapper.existPk(1L)).is(false);
        ATM.DataMap.student.table(1).id.values(1).insert();
        want.bool(mapper.existPk(1L)).is(true);
        db.sqlList().wantFirstSql().eq("SELECT COUNT(*) FROM student WHERE id = ? LIMIT ?, ?");
    }

    @DisplayName("验证save()和insert()方法")
    @Test
    public void save() {
        ATM.DataMap.student.table().clean();
        mapper.save(new StudentEntity().setId(1L).setUserName("test1"));
        ATM.DataMap.student.table(1)
            .userName.values("test1")
            .tenant.values(234567)
            .env.values("test_env")
            .eqQuery("id=1");
        db.sqlList().wantFirstSql().eq("INSERT INTO student(id, gmt_created, gmt_modified, is_deleted, env, tenant, user_name) VALUES (?, now(), now(), 0, ?, ?, ?)");

        mapper.insert(new StudentEntity().setId(2L).setUserName("test1"));
        ATM.DataMap.student.table(1)
            .userName.values("test1")
            .tenant.values(null)
            .env.values(null)
            .eqQuery("id=2");
        db.sqlList().wantSql(1).eq("INSERT INTO student(id, gmt_created, gmt_modified, is_deleted, env, tenant, user_name) VALUES (?, now(), now(), 0, ?, ?, ?)");
    }

    @Test
    public void saveOrUpdate() {
        ATM.DataMap.student.table().clean();
        StudentEntity student = new StudentEntity().setUserName("test1");
        mapper.saveOrUpdate(student);
        ATM.DataMap.student.table(1)
            .userName.values("test1")
            .tenant.values(234567)
            .env.values("test_env")
            .eqTable();

        mapper.saveOrUpdate(student.setUserName("test2"));
        ATM.DataMap.student.table(1)
            .userName.values("test2")
            .eqTable();
        mapper.saveOrUpdate(student.setUserName("test1").setId(student.getId() + 1));
        ATM.DataMap.student.table(2).userName.values("test2", "test1").eqTable();
    }
}