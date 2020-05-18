package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.demo.generate.datamap.EM;
import cn.org.atool.fluent.mybatis.demo.generate.datamap.TM;
import cn.org.atool.fluent.mybatis.demo.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.query.UserEntityQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * SelectPageTest
 *
 * @author darui.wu
 * @create 2020/5/18 2:24 下午
 */
public class SelectPageTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void test_selectPage() throws Exception {
        db.table(t_user).clean()
            .insert(TM.user.createWithInit(8)
                .id.values(23, 24, 25, 26, 27, 28, 29, 30)
                .user_name.values("u2")
            );
        UserEntityQuery query = new UserEntityQuery()
            .and.userName.eq("u2");
        IPage page = new Page(2, 2)
            .addOrder(OrderItem.asc("id"));
        IPage<UserEntity> users = mapper.selectPage(page, query);
        db.sqlList().wantSql(0).start("SELECT").end("FROM t_user WHERE (user_name = ?)");
        db.sqlList().wantSql(1).start("SELECT").end("FROM t_user WHERE (user_name = ?) ORDER BY id limit ? offset ?");
        want.list(users.getRecords()).eqDataMap(EM.user.create(2)
            .id.values(25, 26)
            .userName.values("u2"));
    }
}