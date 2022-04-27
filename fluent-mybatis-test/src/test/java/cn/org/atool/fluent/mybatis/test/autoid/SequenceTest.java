package cn.org.atool.fluent.mybatis.test.autoid;

import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared5.entity.IdcardEntity;
import cn.org.atool.fluent.mybatis.generator.shared5.mapper.IdcardMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

public class SequenceTest extends BaseTest {
    @Autowired
    IdcardMapper mapper;

    @Test
    void testInsert() {
        ATM.dataMap.idcard.table().clean();
        IdcardEntity entity = new IdcardEntity().setCode("code1").setVersion(1L);
        mapper.insert(entity);
        long id = entity.getId();
        mapper.insert(entity);
        want.number(entity.getId()).eq(id + 1);
    }

    @Test
    void testInsertWithPk() {
        ATM.dataMap.idcard.table(1).clean();
        IdcardEntity entity = new IdcardEntity().setId(3L).setCode("code1").setVersion(1L);
        mapper.insertWithPk(entity);
        want.number(entity.getId()).eq(3L);
        ATM.dataMap.idcard.table(1)
            .id.values(3L)
            .code.values("code1")
            .eqTable();
    }

    @Test
    void testBatchInsert() {
        ATM.dataMap.idcard.table(1).clean();
        IdcardEntity entity1 = new IdcardEntity().setCode("code1").setVersion(1L);
        IdcardEntity entity2 = new IdcardEntity().setCode("code1").setVersion(1L);
        mapper.insertBatch(list(entity1, entity2));
        db.sqlList().wantFirstSql().eq("" +
            "INSERT INTO `idcard` (`id`, `code`, `version`, `is_deleted`) " +
            "SELECT NEXTVAL('testSeq'), TMP.* FROM (" +
            " (SELECT ? , ? , 0 FROM DUAL)" +
            " UNION ALL" +
            " (SELECT ? , ? , 0 FROM DUAL) " +
            ") TMP", StringMode.SameAsSpace);
    }

    @Test
    void testBatchInsertWithPk() {
        ATM.dataMap.idcard.table(1).clean();
        IdcardEntity entity1 = new IdcardEntity().setId(3L).setCode("code1").setVersion(1L);
        IdcardEntity entity2 = new IdcardEntity().setId(6L).setCode("code1").setVersion(1L);
        mapper.insertBatchWithPk(list(entity1, entity2));
        db.sqlList().wantFirstSql().eq("" +
            "INSERT INTO `idcard` (`id`, `code`, `version`, `is_deleted`) " +
            "VALUES (?, ?, ?, 0), (?, ?, ?, 0)", StringMode.SameAsSpace);
        ATM.dataMap.idcard.table(2)
            .id.values(3L, 6L)
            .code.values("code1")
            .eqTable();
    }
}
