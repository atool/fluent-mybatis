package cn.org.atool.fluent.mybatis.join;

import cn.org.atool.fluent.mybatis.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.AddressQuery;
import cn.org.atool.fluent.mybatis.generate.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.segment.JoinQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class JoinQueryTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void test_join() {
        JoinQuery<UserQuery, AddressQuery> query = new JoinQuery<>(
            new UserQuery()
                .where.isDeleted().eq(true).end(),
            new AddressQuery()
                .where.isDeleted().eq(true).end(),
            (q1, q2, join) -> {
                join.on(q1.where.id(), q2.where.id());
            });

        mapper.listMaps(query);
        db.sqlList().wantFirstSql().where().eq("is_deleted = ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{true});
    }
}
