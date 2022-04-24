package cn.org.atool.fluent.mybatis.generator.shared3.mix;

import cn.org.atool.fluent.mybatis.generator.shared3.dm.MemberFavoriteDataMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.database.datagen.BaseMix;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库[t_member_favorite]表数据准备和校验通用方法
 *
 * @author Powered By Test4J
 */
@SuppressWarnings({"unused", "rawtypes", "UnusedReturnValue"})
public class MemberFavoriteTableMix extends BaseMix<MemberFavoriteTableMix, MemberFavoriteDataMap> implements IMix {
  public MemberFavoriteTableMix() {
    super("t_member_favorite");
  }

  @Step("清空表[t_member_favorite]数据")
  public MemberFavoriteTableMix cleanMemberFavoriteTable() {
    return super.cleanTable();
  }

  @Step("准备表[t_member_favorite]数据{1}")
  public MemberFavoriteTableMix readyMemberFavoriteTable(MemberFavoriteDataMap data) {
    return super.readyTable(data);
  }

  @Step("验证表[t_member_favorite]有全表数据{1}")
  public MemberFavoriteTableMix checkMemberFavoriteTable(MemberFavoriteDataMap data,
      EqMode... modes) {
    return super.checkTable(data, modes);
  }

  @Step("验证表[t_member_favorite]有符合条件{1}的数据{2}")
  public MemberFavoriteTableMix checkMemberFavoriteTable(String where, MemberFavoriteDataMap data,
      EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[t_member_favorite]有符合条件{1}的数据{2}")
  public MemberFavoriteTableMix checkMemberFavoriteTable(MemberFavoriteDataMap where,
      MemberFavoriteDataMap data, EqMode... modes) {
    return super.checkTable(where, data, modes);
  }

  @Step("验证表[t_member_favorite]有{1}条符合条件{2}的数据")
  public MemberFavoriteTableMix countMemberFavoriteTable(int count, MemberFavoriteDataMap where) {
    return super.countTable(count, where);
  }

  @Step("验证表[t_member_favorite]有{1}条符合条件{2}的数据")
  public MemberFavoriteTableMix countMemberFavoriteTable(int count, String where) {
    return super.countTable(count, where);
  }

  @Step("验证表[t_member_favorite]有{1}条数据")
  public MemberFavoriteTableMix countMemberFavoriteTable(int count) {
    return super.countTable(count);
  }
}
