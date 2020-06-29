package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.demo.generate.helper.UserMapping;
import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class OrderByBaseTest extends BaseTest {

    @Autowired
    private UserMapper mapper;

    @Test
    void orderBy() {
        mapper.listEntity(new UserQuery()
            .orderBy
            .asc(UserMapping.id)
            .desc(UserMapping.userName)
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE 1 = 1 ORDER BY id ASC, user_name DESC");
    }
}