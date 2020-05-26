package cn.org.atool.fluent.mybatis.demo.generate.mix;

import cn.org.atool.fluent.mybatis.demo.generate.datamap.table.UserTableMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库t_user表数据准备和校验通用方法
 *
 * @author generate code
 */
public class UserTableMix implements IMix {
    @Step("清空表[t_user]数据")
    public UserTableMix cleanUserTable() {
        db.table("t_user").clean();
        return this;
    }

    @Step("准备表[t_user]数据{1}")
    public UserTableMix readyUserTable(UserTableMap data) {
        db.table("t_user").insert(data);
        return this;
    }

    @Step("验证表[t_user]有全表数据{1}")
    public UserTableMix checkUserTable(UserTableMap data) {
        db.table("t_user").query().eqDataMap(data, EqMode.IGNORE_ORDER);
        return this;
    }

    @Step("验证表[t_user]有符合条件{1}的数据{2}")
    public UserTableMix checkUserTable(String where, UserTableMap data) {
        db.table("t_user").queryWhere(where).eqDataMap(data, EqMode.IGNORE_ORDER);
        return this;
    }

    @Step("验证表[t_user]有符合条件{1}的数据{2}")
    public UserTableMix checkUserTable(UserTableMap where, UserTableMap data) {
        db.table("t_user").queryWhere(where).eqDataMap(data, EqMode.IGNORE_ORDER);
        return this;
    }

    @Step("验证表[t_user]有{1}条符合条件{2}的数据")
    public UserTableMix countUserTable(int count, UserTableMap where) {
        db.table("t_user").queryWhere(where).sizeEq(count);
        return this;
    }

    @Step("验证表[t_user]有{1}条符合条件{2}的数据")
    public UserTableMix countUserTable(int count, String where) {
        db.table("t_user").queryWhere(where).sizeEq(count);
        return this;
    }

    @Step("验证表[t_user]有{1}条数据")
    public UserTableMix countUserTable(int count) {
        db.table("t_user").query().sizeEq(count);
        return this;
    }
}