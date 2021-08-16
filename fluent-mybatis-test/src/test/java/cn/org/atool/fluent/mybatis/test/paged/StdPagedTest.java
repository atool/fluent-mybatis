package cn.org.atool.fluent.mybatis.test.paged;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class StdPagedTest extends BaseTest {
    @Autowired
    StudentMapper mapper;

    @Test
    @Disabled("不支持group by分页")
    void stdPaged() {
        ATM.dataMap.student.initTable(6)
            .grade.values(2, 3)
            .cleanAndInsert();
        StudentQuery query = new StudentQuery()
            .where.id().in(new int[]{1, 3, 4})
            .end()
            .groupBy.grade().end()
            .limit(3, 10);
        query.of(mapper).stdPagedEntity();
    }
}
