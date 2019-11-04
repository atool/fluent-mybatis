package cn.org.atool.mbplus.test.and;

import cn.org.atool.mbplus.demo.mapper.UserMapper;
import cn.org.atool.mbplus.demo.query.UserEntityQuery;
import cn.org.atool.mbplus.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AndBooleanTest extends BaseTest {
    @Autowired
    private UserMapper mapper;

    @Test
    public void isTrue() {
        UserEntityQuery query = new UserEntityQuery()
                .and.isDeleted.isTrue();
        mapper.selectList(query);
        db.sqlList().wantFirstSql().where().eq("is_deleted = ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{true});
    }

    @Test
    public void isFalse() {
        UserEntityQuery query = new UserEntityQuery()
                .and.isDeleted.isFalse();
        mapper.selectList(query);
        db.sqlList().wantFirstSql().where().eq("is_deleted = ?");
        db.sqlList().wantFirstPara().eqReflect(new Object[]{false});
    }
}