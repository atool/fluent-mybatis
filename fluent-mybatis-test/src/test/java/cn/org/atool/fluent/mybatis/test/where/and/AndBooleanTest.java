package cn.org.atool.fluent.mybatis.test.where.and;

import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AndBooleanTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void isTrue() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.isDeleted().eq(true).end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql().where().eq("`is_deleted` = ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{true});
    }

    @Test
    public void isFalse() {
        StudentQuery query = StudentQuery.emptyQuery()
            .where.isDeleted().eq(false).end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql().where().eq("`is_deleted` = ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{false});
    }
}
