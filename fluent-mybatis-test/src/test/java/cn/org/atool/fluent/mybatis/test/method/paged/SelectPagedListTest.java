package cn.org.atool.fluent.mybatis.test.method.paged;

import cn.org.atool.fluent.mybatis.base.dao.IProtectedDao;
import cn.org.atool.fluent.mybatis.functions.MapFunction;
import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.Ref;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * SelectPagedListTest
 *
 * @author darui.wu 2020/6/24 11:05 上午
 */
@SuppressWarnings("unchecked")
public class SelectPagedListTest extends BaseTest {

    @Autowired
    @Qualifier("studentDaoImpl")
    private IProtectedDao daoProtected;

    @DisplayName("准备100条数据, 分页查询，一次操作返回总数和符合条件的列表")
    @Test
    public void test_select_paged_list() {
        ATM.dataMap.student.initTable(100)
            .id.autoIncrease()
            .userName.formatAutoIncrease("user_%d")
            .age.generate((index) -> new Random().nextInt(100))
            .cleanAndInsert();

        StdPagedList<StudentEntity> list = daoProtected.stdPagedEntity(StudentQuery.emptyQuery()
            .where.userName().like("user").end()
            .orderBy.id().asc().end()
            .limit(20, 10)
        );
        want.number(list.getTotal()).eq(100);
        List<Integer> ids = list.getData().stream()
            .map(e -> (int) (long) e.getId()).collect(Collectors.toList());

        want.list(ids).eqReflect(new int[]{21, 22, 23, 24, 25, 26, 27, 28, 29, 30});
        db.sqlList().wantSql(0)
            .eq("SELECT COUNT(*) FROM fluent_mybatis.student " +
                "WHERE `user_name` LIKE ?");
        db.sqlList().wantSql(1).end("FROM fluent_mybatis.student " +
            "WHERE `user_name` LIKE ? " +
            "ORDER BY `id` ASC " +
            "LIMIT ?, ?");
    }

    @Test
    public void test_select_paged_list2() {
        ATM.dataMap.student.initTable(100)
            .id.autoIncrease()
            .userName.formatAutoIncrease("user_%d")
            .age.generate((index) -> new Random().nextInt(100))
            .cleanAndInsert();

        StdPagedList<Map> list = daoProtected.stdPagedMap(StudentQuery.emptyQuery()
            .where.id().gt(20)
            .and.userName().like("user")
            .end()
            .orderBy.id().asc().end()
            .limit(10)
        );
        // 验证总记录数
        want.number(list.getTotal()).eq(80);
        // 验证list结果值
        MapFunction<Integer> convert = (m) -> ((Number) m.get(Ref.Field.Student.id.column)).intValue();
        List<Integer> ids = list.getData().stream().map(convert).collect(Collectors.toList());
        want.list(ids).eqReflect(new int[]{21, 22, 23, 24, 25, 26, 27, 28, 29, 30});
    }
}
