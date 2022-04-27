package cn.org.atool.fluent.mybatis.test.where.defaults;

import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.generator.shared1.entity.NoAutoIdEntity;
import cn.org.atool.fluent.mybatis.generator.shared1.mapper.NoAutoIdMapper;
import cn.org.atool.fluent.mybatis.generator.shared1.wrapper.NoAutoIdUpdate;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;

public class LockVersionTest extends BaseTest {
    @Autowired
    NoAutoIdMapper mapper;

    @Test
    void updateById() {
        mapper.updateById(new NoAutoIdEntity().setLockVersion(2L).setColumn1("new").setId("1"));
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE `no_auto_id` " +
            "SET `column_1` = ?, `lock_version` = `lock_version` + 1 " +
            "WHERE `id` = ? AND `lock_version` = ?");
        db.sqlList().wantFirstPara().eq(new Object[]{"new", "1", 2L});
    }

    @Test
    void updateById_NoSetVersion() {
        want.exception(() -> mapper
                    .updateById(new NoAutoIdEntity().setColumn1("new").setId("1")),
                MyBatisSystemException.class, FluentMybatisException.class)
            .contains("the parameter[lock version field(lockVersion)] can't be null");
    }

    @Test
    void updateByUpdater() {
        mapper.updateBy(new NoAutoIdUpdate()
            .set.column1().is("new").end()
            .where.id().eq("1")
            .and.lockVersion().eq(2L).end());
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE `no_auto_id` " +
            "SET `lock_version` = `lock_version` + 1, " +
            "`column_1` = ? " +
            "WHERE `id` = ? AND `lock_version` = ?");
        db.sqlList().wantFirstPara().eqList("new", "1", 2L);
    }

    @Test
    void updateByUpdater_NoVersionWhere() {
        want.exception(() -> mapper
                .updateBy(new NoAutoIdUpdate()
                    .set.column1().is("new").end()
                    .where.id().eq("1").end()), MyBatisSystemException.class)
            .contains("@Version field of where condition not set.");
    }

    @Test
    void updateByUpdater_IgnoreLock() {
        mapper.updateBy(new NoAutoIdUpdate()
            .ignoreLockVersion()
            .set.column1().is("new").end()
            .where.id().eq("1").end());
        db.sqlList().wantFirstSql()
            .eq("UPDATE `no_auto_id` SET `column_1` = ? WHERE `id` = ?");
        db.sqlList().wantFirstPara().eqList("new", "1");
    }
}
