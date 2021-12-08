package cn.org.atool.fluent.mybatis.formapi;

import cn.org.atool.fluent.mybatis.formservice.service.IdCardService;
import cn.org.atool.fluent.mybatis.test1.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class VersionUpdateTest extends BaseTest {
    @Autowired
    private IdCardService service;

    @Test
    void updateByCodeAndIdAndVersion() {
        service.updateByIdAndVersion("test1", 2L, 4L);
        db.sqlList().wantFirstSql().end("" +
            "UPDATE `idcard` SET `version` = `version` + 1, `code` = ? " +
            "WHERE (`id` = ? AND `version` = ?)");
    }
}