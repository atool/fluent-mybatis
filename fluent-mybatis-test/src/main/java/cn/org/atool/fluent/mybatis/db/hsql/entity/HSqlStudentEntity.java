package cn.org.atool.fluent.mybatis.db.hsql.entity;


import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * HSqlStudentEntity: 数据映射实体定义
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
    table = "STUDENT",
    dbType = DbType.HSQL
)
public class HSqlStudentEntity extends RichEntity {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId("ID")
    private Long id;

    /**
     *
     */
    @TableField("ADDRESS")
    private String address;

    /**
     *
     */
    @TableField("AGE")
    private Integer age;

    /**
     *
     */
    @TableField("BIRTHDAY")
    private Date birthday;

    /**
     *
     */
    @TableField("BONUS_POINTS")
    private Long bonusPoints;

    /**
     *
     */
    @TableField("DESK_MATE_ID")
    private Long deskMateId;

    /**
     *
     */
    @TableField("EMAIL")
    private String email;

    /**
     *
     */
    @TableField("ENV")
    private String env;

    /**
     *
     */
    @TableField("GENDER")
    private Integer gender;

    /**
     *
     */
    @TableField("GMT_CREATED")
    private Date gmtCreated;

    /**
     *
     */
    @TableField("GMT_MODIFIED")
    private Date gmtModified;

    /**
     *
     */
    @TableField("GRADE")
    private Integer grade;

    /**
     *
     */
    @TableField("HOME_ADDRESS_ID")
    private Long homeAddressId;

    /**
     *
     */
    @TableField("HOME_COUNTY_ID")
    private Long homeCountyId;

    /**
     *
     */
    @TableField("IS_DELETED")
    private Integer isDeleted;

    /**
     *
     */
    @TableField("PHONE")
    private String phone;

    /**
     *
     */
    @TableField("STATUS")
    private String status;

    /**
     *
     */
    @TableField("TENANT")
    private Long tenant;

    /**
     *
     */
    @TableField("USER_NAME")
    private String userName;

    /**
     *
     */
    @TableField("VERSION")
    private String version;

    @Override
    public final Class<? extends IEntity> entityClass() {
        return HSqlStudentEntity.class;
    }
}