package cn.org.atool.fluent.mybatis.typehandler;

import cn.org.atool.fluent.mybatis.generate.mapper.BlobValueMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.BlobValueQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
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
