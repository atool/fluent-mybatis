package cn.org.atool.fluent.mybatis.test.join;

import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.JoinBuilder;
import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.HomeAddressQuery;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;

import java.util.List;

public class JoinQueryTest_Alias2 extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test() {
        ATM.dataMap.student.initTable(3)
            .userName.formatAutoIncrease("user_%d")
            .age.values(34)
            .homeAddressId.values(1, 3)
            .cleanAndInsert();
        ATM.dataMap.homeAddress.initTable(2)
            .id.values(3, 4)
            .address.values("address_1", "address_2")
            .cleanAndInsert();

        Parameters parameter = new Parameters();
        IQuery query = JoinBuilder.from(new StudentQuery(false, null, "t1", parameter)
                .selectAll()
                .where.age().eq(34)
                .end())
            .join(new HomeAddressQuery(false, null, "t2", parameter)
                .where.address().like("address")
                .end())
            .on(l -> l.where.homeAddressId(), r -> r.where.id())
            .endJoin()
            .build();
        List<StudentEntity> entities = this.mapper.listEntity(query);
        want.list(entities).eqDataMap(ATM.dataMap.student.entity(2)
            .id.values(2, 3)
            .homeAddressId.values(3)
            .age.values(34)
            .userName.values("user_2", "user_3")
        );
    }

    @Test
    public void test_table_supplier() {
        StudentQuery query1 = StudentQuery.query(() -> "student_1", "t1")
            .selectId()
            .where.age().eq(34)
            .end();
        HomeAddressQuery query2 = HomeAddressQuery.query(() -> "home_address_2", "t2")
            .where.address().like("address")
            .end();

        IQuery query = JoinBuilder.from(query1)
            .join(query2)
            .on(l -> l.where.homeAddressId(), r -> r.where.id())
            .endJoin()
            .build();
        want.exception(() -> this.mapper.listEntity(query), BadSqlGrammarException.class);
        db.sqlList().wantFirstSql().start("" +
            "SELECT t1.`id` FROM `student_1` t1 JOIN `home_address_2` t2 " +
            "ON t1.`home_address_id` = t2.`id`");
    }
}
