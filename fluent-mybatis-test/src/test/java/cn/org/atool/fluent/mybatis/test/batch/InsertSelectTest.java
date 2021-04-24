package cn.org.atool.fluent.mybatis.test.batch;

import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class InsertSelectTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    void test() {
        ATM.dataMap.student.table(3)
            .address.values("address1", "address2", "address3")
            .age.values(34, 45, 55)
            .cleanAndInsert();
        int count = mapper.insertSelect(new StudentQuery()
                .select.address().age().end()
                .where.id().in(new long[]{1, 2, 3}).end()
            , "address", "age");
        db.sqlList().wantFirstSql()
            .eq("INSERT INTO student (address,age) SELECT address, age FROM student WHERE id IN (?, ?, ?)");
        want.number(count).eq(3);
        ATM.dataMap.student.table(6)
            .address.values("address1", "address2", "address3", "address1", "address2", "address3")
            .age.values(34, 45, 55, 34, 45, 55)
            .eqTable();
    }
}
