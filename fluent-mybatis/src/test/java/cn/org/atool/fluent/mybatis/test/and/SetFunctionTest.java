package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.demo.generate.datamap.TM;
import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.query.UserUpdate;
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
        db.table(t_user).clean()
            .insert(TM.user.createWithInit(1)
                .id.values(1)
                .user_name.values("test")
                .age.values(23)
            );
        UserUpdate update = new UserUpdate()
            .set.userName.apply("concat(user_name, ?)", "_aaa")
            .set.age.apply("age+1")
            .and.id.eq(1L);
        mapper.updateBy(update);
        db.table(t_user).query().eqDataMap(TM.user.create(1)
            .user_name.values("test_aaa")
            .age.values(24)
        );
        db.sqlList().wantFirstSql()
            .eq("UPDATE t_user SET gmt_modified=now(), user_name = concat(user_name, ?),age = age+1 WHERE id = ?");
    }
}