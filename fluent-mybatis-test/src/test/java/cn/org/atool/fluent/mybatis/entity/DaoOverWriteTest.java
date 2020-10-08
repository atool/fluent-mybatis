package cn.org.atool.fluent.mybatis.entity;

import cn.org.atool.fluent.mybatis.generate.dao.impl.UserDaoImpl;
import cn.org.atool.fluent.mybatis.generate.dao.intf.UserDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

public class DaoOverWriteTest extends Test4J {
    @DisplayName("测试dao类未被重写")
    @Test
    void test() {
        UserDao dao = new UserDaoImpl();
        want.number(dao.noOverWrite()).eq(10);
    }
}
