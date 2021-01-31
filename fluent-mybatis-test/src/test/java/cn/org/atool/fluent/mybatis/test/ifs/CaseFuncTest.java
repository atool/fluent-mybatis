package cn.org.atool.fluent.mybatis.test.ifs;

import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentUpdate;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

public class CaseFuncTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test_applyFunc() throws Exception {
        StudentUpdate update = new StudentUpdate()
            .update.address().applyFunc("case id " +
                "when 1 then 'address 1' " +
                "when 2 then 'address 2' " +
                "else 'address 3' end")
            .end()
            .where.id().eq(2).end();
        mapper.updateBy(update);
        // 验证SQL语句
        db.sqlList().wantFirstSql()
            .eq("UPDATE student " +
                    "SET gmt_modified = now(), " +
                    "address = case id when 1 then 'address 1' when 2 then 'address 2' else 'address 3' end " +
                    "WHERE id = ?",
                StringMode.SameAsSpace);
    }

    @Test
    public void test_applyFunc2() throws Exception {
        final String CaseWhen = "case id " +
            "when 1 then ? " +
            "when 2 then ? " +
            "else ? end";
        StudentUpdate update = new StudentUpdate()
            .update.address().applyFunc(CaseWhen, "address 1", "address 2", "address 3")
            .set.age().applyFunc(CaseWhen, 23, 24, 25)
            .end()
            .where.id().in(new int[]{1, 2, 3}).end();
        mapper.updateBy(update);
        // 验证SQL语句
        db.sqlList().wantFirstSql()
            .eq("UPDATE student " +
                    "SET gmt_modified = now(), " +
                    "address = case id when 1 then ? when 2 then ? else ? end, " +
                    "age = case id when 1 then ? when 2 then ? else ? end " +
                    "WHERE id IN (?, ?, ?)",
                StringMode.SameAsSpace);
        // 验证参数
        db.sqlList().wantFirstPara()
            .eqReflect(new Object[]{"address 1", "address 2", "address 3", 23, 24, 25, 1, 2, 3});
    }
}
