package cn.org.atool.fluent.mybatis.generator.shared3.mix;

import cn.org.atool.fluent.mybatis.generator.shared3.dm.MemberLoveDataMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.database.datagen.BaseMix;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库[t_member_love]表数据准备和校验通用方法
 *
 * @author Powered By Test4J
 */
@SuppressWarnings({"unused", "rawtypes", "UnusedReturnValue"})
public class MemberLoveTableMix extends BaseMix<MemberLoveTableMix, MemberLoveDataMap> implements IMix {
  public MemberLoveTableMix() {
    super("t_member_love");
  }

  @Step("清空表[t_member_love]数据")
  public MemberLoveTableMix cleanMemberLoveTable() {
    return super.cleanTable();
  }

  @Step("准备表[t_member_love]数据{1}")
  public MemberLoveTableMix readyMemberLoveTable(MemberLoveDataMap data) {
    return super.readyTable(data);
  }

  @Step("验证表[t_member_love]有全表数据{1}")
  public MemberLoveTableMix checkMemberLoveTable(MemberLoveDataMap data, EqMode... modes) {
    return super.checkTable(data, modes);
  }

  @Step("验证表[t_member_love]有符合条件{1}的数据{2}")
  public MemberLoveTableMix checkMemberLoveTable(String where, MemberLoveDataMap data,
      EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[t_member_love]有符合条件{1}的数据{2}")
  public MemberLoveTableMix checkMemberLoveTable(MemberLoveDataMap where, MemberLoveDataMap data,
      EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[t_member_love]有{1}条符合条件{2}的数据")
  public MemberLoveTableMix countMemberLoveTable(int count, MemberLoveDataMap where) {
    return super.countTable(count, where);
  }

  @Step("验证表[t_member_love]有{1}条符合条件{2}的数据")
  public MemberLoveTableMix countMemberLoveTable(int count, String where) {
    return super.countTable(count, where);
  }

  @Step("验证表[t_member_love]有{1}条数据")
  public MemberLoveTableMix countMemberLoveTable(int count) {
    return super.countTable(count);
  }
}
