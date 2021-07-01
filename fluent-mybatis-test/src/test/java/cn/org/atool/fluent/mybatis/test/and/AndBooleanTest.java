package cn.org.atool.fluent.mybatis.test.and;

import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AndBooleanTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    public void isTrue() {
        StudentQuery query = new StudentQuery()
            .where.isDeleted().eq(true).end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql().where().eq("`is_deleted` = ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{true});
    }

    @Test
    public void isFalse() {
        StudentQuery query = new StudentQuery()
            .where.isDeleted().eq(false).end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql().where().eq("`is_deleted` = ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{false});
    }
}
