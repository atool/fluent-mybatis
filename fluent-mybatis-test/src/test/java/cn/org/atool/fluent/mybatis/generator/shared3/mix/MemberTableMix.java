package cn.org.atool.fluent.mybatis.generator.shared3.mix;

import cn.org.atool.fluent.mybatis.generator.shared3.dm.MemberDataMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.database.datagen.BaseMix;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库[t_member]表数据准备和校验通用方法
 *
 * @author Powered By Test4J
 */
@SuppressWarnings({"unused", "rawtypes", "UnusedReturnValue"})
public class MemberTableMix extends BaseMix<MemberTableMix, MemberDataMap> implements IMix {
  public MemberTableMix() {
    super("t_member");
  }

  @Step("清空表[t_member]数据")
  public MemberTableMix cleanMemberTable() {
    return super.cleanTable();
  }

  @Step("准备表[t_member]数据{1}")
  public MemberTableMix readyMemberTable(MemberDataMap data) {
    return super.readyTable(data);
  }

  @Step("验证表[t_member]有全表数据{1}")
  public MemberTableMix checkMemberTable(MemberDataMap data, EqMode... modes) {
    return super.checkTable(data, modes);
  }

  @Step("验证表[t_member]有符合条件{1}的数据{2}")
  public MemberTableMix checkMemberTable(String where, MemberDataMap data, EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[t_member]有符合条件{1}的数据{2}")
  public MemberTableMix checkMemberTable(MemberDataMap where, MemberDataMap data, EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[t_member]有{1}条符合条件{2}的数据")
  public MemberTableMix countMemberTable(int count, MemberDataMap where) {
    return super.countTable(count, where);
  }

  @Step("验证表[t_member]有{1}条符合条件{2}的数据")
  public MemberTableMix countMemberTable(int count, String where) {
    return super.countTable(count, where);
  }

  @Step("验证表[t_member]有{1}条数据")
  public MemberTableMix countMemberTable(int count) {
    return super.countTable(count);
  }
}
