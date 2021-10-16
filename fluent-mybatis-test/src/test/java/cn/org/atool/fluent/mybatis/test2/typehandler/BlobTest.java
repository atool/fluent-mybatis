package cn.org.atool.fluent.mybatis.test2.typehandler;

import cn.org.atool.fluent.mybatis.generator.shared4.mapper.BlobValueMapper;
import cn.org.atool.fluent.mybatis.generator.shared4.wrapper.BlobValueQuery;
import cn.org.atool.fluent.mybatis.test1.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class BlobTest extends BaseTest {
    @Autowired
    BlobValueMapper mapper;

    @Test
    void query() {
        BlobValueQuery query = new BlobValueQuery().where.id().eq(1).end();
        mapper.findOne(query);
    }
}
