package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

import java.util.Arrays;

public class DeleteByIdsTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void testDeleteByIds() {
        ATM.dataMap.student.initTable(5)
            .id.values(23L, 24L, 25L, 26L, 27L)
            .userName.values("user1", "user2")
            .cleanAndInsert();
        mapper.deleteByIds(Arrays.asList(24, 27, 25));
        db.sqlList().wantFirstSql()
            .eq("DELETE FROM student WHERE `id` IN (?, ?, ?)", StringMode.SameAsSpace);
        ATM.dataMap.student.table(2)
            .id.values(23L, 26L)
            .userName.values("user1", "user2")
            .eqTable();
    }
}
