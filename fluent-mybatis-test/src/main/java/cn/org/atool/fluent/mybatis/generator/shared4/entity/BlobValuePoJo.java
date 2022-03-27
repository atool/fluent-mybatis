package cn.org.atool.fluent.mybatis.generator.shared4.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import cn.org.atool.fluent.mybatis.customize.ICustomizedMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.BlobTypeHandler;

/**
 * BlobValuePoJo: 数据映射实体定义
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
    table = "blob_value",
    schema = "fluent_mybatis",
    superMapper = ICustomizedMapper.class,
    suffix = "PoJo"
)
public class BlobValuePoJo extends RichEntity {
  private static final long serialVersionUID = 1L;

  @TableId(
      value = "id",
      desc = "主键id"
  )
  private Long id;

  @TableField(
      value = "blob_value",
      typeHandler = BlobTypeHandler.class
  )
  private byte[] blobValue;

  @TableField("max")
  private Long max;

  @TableField("min")
  private Long min;

  @TableField("origin")
  private String origin;

  @Override
  public final Class entityClass() {
    return BlobValuePoJo.class;
  }
}
