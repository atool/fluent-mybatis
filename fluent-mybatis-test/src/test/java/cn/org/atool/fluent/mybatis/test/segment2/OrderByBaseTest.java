package cn.org.atool.fluent.mybatis.test.segment2;

import cn.org.atool.fluent.mybatis.generate.helper.UserMapping;
import cn.org.atool.fluent.mybatis.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.UserQuery;
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
            .id().asc()
            .desc(UserMapping.userName).end()
        );
        db.sqlList().wantFirstSql()
            .end("FROM t_user ORDER BY id ASC, user_name DESC");
    }
}