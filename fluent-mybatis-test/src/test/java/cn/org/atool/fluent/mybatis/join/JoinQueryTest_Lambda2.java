package cn.org.atool.fluent.mybatis.join;

import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.base.JoinBuilder2;
import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.HomeAddressQuery;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class JoinQueryTest_Lambda2 extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void test() {
        ATM.DataMap.student.initTable(3)
            .userName.formatAutoIncrease("user_%d")
            .age.values(34)
            .addressId.values(1, 3)
            .cleanAndInsert();
        ATM.DataMap.homeAddress.initTable(2)
            .id.values(3, 4)
            .address.values("address_1", "address_2")
            .cleanAndInsert();
        IQuery query = JoinBuilder2.from(StudentQuery.class, q ->
            q.selectAll()
                .where.age().eq(34).end())
            .join(HomeAddressQuery.class, q -> q
                .where.address().like("address").end())
            .on(l -> l.where.addressId(), r -> r.where.id()).endJoin()
            .build();
        List<StudentEntity> entities = this.mapper.listEntity(query);
        want.list(entities).eqDataMap(ATM.DataMap.student.entity(2)
            .id.values(2, 3)
            .addressId.values(3)
            .age.values(34)
            .userName.values("user_2", "user_3")
        );
    }
}