package cn.org.atool.fluent.mybatis.generate.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import cn.org.atool.fluent.mybatis.customize.model.MyEnum;
import cn.org.atool.fluent.mybatis.functions.TableSupplier;
import java.io.Serializable;
import java.util.function.Consumer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.apache.ibatis.type.EnumTypeHandler;

/**
 * MyEnumTypePoJo: 数据映射实体定义
 *
 * @author Powered By Fluent Mybatis
 */
@SuppressWarnings({"unchecked"})
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
  @TableId("id")
  private Long id;

  /**
   * 枚举类型, 序号
   */
  @TableField(
      value = "enum_num",
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
  public Serializable findPk() {
    return this.id;
  }

  @Override
  public Consumer<Long> pkSetter() {
    return this::setId;
  }

  @Override
  public final Class<? extends IEntity> entityClass() {
    return MyEnumTypePoJo.class;
  }

  @Override
  public final MyEnumTypePoJo changeTableBelongTo(TableSupplier supplier) {
    return super.changeTableBelongTo(supplier);
  }

  @Override
  public final MyEnumTypePoJo changeTableBelongTo(String table) {
    return super.changeTableBelongTo(table);
  }
}
