package cn.org.atool.fluent.mybatis.test.basedao.paged;

import cn.org.atool.fluent.mybatis.base.IDaoProtected;
import cn.org.atool.fluent.mybatis.base.model.MarkerList;
import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.generate.helper.UserMapping;
import cn.org.atool.fluent.mybatis.generate.wrapper.UserQuery;
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
public class SelectMakerListTest extends BaseTest {

    @Autowired
    @Qualifier("userDaoImpl")
    private IDaoProtected dao;

    @DisplayName("准备100条数据，按条件>分页开始标识方式查询，自动获取下一页的标识")
    @Test
    public void test_select_paged_list() throws Exception {
        db.table(ATM.Table.user).clean().insert(ATM.DataMap.user.initTable(100)
            .id.autoIncrease()
            .userName.formatAutoIncrease("user_%d")
            .age.generate((index) -> new Random().nextInt(100))
        );

        MarkerList<UserEntity> list = dao.markerPagedEntity(new UserQuery()
            .where.
                id().gt(20).
                userName().like("user").end()
            .orderBy.id().asc().end()
            .limit(10)
        );

        List<Integer> ids = list.getData().stream()
            .map(e -> (int) (long) e.getId()).collect(Collectors.toList());
        want.list(ids).eqReflect(new int[]{21, 22, 23, 24, 25, 26, 27, 28, 29, 30});
        long next = list.parseMarker((UserEntity e) -> e.getId());
        want.number(next).eq(31L);
    }

    @Test
    public void test_select_paged_list2() throws Exception {
        db.table(ATM.Table.user).clean().insert(ATM.DataMap.user.initTable(100)
            .id.autoIncrease()
            .userName.formatAutoIncrease("user_%d")
            .age.generate((index) -> new Random().nextInt(100))
        );
        Function<Map, Integer> convert = (m) -> ((BigInteger) m.get(UserMapping.id.column)).intValue();
        MarkerList<Map> list = dao.markerPagedMaps(new UserQuery()
            .selectId()
            .where.id().gt(20)
            .and.userName().like("user").end()
            .orderBy.id().asc().end()
            .limit(10)
        );
        List<Integer> ids = list.getData().stream().map(convert).collect(Collectors.toList());
        want.list(ids).eqReflect(new int[]{21, 22, 23, 24, 25, 26, 27, 28, 29, 30});
        int next = list.parseMarker(convert);
        want.number(next).eq(31);
    }
}
