package cn.org.atool.fluent.mybatis.generate.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.RefMethod;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import cn.org.atool.fluent.mybatis.customize.IBaseEntity;
import cn.org.atool.fluent.mybatis.customize.MyCustomerInterface;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * StudentEntity: 数据映射实体定义
 *
 * @author Powered By Fluent Mybatis
 */
@Data
@Accessors(
    chain = true
)
@EqualsAndHashCode(
    callSuper = false
)
@FluentMybatis(
    table = "student",
    mapperBeanPrefix = "my",
    defaults = MyCustomerInterface.class
)
public class StudentEntity extends RichEntity implements IBaseEntity<StudentEntity> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("id")
    private Long id;

    /**
     * 创建时间
     */
    @TableField(
        value = "gmt_created",
        insert = "now()"
    )
    private Date gmtCreated;

    /**
     * 更新时间
     */
    @TableField(
        value = "gmt_modified",
        insert = "now()",
        update = "now()"
    )
    private Date gmtModified;

    /**
     * 是否逻辑删除
     */
    @TableField(
        value = "is_deleted",
        insert = "0"
    )
    private Boolean isDeleted;

    /**
     * 家庭详细住址
     */
    @TableField("address")
    private String address;

    /**
     * 年龄
     */
    @TableField("age")
    private Integer age;

    /**
     * 生日
     */
    @TableField("birthday")
    private Date birthday;

    /**
     * 积分
     */
    @TableField("bonus_points")
    private Long bonusPoints;

    /**
     * 数据隔离环境
     */
    @TableField("env")
    private String env;

    /**
     * 性别, 0:女; 1:男
     */
    @TableField("gender_man")
    private Integer genderMan;

    /**
     * 年级
     */
    @TableField("grade")
    private Integer grade;

    /**
     * home_address外键
     */
    @TableField("home_address_id")
    private Long homeAddressId;

    /**
     * 家庭所在区县
     */
    @TableField("home_county_id")
    private Long homeCountyId;

    /**
     * 电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 状态(字典)
     */
    @TableField("status")
    private String status;

    /**
     * 租户标识
     */
    @TableField("tenant")
    private Long tenant;

    /**
     * 名字
     */
    @TableField("user_name")
    private String userName;

    /**
     * 版本号
     */
    @TableField(
        value = "version",
        notLarge = false
    )
    private String version;

    @Override
    public Serializable findPk() {
        return this.id;
    }

    /**
     * 实现定义在{@link cn.org.atool.fluent.mybatis.base.EntityRefQuery}子类上
     */
    @RefMethod("studentId = id && isDeleted = isDeleted && env = env")
    public List<StudentScoreEntity> findStudentScoreList() {
        return super.invoke(true, "findStudentScoreList");
    }

    /**
     * 实现定义在{@link cn.org.atool.fluent.mybatis.base.EntityRefQuery}子类上
     */
    @RefMethod
    public StudentScoreEntity findEnglishScore() {
        return super.invoke(true, "findEnglishScore");
    }
}
