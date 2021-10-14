package cn.org.atool.fluent.mybatis.generate.mix;

import cn.org.atool.fluent.mybatis.generate.dm.MemberFavoriteDataMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库[t_member_favorite]表数据准备和校验通用方法
 *
 * @author Powered By Test4J
 */
public class MemberFavoriteTableMix implements IMix {
    @Step("清空表[t_member_favorite]数据")
    public MemberFavoriteTableMix cleanMemberFavoriteTable() {
        db.table("t_member_favorite").clean();
        return this;
    }

    @Step("准备表[t_member_favorite]数据{1}")
    public MemberFavoriteTableMix readyMemberFavoriteTable(MemberFavoriteDataMap data) {
        db.table("t_member_favorite").insert(data);
        return this;
    }

    @Step("验证表[t_member_favorite]有全表数据{1}")
    public MemberFavoriteTableMix checkMemberFavoriteTable(MemberFavoriteDataMap data,
                                                           EqMode... modes) {
        db.table("t_member_favorite").query().eqDataMap(data, modes);
        return this;
    }

    @Step("验证表[t_member_favorite]有符合条件{1}的数据{2}")
    public MemberFavoriteTableMix checkMemberFavoriteTable(String where, MemberFavoriteDataMap data,
                                                           EqMode... modes) {
        db.table("t_member_favorite").queryWhere(where).eqDataMap(data, modes);
        return this;
    }

    @Step("验证表[t_member_favorite]有符合条件{1}的数据{2}")
    public MemberFavoriteTableMix checkMemberFavoriteTable(MemberFavoriteDataMap where,
                                                           MemberFavoriteDataMap data, EqMode... modes) {
        db.table("t_member_favorite").queryWhere(where).eqDataMap(data, modes);
        return this;
    }

    @Step("验证表[t_member_favorite]有{1}条符合条件{2}的数据")
    public MemberFavoriteTableMix countMemberFavoriteTable(int count, MemberFavoriteDataMap where) {
        db.table("t_member_favorite").queryWhere(where).sizeEq(count);
        return this;
    }

    @Step("验证表[t_member_favorite]有{1}条符合条件{2}的数据")
    public MemberFavoriteTableMix countMemberFavoriteTable(int count, String where) {
        db.table("t_member_favorite").queryWhere(where).sizeEq(count);
        return this;
    }

    @Step("验证表[t_member_favorite]有{1}条数据")
    public MemberFavoriteTableMix countMemberFavoriteTable(int count) {
        db.table("t_member_favorite").query().sizeEq(count);
        return this;
    }
}
