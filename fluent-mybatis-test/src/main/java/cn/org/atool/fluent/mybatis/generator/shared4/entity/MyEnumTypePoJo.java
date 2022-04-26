package cn.org.atool.fluent.mybatis.generator.shared4.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.LogicDelete;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import cn.org.atool.fluent.mybatis.customize.model.MyEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.LongTypeHandler;

/**
 * MyEnumTypePoJo: 数据映射实体定义
 *
 * @author Powered By Fluent Mybatis
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Data
@Accessors(
    chain = true
)
@EqualsAndHashCode(
    callSuper = false
)
@AllArgsConstructor
@NoArgsConstructor
@FluentMybatis(
    table = "my_enum_type",
    schema = "fluent_mybatis",
    suffix = "PoJo",
    useDao = false
)
public class MyEnumTypePoJo extends RichEntity {
  private static final long serialVersionUID = 1L;

  @TableId(
      value = "id",
      typeHandler = LongTypeHandler.class,
      desc = "主键id"
  )
  private Long id;

  @TableField(
      value = "enum-num",
      typeHandler = EnumOrdinalTypeHandler.class,
      desc = "枚举类型, 序号"
  )
  private MyEnum enumNum;

  @TableField(
      value = "enum_string",
      typeHandler = EnumTypeHandler.class,
      desc = "枚举类型, 字符"
  )
  private MyEnum enumString;

  @TableField(
      value = "is_deleted",
      insert = "0",
      desc = "是否逻辑删除"
  )
  @LogicDelete
  private Boolean isDeleted;

  @Override
  public final Class entityClass() {
    return MyEnumTypePoJo.class;
  }
}
