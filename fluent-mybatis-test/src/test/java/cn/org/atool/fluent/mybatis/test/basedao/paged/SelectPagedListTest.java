package cn.org.atool.fluent.mybatis.test.basedao.paged;

import cn.org.atool.fluent.mybatis.base.dao.IProtectedDao;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.functions.MapFunction;
import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.helper.StudentMapping;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * SelectPagedListTest
 *
 * @author darui.wu
 * @create 2020/6/24 11:05 上午
 */
public class SelectPagedListTest extends BaseTest {

    @Autowired
    @Qualifier("studentDaoImpl")
    private IProtectedDao daoProtected;

    @DisplayName("准备100条数据, 分页查询，一次操作返回总数和符合条件的列表")
    @Test
    public void test_select_paged_list() throws Exception {
        ATM.dataMap.student.initTable(100)
            .id.autoIncrease()
            .userName.formatAutoIncrease("user_%d")
            .age.generate((index) -> new Random().nextInt(100))
            .cleanAndInsert();

        StdPagedList<StudentEntity> list = daoProtected.stdPagedEntity(new StudentQuery()
            .where.userName().like("user").end()
            .orderBy.id().asc().end()
            .limit(20, 10)
        );
        want.number(list.getTotal()).eq(100);
        List<Integer> ids = list.getData().stream()
            .map(e -> (int) (long) e.getId()).collect(Collectors.toList());

        want.list(ids).eqReflect(new int[]{21, 22, 23, 24, 25, 26, 27, 28, 29, 30});
        db.sqlList().wantSql(0)
            .eq("SELECT COUNT(*) FROM student " +
                "WHERE `user_name` LIKE ?");
        db.sqlList().wantSql(1).end("FROM student " +
            "WHERE `user_name` LIKE ? " +
            "ORDER BY `id` ASC " +
            "LIMIT ?, ?");
    }

    @Test
    public void test_select_paged_list2() throws Exception {
        ATM.dataMap.student.initTable(100)
            .id.autoIncrease()
            .userName.formatAutoIncrease("user_%d")
            .age.generate((index) -> new Random().nextInt(100))
            .cleanAndInsert();

        StdPagedList<Map> list = daoProtected.stdPagedMap(new StudentQuery()
            .where.id().gt(20)
            .and.userName().like("user")
            .end()
            .orderBy.id().asc().end()
            .limit(10)
        );
        // 验证总记录数
        want.number(list.getTotal()).eq(80);
        // 验证list结果值
        MapFunction<Integer> convert = (m) -> ((BigInteger) m.get(StudentMapping.id.column)).intValue();
        List<Integer> ids = list.getData().stream().map(convert).collect(Collectors.toList());
        want.list(ids).eqReflect(new int[]{21, 22, 23, 24, 25, 26, 27, 28, 29, 30});
    }
}
