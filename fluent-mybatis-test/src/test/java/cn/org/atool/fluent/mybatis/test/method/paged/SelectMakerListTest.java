package cn.org.atool.fluent.mybatis.test.method.paged;

import cn.org.atool.fluent.mybatis.base.dao.IProtectedDao;
import cn.org.atool.fluent.mybatis.functions.MapFunction;
import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.Ref;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.model.TagPagedList;
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
public class SelectMakerListTest extends BaseTest {

    @Autowired
    @Qualifier("studentDaoImpl")
    private IProtectedDao dao;

    @DisplayName("准备100条数据，按条件>分页开始标识方式查询，自动获取下一页的标识")
    @Test
    public void test_select_paged_list() {
        ATM.dataMap.student.initTable(100)
            .id.autoIncrease()
            .userName.formatAutoIncrease("user_%d")
            .age.generate((index) -> new Random().nextInt(100))
            .cleanAndInsert();

        TagPagedList<StudentEntity> list = dao.tagPagedEntity(StudentQuery.emptyQuery()
            .where.
            id().gt(20).
            userName().like("user").end()
            .orderBy.id().asc().end()
            .limit(10)
        );

        List<Integer> ids = list.getData().stream()
            .map(e -> (int) (long) e.getId()).collect(Collectors.toList());
        want.list(ids).eqReflect(new int[]{21, 22, 23, 24, 25, 26, 27, 28, 29, 30});
        long next = list.parseNext(StudentEntity::getId);
        want.number(next).eq(31L);
    }

    @Test
    public void test_select_paged_list2() {
        ATM.dataMap.student.initTable(100)
            .id.autoIncrease()
            .userName.formatAutoIncrease("user_%d")
            .age.generate((index) -> new Random().nextInt(100))
            .cleanAndInsert();

        TagPagedList<Map> list = dao.tagPagedMap(StudentQuery.emptyQuery()
            .selectId()
            .where.id().gt(20)
            .and.userName().like("user").end()
            .orderBy.id().asc().end()
            .limit(10)
        );

        MapFunction<Integer> convert = (m) -> ((Number) m.get(Ref.Field.Student.id.column)).intValue();
        List<Integer> ids = list.getData().stream().map(convert).collect(Collectors.toList());
        want.list(ids).eqReflect(new int[]{21, 22, 23, 24, 25, 26, 27, 28, 29, 30});
        int next = list.parseNext(convert);
        want.number(next).eq(31);
    }
}
