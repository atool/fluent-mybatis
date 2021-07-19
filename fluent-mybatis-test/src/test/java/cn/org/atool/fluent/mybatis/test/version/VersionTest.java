package cn.org.atool.fluent.mybatis.test.version;

import cn.org.atool.fluent.mybatis.generate.entity.IdcardEntity;
import cn.org.atool.fluent.mybatis.generate.mapper.IdcardMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.IdcardUpdate;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("unchecked")
public class VersionTest extends BaseTest {
    @Autowired
    private IdcardMapper cardMapper;

    @Test
    public void test_updateById_withVersion() {
        cardMapper.updateById(new IdcardEntity()
            .setId(1L).setVersion(1L));
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE idcard SET `version` = `version` + 1 " +
            "WHERE `id` = ? AND `version` = ?");
    }

    @Test
    public void test_updateById_noVersion() {
        want.exception(() ->
                cardMapper.updateById(new IdcardEntity().setCode("new").setId(1L))
            , MyBatisSystemException.class)
            .contains("the parameter[lock version field(version)] can't be null");
    }

    @Test
    public void test_updateBy_withVersion() {
        cardMapper.updateBy(new IdcardUpdate()
            .set.code().is("new").end()
            .where.id().eq(1L)
            .and.version().eq(2L).end());
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE idcard SET `version` = `version` + 1, `code` = ? " +
            "WHERE `id` = ? AND `version` = ?");
    }

    @Test
    public void test_updateBy_noVersion() {
        want.exception(() -> cardMapper.updateBy(new IdcardUpdate()
                .set.code().is("new").end()
                .where.id().eq(1L).end())
            , MyBatisSystemException.class)
            .contains("no version condition was found");
    }
}