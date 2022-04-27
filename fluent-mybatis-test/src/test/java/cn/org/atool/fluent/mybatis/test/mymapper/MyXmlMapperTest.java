package cn.org.atool.fluent.mybatis.test.mymapper;

import cn.org.atool.fluent.mybatis.customize.mapper.MyXmlMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MyXmlMapperTest extends BaseTest {
    @Autowired
    MyXmlMapper mapper;

    @Test
    void test_my_xml_mapper() {
        this.mapper.deleteByUserId(2);
        db.sqlList().wantFirstSql().eq("DELETE FROM student WHERE id=?");
    }
}
