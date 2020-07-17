package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.UserUpdate;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class UpdateBaseTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    void eqByNotNull() {
        mapper.updateBy(new UserUpdate()
            .set.eqByNotNull(new HashMap<String, Object>() {
                {
                    this.put("age", 34);
                    this.put("user_name", "aaa");
                }
            }).end()
            .where.id().eq(2).end());
        db.sqlList().wantFirstSql()
            .end("SET gmt_modified = now(), user_name = ?, age = ? WHERE id = ?");
    }
}