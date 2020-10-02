package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.generate.datamap.TM;
import cn.org.atool.fluent.mybatis.generate.entity.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.generate.entity.wrapper.UserUpdate;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * QuestionMarkTest
 *
 * @author darui.wu
 * @create 2020/6/20 3:57 下午
 */
public class QuestionMarkTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @DisplayName("?反义处理")
    @Test
    void test() {
        db.table(t_user).clean()
            .insert(TM.user.createWithInit(1)
                .id.values(1)
                .user_name.values("test")
                .age.values(23)
            );
        UserUpdate update = new UserUpdate()
            .update.userName().apply("concat(user_name, concat('_\\\\\\?', ? ))", "_aaa")
            .set.age().apply("age+1").end()
            .where.id().eq(1L).end();
        mapper.updateBy(update);
        db.table(t_user).query().eqDataMap(TM.user.create(1)
            .user_name.values("test_\\?_aaa")
            .age.values(24)
        );
        db.sqlList().wantFirstSql()
            .eq("UPDATE t_user " +
                "SET gmt_modified = now(), " +
                "user_name = concat(user_name, concat('_\\\\?', ? )), " +
                "age = age+1 " +
                "WHERE id = ?");
    }
}