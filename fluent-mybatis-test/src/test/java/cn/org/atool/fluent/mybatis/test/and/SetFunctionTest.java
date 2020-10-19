package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.UserUpdate;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * SetFunctionTest
 *
 * @author darui.wu
 * @create 2020/6/4 11:31 上午
 */
public class SetFunctionTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    void setFunction() {
        db.table(ATM.Table.user).clean()
            .insert(ATM.DataMap.user.initTable(1)
                .id.values(1)
                .userName.values("test")
                .age.values(23)
            );
        UserUpdate update = new UserUpdate()
            .update.userName().apply("concat(user_name, ?)", "_aaa")
            .set.age().apply("age+1").end()
            .where.id().eq(1L).end();
        mapper.updateBy(update);
        db.table(ATM.Table.user).query().eqDataMap(ATM.DataMap.user.table(1)
            .userName.values("test_aaa")
            .age.values(24)
        );
        db.sqlList().wantFirstSql()
            .eq("UPDATE t_user SET gmt_modified = now(), user_name = concat(user_name, ?), age = age+1 WHERE id = ?");
    }
}
