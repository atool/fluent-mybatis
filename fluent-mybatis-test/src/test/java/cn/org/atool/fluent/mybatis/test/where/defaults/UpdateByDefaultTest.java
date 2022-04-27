package cn.org.atool.fluent.mybatis.test.where.defaults;

import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared5.entity.IdcardEntity;
import cn.org.atool.fluent.mybatis.generator.shared5.mapper.IdcardMapper;
import cn.org.atool.fluent.mybatis.generator.shared5.wrapper.IdcardUpdate;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * update default功能验证
 *
 * @author wudarui
 */
public class UpdateByDefaultTest extends BaseTest {
    @Autowired
    private IdcardMapper mapper;

    @BeforeEach
    void setup() {
        ATM.dataMap.idcard.table(2)
            .id.values(1, 2)
            .code.values("old")
            .version.values(2, 2)
            .cleanAndInsert();
    }

    @AfterEach
    void validate() {

        ATM.dataMap.idcard.table(2)
            .id.values(1, 2)
            .code.values("new", "old")
            .version.values(3, 2)
            .eqTable();
    }

    @Test
    void updateById() {
        mapper.updateById(new IdcardEntity().setId(1L).setCode("new").setVersion(2L));
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE `idcard` SET `code` = ?, `version` = `version` + 1 " +
            "WHERE `id` = ? AND `version` = ?");
        db.sqlList().wantFirstPara().eqList("new", 1L, 2L);
    }

    @Test
    void updateById_EntityHasVersion() {
        mapper.updateById(new IdcardEntity().setId(1L).setCode("new").setVersion(2L));
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE `idcard` SET `code` = ?, `version` = `version` + 1 " +
            "WHERE `id` = ? AND `version` = ?");
        db.sqlList().wantFirstPara().eqList("new", 1L, 2L);
    }

    @Test
    void updateByUpdater() {
        mapper.updateBy(new IdcardUpdate()
            .set.code().is("new").end()
            .where.id().eq(1L)
            .and.version().eq(2L).end());
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE `idcard` SET `version` = `version` + 1, `code` = ? " +
            "WHERE `id` = ? AND `version` = ?");
        db.sqlList().wantFirstPara().eq(new Object[]{"new", 1L, 2L});
    }
}
