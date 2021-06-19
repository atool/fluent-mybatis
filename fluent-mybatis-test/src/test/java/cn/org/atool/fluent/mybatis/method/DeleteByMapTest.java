package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.mapper.IdcardMapper;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

import java.util.HashMap;

public class DeleteByMapTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Autowired
    private IdcardMapper idcardMapper;

    @Test
    public void testDeleteByIds() {
        ATM.dataMap.student.initTable(2)
            .id.values(23L, 24L)
            .userName.values("user1", "user2")
            .cleanAndInsert();
        mapper.deleteByMap(new HashMap<String, Object>() {
            {
                this.put("id", 24);
                this.put("user_name", "user2");
            }
        });
        db.sqlList().wantFirstSql()
            .eq("DELETE FROM student WHERE `user_name` = ? AND `id` = ?", StringMode.SameAsSpace);
        db.table(ATM.table.student).query().eqDataMap(ATM.dataMap.student.table(1)
            .id.values(23L).eqTable()
        );
    }

    @Test
    public void testLogicDeleteByIds() {
        mapper.logicDeleteByMap(new HashMap<String, Object>() {
            {
                this.put("id", 24);
                this.put("user_name", "user2");
            }
        });
        db.sqlList().wantFirstSql()
            .eq("UPDATE student SET `is_deleted` = true WHERE `user_name` = ? AND `id` = ?", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eq(new Object[]{"user2", 24});
    }

    @Test
    public void testLogicDeleteByIds_Long() {
        idcardMapper.logicDeleteByMap(new HashMap<String, Object>() {
            {
                this.put("id", 24);
            }
        });
        db.sqlList().wantFirstSql()
            .start("UPDATE idcard SET `is_deleted` =")
            .end("WHERE `id` = ?")
            .notContain("`is_deleted` = true");
        db.sqlList().wantFirstPara().eq(new Object[]{24});
    }
}
