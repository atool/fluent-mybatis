package cn.org.atool.fluent.mybatis.generate.mix;

import cn.org.atool.fluent.mybatis.generate.dm.MemberDataMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库[t_member]表数据准备和校验通用方法
 *
 * @author Powered By Test4J
 */
public class MemberTableMix implements IMix {
  @Step("清空表[t_member]数据")
  public MemberTableMix cleanMemberTable() {
    db.table("t_member").clean();
    return this;
  }

  @Step("准备表[t_member]数据{1}")
  public MemberTableMix readyMemberTable(MemberDataMap data) {
    db.table("t_member").insert(data);
    return this;
  }

  @Step("验证表[t_member]有全表数据{1}")
  public MemberTableMix checkMemberTable(MemberDataMap data, EqMode... modes) {
    db.table("t_member").query().eqDataMap(data, modes);
    return this;
  }

  @Step("验证表[t_member]有符合条件{1}的数据{2}")
  public MemberTableMix checkMemberTable(String where, MemberDataMap data, EqMode... modes) {
    db.table("t_member").queryWhere(where).eqDataMap(data, modes);
    return this;
  }

  @Step("验证表[t_member]有符合条件{1}的数据{2}")
  public MemberTableMix checkMemberTable(MemberDataMap where, MemberDataMap data, EqMode... modes) {
    db.table("t_member").queryWhere(where).eqDataMap(data, modes);
    return this;
  }

  @Step("验证表[t_member]有{1}条符合条件{2}的数据")
  public MemberTableMix countMemberTable(int count, MemberDataMap where) {
    db.table("t_member").queryWhere(where).sizeEq(count);
    return this;
  }

  @Step("验证表[t_member]有{1}条符合条件{2}的数据")
  public MemberTableMix countMemberTable(int count, String where) {
    db.table("t_member").queryWhere(where).sizeEq(count);
    return this;
  }

  @Step("验证表[t_member]有{1}条数据")
  public MemberTableMix countMemberTable(int count) {
    db.table("t_member").query().sizeEq(count);
    return this;
  }
}
