package cn.org.atool.fluent.mybatis.generate.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.LogicDelete;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import cn.org.atool.fluent.mybatis.customize.model.MyEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@FluentMybatis(
    table = "my_enum_type",
    schema = "fluent_mybatis",
    suffix = "PoJo"
)
public class MyEnumTypePoJo extends RichEntity {
  private static final long serialVersionUID = 1L;

  /**
   * 主键id
   */
  @TableId(
      value = "id",
      typeHandler = LongTypeHandler.class
  )
  private Long id;

  /**
   * 是否逻辑删除
   */
  @TableField(
      value = "is_deleted",
      insert = "0"
  )
  @LogicDelete
  private Boolean isDeleted;

  /**
   * 枚举类型, 序号
   */
  @TableField(
      value = "enum-num",
      typeHandler = EnumOrdinalTypeHandler.class
  )
  private MyEnum enumNum;

  /**
   * 枚举类型, 字符
   */
  @TableField(
      value = "enum_string",
      typeHandler = EnumTypeHandler.class
  )
  private MyEnum enumString;

  @Override
  public final Class entityClass() {
    return MyEnumTypePoJo.class;
  }
}
