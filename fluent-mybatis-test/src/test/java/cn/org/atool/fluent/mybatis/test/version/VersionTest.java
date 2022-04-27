package cn.org.atool.fluent.mybatis.test.version;

import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.generator.shared5.dao.intf.IdcardDao;
import cn.org.atool.fluent.mybatis.generator.shared5.entity.IdcardEntity;
import cn.org.atool.fluent.mybatis.generator.shared5.mapper.IdcardMapper;
import cn.org.atool.fluent.mybatis.generator.shared5.wrapper.IdcardUpdate;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("unchecked")
public class VersionTest extends BaseTest {
    @Autowired
    private IdcardMapper mapper;

    @Autowired
    private IdcardDao dao;

    @Test
    public void test_updateById_withVersion2() {
        dao.updateEntityByIds(new IdcardEntity()
            .setCode("xxx")
            .setId(1L).setVersion(1L));
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE `idcard` SET `version` = `version` + 1, `code` = ? " +
            "WHERE `version` = ? AND `id` = ?");
    }

    @Test
    public void test_updateById_noVersion2() {
        want.exception(() -> dao.updateEntityByIds(new IdcardEntity()
                    .setCode("xxx")
                    .setId(1L))
                , FluentMybatisException.class)
            .contains("In updateById method, the lock version value cannot be null");
    }

    @Test
    public void test_updateById_withVersion() {
        mapper.updateById(new IdcardEntity()
            .setId(1L).setVersion(1L));
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE `idcard` SET `version` = `version` + 1 " +
            "WHERE `id` = ? AND `version` = ?");
    }

    @Test
    public void test_updateById_noVersion() {
        want.exception(() ->
                    mapper.updateById(new IdcardEntity().setCode("new").setId(1L))
                , MyBatisSystemException.class, FluentMybatisException.class)
            .contains("the parameter[lock version field(version)] can't be null");
    }

    @Test
    public void test_updateBy_withVersion() {
        mapper.updateBy(new IdcardUpdate()
            .set.code().is("new").end()
            .where.id().eq(1L)
            .and.version().eq(2L).end());
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE `idcard` SET `version` = `version` + 1, `code` = ? " +
            "WHERE `id` = ? AND `version` = ?");
    }

    @Test
    public void test_updateBy_noVersion() {
        want.exception(() -> mapper.updateBy(new IdcardUpdate()
                    .set.code().is("new").end()
                    .where.id().eq(1L).end())
                , MyBatisSystemException.class)
            .contains("@Version field of where condition not set.");
    }
}
