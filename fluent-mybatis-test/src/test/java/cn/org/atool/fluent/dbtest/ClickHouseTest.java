package cn.org.atool.fluent.dbtest;

import cn.org.atool.fluent.mybatis.db.clickhouse.mapper.CkUserMapper;
import cn.org.atool.fluent.mybatis.db.clickhouse.wrapper.CkUserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

public class ClickHouseTest extends BaseTest {
    @Autowired
    CkUserMapper mapper;

    @DisplayName("验证clickhouse数据库分页")
    @Test
    void testPage() {
        new CkUserQuery().select("*").limit(1, 10).of(mapper).listEntity();

        db.sqlList().wantFirstSql().eq("" +
            "SELECT * FROM user LIMIT ?, ?", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqList(1, 10);
    }
}