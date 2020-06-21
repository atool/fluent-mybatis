package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.query.UserUpdate;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * AndBooleanTest_IsTrue
 *
 * @author darui.wu
 * @create 2020/6/19 12:42 下午
 */
public class SetObjectTest_IsTrue extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void isTrue() {
        UserUpdate update = new UserUpdate()
            .set.userName.is("u2")
            .set.isDeleted.isTrue()
            .set.addressId.isNull()
            .and.isDeleted.isFalse();
        mapper.updateBy(update);
        db.sqlList().wantFirstSql()
            .eq("UPDATE t_user SET address_id=?, gmt_modified=now(), is_deleted=?, user_name=? WHERE is_deleted = ?");
    }
}