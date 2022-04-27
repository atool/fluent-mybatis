package cn.org.atool.fluent.mybatis.test.join;

import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.JoinBuilder;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.HomeAddressQuery;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.metadata.JoinType;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class NestedJoinTest extends BaseTest {
    @Autowired
    StudentMapper mapper;

    @DisplayName("子查询是join查询场景")
    @Test
    void issue_I3UPZ0() {
        IQuery nested = JoinBuilder.from(StudentQuery.emptyQuery("a").select.id().end())
            .join(HomeAddressQuery.emptyQuery("b").where.address().like("add").end())
            .on(l -> l.where.homeAddressId(), r -> r.where.id()).endJoin()
            .build();

        StudentQuery query = StudentQuery.emptyQuery()
            .select.userName().end()
            .where.id().in(nested).end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT `user_name` " +
            "FROM fluent_mybatis.student " +
            "WHERE `id` IN (SELECT a.`id` " +
            "FROM fluent_mybatis.student a " +
            "JOIN `home_address` b " +
            "ON a.`home_address_id` = b.`id` " +
            "WHERE b.`address` LIKE ?)");
        db.sqlList().wantFirstPara().eq(new Object[]{"%add%"});
    }

    @DisplayName("对join查询进行count操作")
    @Test
    void issue_I3UPYD() {
        IQuery query = StudentQuery.emptyQuery("t1").selectAll()
            .join(JoinType.LeftJoin, HomeAddressQuery.emptyQuery("t2"))
            .on(l -> l.where.homeAddressId(), r -> r.where.id()).endJoin()
            .limit(50, 10)
            .build();
        mapper.stdPagedEntity(query);
        db.sqlList().wantFirstSql().eq("" +
            "SELECT COUNT(*) " +
            "FROM fluent_mybatis.student t1 " +
            "LEFT JOIN `home_address` t2 " +
            "ON t1.`home_address_id` = t2.`id`");
        db.sqlList().wantSql(1).end("" +
            "FROM fluent_mybatis.student t1 LEFT " +
            "JOIN `home_address` t2 " +
            "ON t1.`home_address_id` = t2.`id` LIMIT ?, ?");
        db.sqlList().wantPara(1).eqList(50, 10);
    }
}
