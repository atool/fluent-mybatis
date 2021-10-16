package cn.org.atool.fluent.mybatis.generator.shared3.mix;

import cn.org.atool.fluent.mybatis.generator.shared3.dm.MemberLoveDataMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库[t_member_love]表数据准备和校验通用方法
 *
 * @author Powered By Test4J
 */
@SuppressWarnings({"unused", "rawtypes", "UnusedReturnValue"})
public class MemberLoveTableMix implements IMix {
  @Step("清空表[t_member_love]数据")
  public MemberLoveTableMix cleanMemberLoveTable() {
    db.table("t_member_love").clean();
    return this;
  }

  @Step("准备表[t_member_love]数据{1}")
  public MemberLoveTableMix readyMemberLoveTable(MemberLoveDataMap data) {
    db.table("t_member_love").insert(data);
    return this;
  }

  @Step("验证表[t_member_love]有全表数据{1}")
  public MemberLoveTableMix checkMemberLoveTable(MemberLoveDataMap data, EqMode... modes) {
    db.table("t_member_love").query().eqDataMap(data, modes);
    return this;
  }

  @Step("验证表[t_member_love]有符合条件{1}的数据{2}")
  public MemberLoveTableMix checkMemberLoveTable(String where, MemberLoveDataMap data,
      EqMode... modes) {
    db.table("t_member_love").queryWhere(where).eqDataMap(data, modes);
    return this;
  }

  @Step("验证表[t_member_love]有符合条件{1}的数据{2}")
  public MemberLoveTableMix checkMemberLoveTable(MemberLoveDataMap where, MemberLoveDataMap data,
      EqMode... modes) {
    db.table("t_member_love").queryWhere(where).eqDataMap(data, modes);
    return this;
  }

  @Step("验证表[t_member_love]有{1}条符合条件{2}的数据")
  public MemberLoveTableMix countMemberLoveTable(int count, MemberLoveDataMap where) {
    db.table("t_member_love").queryWhere(where).sizeEq(count);
    return this;
  }

  @Step("验证表[t_member_love]有{1}条符合条件{2}的数据")
  public MemberLoveTableMix countMemberLoveTable(int count, String where) {
    db.table("t_member_love").queryWhere(where).sizeEq(count);
    return this;
  }

  @Step("验证表[t_member_love]有{1}条数据")
  public MemberLoveTableMix countMemberLoveTable(int count) {
    db.table("t_member_love").query().sizeEq(count);
    return this;
  }
}
