package cn.org.atool.fluent.mybatis.test.basedao.paged;

import cn.org.atool.fluent.mybatis.base.IDaoProtected;
import cn.org.atool.fluent.mybatis.base.model.PagedList;
import cn.org.atool.fluent.mybatis.demo.generate.datamap.TM;
import cn.org.atool.fluent.mybatis.demo.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.demo.generate.helper.UserMapping;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * SelectPagedListTest
 *
 * @author darui.wu
 * @create 2020/6/24 11:05 上午
 */
public class SelectPagedListTest extends BaseTest {

    @Autowired
    @Qualifier("userDaoImpl")
    private IDaoProtected daoProtected;

    @DisplayName("准备100条数据, 分页查询，一次操作返回总数和符合条件的列表")
    @Test
    public void test_select_paged_list() throws Exception {
        db.table(t_user).clean().insert(TM.user.createWithInit(100)
            .id.autoIncrease()
            .user_name.formatAutoIncrease("user_%d")
            .age.generate((index) -> new Random().nextInt(100))
        );

        PagedList<UserEntity> list = daoProtected.pagedEntity(new UserQuery()
            .where.userName().like("user").end()
            .orderBy.id().asc().end()
            .limit.by(20, 10)
        );
        want.number(list.getTotal()).eq(100);
        List<Integer> ids = list.getData().stream()
            .map(e -> (int) (long) e.getId()).collect(Collectors.toList());

        want.list(ids).eqReflect(new int[]{21, 22, 23, 24, 25, 26, 27, 28, 29, 30});
        db.sqlList().wantSql(0)
            .eq("SELECT COUNT( * ) FROM t_user " +
                "WHERE user_name LIKE ?");
        db.sqlList().wantSql(1).end("FROM t_user " +
            "WHERE user_name LIKE ? " +
            "ORDER BY id ASC " +
            "LIMIT ?, ?");
    }

    @Test
    public void test_select_paged_list2() throws Exception {
        db.table(t_user).clean().insert(TM.user.createWithInit(100)
            .id.autoIncrease()
            .user_name.formatAutoIncrease("user_%d")
            .age.generate((index) -> new Random().nextInt(100))
        );

        Function<Map, Integer> convert = (m) -> ((BigInteger) m.get(UserMapping.id.column)).intValue();
        PagedList<Map> list = daoProtected.pagedMaps(new UserQuery()
            .where.id().gt(20)
            .and.userName().like("user")
            .end()
            .orderBy.id().asc().end()
            .limit.by(10)
        );
        want.number(list.getTotal()).eq(80);
        List<Integer> ids = list.getData().stream().map(convert).collect(Collectors.toList());
        want.list(ids).eqReflect(new int[]{21, 22, 23, 24, 25, 26, 27, 28, 29, 30});
    }
}