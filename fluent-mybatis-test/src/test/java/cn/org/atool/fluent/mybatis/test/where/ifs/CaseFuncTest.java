package cn.org.atool.fluent.mybatis.test.where.ifs;

import cn.org.atool.fluent.mybatis.customize.mapper.StudentBatchMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentUpdate;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

import java.util.Arrays;
import java.util.List;

import static cn.org.atool.fluent.mybatis.utility.PoJoHelper.getFields;

public class CaseFuncTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Autowired
    private StudentBatchMapper batchMapper;

    @Test
    public void test_applyFunc() throws Exception {
        StudentUpdate update = StudentUpdate.emptyUpdater()
            .set.address().applyFunc("case id " +
                "when 1 then 'address 1' " +
                "when 2 then 'address 2' " +
                "else 'address 3' end")
            .end()
            .where.id().eq(2).end();
        mapper.updateBy(update);
        // 验证SQL语句
        db.sqlList().wantFirstSql()
            .eq("UPDATE fluent_mybatis.student " +
                    "SET `gmt_modified` = now(), " +
                    "`address` = case id when 1 then 'address 1' when 2 then 'address 2' else 'address 3' end " +
                    "WHERE `id` = ?",
                StringMode.SameAsSpace);
    }

    @Test
    public void test_mybatis_batch() {
        batchMapper.updateBatchByIds(Arrays.asList(
            new StudentEntity().setId(1L).setAddress("address 1").setAge(23),
            new StudentEntity().setId(2L).setAddress("address 2").setAge(24),
            new StudentEntity().setId(3L).setAddress("address 3").setAge(25)
        ));
        /** 验证执行的SQL语句 **/
        db.sqlList().wantFirstSql().eq("" +
                "update student " +
                "set address =case id when ? then ? when ? then ? when ? then ? end, " +
                "age =case id when ? then ? when ? then ? when ? then ? end " +
                "where id in ( ? , ? , ? )"
            , StringMode.SameAsSpace);
    }

    @Test
    public void test_fluentMybatisBatch() throws Exception {
        final String CaseWhen = "case id " +
            "when 1 then ? " +
            "when 2 then ? " +
            "else ? end";
        StudentUpdate update = StudentUpdate.emptyUpdater()
            .set.address().applyFunc(CaseWhen, "address 1", "address 2", "address 3")
            .set.age().applyFunc(CaseWhen, 23, 24, 25)
            .end()
            .where.id().in(new long[]{1L, 2L, 3L}).end();
        mapper.updateBy(update);
        // 验证SQL语句
        db.sqlList().wantFirstSql()
            .eq("UPDATE fluent_mybatis.student " +
                    "SET `gmt_modified` = now(), " +
                    "`address` = case id when 1 then ? when 2 then ? else ? end, " +
                    "`age` = case id when 1 then ? when 2 then ? else ? end " +
                    "WHERE `id` IN (?, ?, ?)",
                StringMode.SameAsSpace);
        // 验证参数
        db.sqlList().wantFirstPara()
            .eqReflect(new Object[]{"address 1", "address 2", "address 3", 23, 24, 25, 1L, 2L, 3L});
    }

    @Test
    public void test_fluentMybatisBatch2() throws Exception {
        List<StudentEntity> students = Arrays.asList(
            new StudentEntity().setId(1L).setAddress("address 1").setAge(23),
            new StudentEntity().setId(2L).setAddress("address 2").setAge(24),
            new StudentEntity().setId(3L).setAddress("address 3").setAge(25));
        final String CaseWhen = "case id " +
            "when 1 then ? " +
            "when 2 then ? " +
            "else ? end";
        StudentUpdate update = StudentUpdate.emptyUpdater()
            .set.address().applyFunc(CaseWhen, getFields(students, StudentEntity::getAddress))
            .set.age().applyFunc(CaseWhen, getFields(students, StudentEntity::getAge))
            .end()
            .where.id().in(getFields(students, StudentEntity::getId)).end();
        mapper.updateBy(update);
        // 验证SQL语句
        db.sqlList().wantFirstSql()
            .eq("UPDATE fluent_mybatis.student " +
                    "SET `gmt_modified` = now(), " +
                    "`address` = case id when 1 then ? when 2 then ? else ? end, " +
                    "`age` = case id when 1 then ? when 2 then ? else ? end " +
                    "WHERE `id` IN (?, ?, ?)",
                StringMode.SameAsSpace);
        // 验证参数
        db.sqlList().wantFirstPara()
            .eqReflect(new Object[]{"address 1", "address 2", "address 3", 23, 24, 25, 1L, 2L, 3L});
    }
}
