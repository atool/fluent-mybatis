package cn.org.atool.fluent.mybatis.test.formservice;

import cn.org.atool.fluent.mybatis.formservice.service.IdCardService;
import cn.org.atool.fluent.mybatis.formservice.service.IdCardService.VForm;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SuppressWarnings("all")
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

    @Test
    void validateArg() {
        service.validateArg(6, new VForm().setVersion(9).setCode("c"));
    }

    @Test
    void validateArg1() {
        VForm form = new VForm().setVersion(120).setCode("");
        want.exception(() -> service.validateArg(2, form), IllegalArgumentException.class)
            .contains("validateArg.arg0: 最小不能小于4");
    }

    @Test
    void validateArg2() {
        VForm form = new VForm().setVersion(120).setCode("");
        want.exception(() -> service.validateArg(5, form), IllegalArgumentException.class)
            .contains("validateArg.arg1.version: 最大不能超过100")
            .contains("validateArg.arg1.code: 不能为空");
    }

    @Test
    void testNotAllowed() {
        VForm form = new VForm().setVersion(9).setCode("NotAllowed");
        want.exception(() -> service.validateArg(6, form), IllegalArgumentException.class)
            .contains("NotAllowed");
    }

    @Test
    void validateFormList() {
        List list = list(new VForm().setVersion(1333));
        want.exception(() -> service.validateFormList(list), IllegalArgumentException.class)
            .contains("validateFormList.arg0.code: 不能为空")
            .contains("validateFormList.arg0.version: 最大不能超过100");
    }
}
