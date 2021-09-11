package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.mapper.NoPrimaryMapper;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

public class DeleteByIdTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Autowired
    private NoPrimaryMapper noPrimaryMapper;

    @Test
    public void testDeleteById() {
        ATM.dataMap.student.initTable(2)
            .id.values(23L, 24L)
            .userName.values("user1", "user2")
            .env.values("test_env")
            .cleanAndInsert();
        mapper.deleteById(24);
        db.sqlList().wantFirstSql()
            .eq("DELETE FROM fluent_mybatis.student " +
                "WHERE `is_deleted` = ? AND `env` = ? AND `id` = ?", StringMode.SameAsSpace);
        db.table(ATM.table.student).query().eqDataMap(ATM.dataMap.student.table(1)
            .id.values(23L)
            .userName.values("user1")
        );
    }

    @Test
    public void testLogicDeleteById() {
        ATM.dataMap.student.initTable(2)
            .id.values(23L, 24L)
            .userName.values("user1", "user2")
            .env.values("test_env")
            .cleanAndInsert();
        mapper.logicDeleteById(24);
        db.sqlList().wantFirstSql().eq("" +
                "UPDATE fluent_mybatis.student " +
                "SET `gmt_modified` = now(), `is_deleted` = ? " +
                "WHERE `is_deleted` = ? AND `env` = ? AND `id` = ?"
            , StringMode.SameAsSpace);
        db.table(ATM.table.student).query().eqDataMap(ATM.dataMap.student.table(2)
            .id.values(23L, 24L)
            .userName.values("user1", "user2")
            .isDeleted.values(0, 1)
        );
    }

    @Test
    public void testDeleteByIdArr() {
        ATM.dataMap.student.initTable(2)
            .id.values(23L, 24L)
            .userName.values("user1", "user2")
            .env.values("test_env")
            .cleanAndInsert();
        mapper.deleteById(24, 25);
        db.sqlList().wantFirstSql()
            .eq("DELETE FROM fluent_mybatis.student " +
                "WHERE `is_deleted` = ? AND `env` = ? AND `id` IN (?, ?)", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqList(false, "test_env", 24, 25);
        db.table(ATM.table.student).query().eqDataMap(ATM.dataMap.student.table(1)
            .id.values(23L)
            .userName.values("user1")
        );
    }

    @Test
    public void testLogicDeleteByIdArr() {
        mapper.logicDeleteById(24, 25);
        db.sqlList().wantFirstSql().eq("" +
                "UPDATE fluent_mybatis.student " +
                "SET `gmt_modified` = now(), `is_deleted` = ? " +
                "WHERE `is_deleted` = ? AND `env` = ? AND `id` IN (?, ?)"
            , StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqList(true, false, "test_env", 24, 25);
    }

    @Test
    public void test_deleteById_noPrimary() {
        db.table(ATM.table.noPrimary).clean().insert(ATM.dataMap.noPrimary.initTable(3)
            .column1.values(1, 2, 3)
            .column2.values("c1", "c2", "c3")
        );
        want.exception(() -> noPrimaryMapper.deleteById(3L), MyBatisSystemException.class, RuntimeException.class);
    }

    @Test
    public void test_logicDeleteById_noLogicDeletedField() {
        db.table(ATM.table.noPrimary).clean().insert(ATM.dataMap.noPrimary.initTable(3)
            .column1.values(1, 2, 3)
            .column2.values("c1", "c2", "c3")
        );
        want.exception(() -> noPrimaryMapper.logicDeleteById(3L),
            MyBatisSystemException.class, RuntimeException.class)
            .contains("the primary not found.");
    }
}
