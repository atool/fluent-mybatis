package cn.org.atool.fluent.mybatis.test.defaults;

import cn.org.atool.fluent.mybatis.generate.mapper.NoAutoIdMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.NoAutoIdUpdate;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;

public class LockVersionTest extends BaseTest {
    @Autowired
    NoAutoIdMapper mapper;

    @Test
    void updateByUpdater() {
        mapper.updateBy(new NoAutoIdUpdate()
            .set.column1().is("new").end()
            .where.id().eq(1L)
            .and.lockVersion().eq(2L).end());
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE no_auto_id " +
            "SET lock_version = `lock_version` + 1, " +
            "column_1 = ? " +
            "WHERE id = ? AND lock_version = ?");
        db.sqlList().wantFirstPara().eq(new Object[]{"new", 1L, 2L});
    }

    @Test
    void updateByUpdater_NoVersionWhere() {
        want.exception(() -> mapper
            .updateBy(new NoAutoIdUpdate()
                .set.column1().is("new").end()
                .where.id().eq(1L).end()), MyBatisSystemException.class)
            .contains("The version lock field was explicitly set");
    }
}
