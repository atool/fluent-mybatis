package cn.org.atool.fluent.mybatis.db.pg.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * PgStudentEntity: 数据映射实体定义
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
    table = "student",
    schema = "test",
    dbType = DbType.POSTGRE_SQL
)
public class PgStudentEntity extends RichEntity {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId("id")
    private Integer id;

    /**
     *
     */
    @TableField("address")
    private String address;

    /**
     *
     */
    @TableField("age")
    private Integer age;

    /**
     *
     */
    @TableField("birthday")
    private Date birthday;

    /**
     *
     */
    @TableField("bonus_points")
    private Long bonusPoints;

    /**
     *
     */
    @TableField("desk_mate_id")
    private Long deskMateId;

    /**
     *
     */
    @TableField("email")
    private String email;

    /**
     *
     */
    @TableField("env")
    private String env;

    /**
     *
     */
    @TableField("gender")
    private Integer gender;

    /**
     *
     */
    @TableField("gmt_created")
    private Date gmtCreated;

    /**
     *
     */
    @TableField("gmt_modified")
    private Date gmtModified;

    /**
     *
     */
    @TableField("grade")
    private Integer grade;

    /**
     *
     */
    @TableField("home_address_id")
    private Long homeAddressId;

    /**
     *
     */
    @TableField("home_county_id")
    private Long homeCountyId;

    /**
     *
     */
    @TableField("is_deleted")
    private Integer isDeleted;

    /**
     *
     */
    @TableField("phone")
    private String phone;

    /**
     *
     */
    @TableField("status")
    private String status;

    /**
     *
     */
    @TableField("tenant")
    private Long tenant;

    /**
     *
     */
    @TableField("user_name")
    private String userName;

    /**
     *
     */
    @TableField("version")
    private String version;

    @Override
    public final Class entityClass() {
        return PgStudentEntity.class;
    }
}