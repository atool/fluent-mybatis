package cn.org.atool.fluent.mybatis.join;

import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.base.JoinBuilder;
import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.AddressQuery;
import cn.org.atool.fluent.mybatis.generate.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class JoinQuery2Test extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void test() {
        ATM.DataMap.user.initTable(3)
            .userName.formatAutoIncrease("user_%d")
            .age.values(34)
            .addressId.values(1, 3)
            .cleanAndInsert();
        ATM.DataMap.address.initTable(2)
            .id.values(3, 4)
            .address.values("address_1", "address_2")
            .cleanAndInsert();
        IQuery query = JoinBuilder.from(UserQuery.class, q ->
            q.selectAll()
                .where.age().eq(34).end())
            .join(AddressQuery.class, q -> q
                .where.address().like("address").end())
            .on(l -> l.where.addressId(), r -> r.where.id()).endJoin()
            .build();
        List<UserEntity> entities = this.mapper.listEntity(query);
        want.list(entities).eqDataMap(ATM.DataMap.user.entity(2)
            .id.values(2, 3)
            .addressId.values(3)
            .age.values(34)
            .userName.values("user_2", "user_3")
        );
    }
}