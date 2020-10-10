package cn.org.atool.fluent.mybatis.test.segment2;

import cn.org.atool.fluent.mybatis.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.UserUpdate;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

class UpdateBaseTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    void eqByNotNull() {
        mapper.updateBy(new UserUpdate()
            .update.byNotNull(new HashMap<String, Object>() {
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
