package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.generate.helper.StudentMapping;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void order() {
        StudentQuery query = new StudentQuery()
            .where.userName().like("user").end()
            .orderBy.id().asc()
            .homeAddressId().desc()
            .desc("user_name", "id+0").end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql().where().eq("`user_name` LIKE ?");
        db.sqlList().wantFirstSql().end("ORDER BY id ASC, home_address_id DESC, user_name DESC, id+0 DESC");
    }

    @Test
    public void order2() {
        StudentQuery query = new StudentQuery()
            .where.userName().like("user").end()
            .orderBy
            .id().asc()
            .asc("home_address_id")
            .userName().desc()
            .asc("id+0")
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql().where().eq("`user_name` LIKE ?");
        db.sqlList().wantFirstSql().end("ORDER BY id ASC, home_address_id ASC, user_name DESC, id+0 ASC");
    }

    @Test
    public void orderBy_condition() {
        StudentQuery query = new StudentQuery()
            .where.userName().like("user").end()
            .orderBy
            .apply(true, false, StudentMapping.id, StudentMapping.homeAddressId)
            .apply(false, true, StudentMapping.userName)
            .asc("id+0")
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql().end("ORDER BY id DESC, home_address_id DESC, id+0 ASC");
    }
}
