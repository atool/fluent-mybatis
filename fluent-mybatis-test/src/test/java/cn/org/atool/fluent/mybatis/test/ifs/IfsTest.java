package cn.org.atool.fluent.mybatis.test.ifs;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentUpdate;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

import java.util.Arrays;
import java.util.List;

public class IfsTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_multiIf() throws Exception {
        long id = 2L;
        StudentUpdate update = new StudentUpdate()
            .set.address().is("address 1", v -> id == 1)
            .set.address().is("address 2", v -> id == 2)
            .set.address().is("address 3", v -> id == 3)
            .end()
            .where.id().eq(id).end();

        mapper.updateBy(update);
        // 验证SQL语句
        db.sqlList().wantFirstSql().eq("" +
                "UPDATE fluent_mybatis.student " +
                "SET `gmt_modified` = now(), `address` = ? " +
                "WHERE `id` = ?",
            StringMode.SameAsSpace);
        // 验证参数
        db.sqlList().wantFirstPara().eqReflect(new Object[]{"address 2", 2L});
    }

    @Test
    public void test_Ifs() throws Exception {
        long id = 2L;
        StudentUpdate update = new StudentUpdate()
            .set.address().is(If.test()
                .when(v -> id == 1, "address 1")
                .when(v -> id == 2, "address 2")
                .when(v -> id == 3, "address 3"))
            .end()
            .where.id().eq(id).end();

        mapper.updateBy(update);
        // 验证SQL语句
        db.sqlList().wantFirstSql().eq("" +
                "UPDATE fluent_mybatis.student " +
                "SET `gmt_modified` = now(), `address` = ? " +
                "WHERE `id` = ?",
            StringMode.SameAsSpace);
        // 验证参数
        db.sqlList().wantFirstPara().eqReflect(new Object[]{"address 2", 2L});
    }

    @Test
    public void test_Ifs2() throws Exception {
        long id = 2L;
        StudentUpdate update = new StudentUpdate()
            .set.address().is(If.test()
                .when(v -> id == 1, "address 1")
                .when(v -> id == 2, "address 2")
                .other("address3")
            )
            .end()
            .where.id().eq(If.test()
                .when(v -> id == 1, 1)
                .when(v -> id == 2, 2))
            .end();

        mapper.updateBy(update);

        // 验证SQL语句
        db.sqlList().wantFirstSql().eq("" +
                "UPDATE fluent_mybatis.student " +
                "SET `gmt_modified` = now(), `address` = ? " +
                "WHERE `id` = ?",
            StringMode.SameAsSpace);
        // 验证参数
        db.sqlList().wantFirstPara().eqReflect(new Object[]{"address 2", 2L});
    }

    @Test
    public void test_InIfs() throws Exception {
        int[] ids = {2, 3};
        StudentUpdate update = new StudentUpdate()
            .set.address().is("address")
            .end()
            .where.id().in(If.testIn()
                .when(list -> list.contains(1), ids)
                .when(list -> list.contains(2), ids)
                .other(ids))
            .end();

        mapper.updateBy(update);

        // 验证SQL语句
        db.sqlList().wantFirstSql().eq("" +
                "UPDATE fluent_mybatis.student SET `gmt_modified` = now(), `address` = ? " +
                "WHERE `id` IN (?, ?)",
            StringMode.SameAsSpace);
        // 验证参数
        db.sqlList().wantFirstPara().eqReflect(new Object[]{"address", 2, 3});
    }


    @Test
    public void test_InIfs2() throws Exception {
        List ids = Arrays.asList(2, 3);
        StudentUpdate update = new StudentUpdate()
            .set.address().is("address")
            .end()
            .where.id().in(If.testIn()
                .when(list -> list.contains(1), ids)
                .when(list -> list.contains(2), ids)
                .other(ids))
            .end();

        mapper.updateBy(update);

        // 验证SQL语句
        db.sqlList().wantFirstSql().eq("" +
                "UPDATE fluent_mybatis.student " +
                "SET `gmt_modified` = now(), `address` = ? WHERE `id` IN (?, ?)",
            StringMode.SameAsSpace);
        // 验证参数
        db.sqlList().wantFirstPara().eqReflect(new Object[]{"address", 2, 3});
    }
}
