package cn.org.atool.fluent.mybatis.test.entity;

import cn.org.atool.fluent.mybatis.generator.shared2.dao.impl.StudentDaoImpl;
import cn.org.atool.fluent.mybatis.generator.shared2.dao.intf.StudentDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

public class DaoOverWriteTest extends Test4J {
    @DisplayName("测试dao类未被重写")
    @Test
    void test() {
        StudentDao dao = new StudentDaoImpl();
        want.number(dao.noOverWrite()).eq(10);
    }
}
