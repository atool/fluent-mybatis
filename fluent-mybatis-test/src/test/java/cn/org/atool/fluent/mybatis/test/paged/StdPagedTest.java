package cn.org.atool.fluent.mybatis.test.paged;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.tools.datagen.DataMap;

import java.util.Map;

public class StdPagedTest extends BaseTest {
    @Autowired
    StudentMapper mapper;

    @Test
    void stdPaged_withGroupBy() {
        ATM.dataMap.student.initTable(6)
            .address.generate(index -> "addr_" + index / 2)
            .age.values(20)
            .cleanAndInsert();
        StudentQuery query = StudentQuery.emptyQuery()
            .select.address().sum.age("sum").end()
            .groupBy.address().end()
            .limit(0, 10);
        StdPagedList<Map<String, Object>> paged = query.of(mapper).stdPagedMap();
        want.number(paged.getTotal()).eq(4);
        want.list(paged.getData()).eqDataMap(DataMap.create(4)
            .kv("address", "addr_0", "addr_1", "addr_2", "addr_3")
            .kv("sum", 20, 40, 40, 20)
        );
    }
}
