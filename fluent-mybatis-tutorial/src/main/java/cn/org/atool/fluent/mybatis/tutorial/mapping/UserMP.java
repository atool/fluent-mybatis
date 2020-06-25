package cn.org.atool.fluent.mybatis.tutorial.mapping;

import cn.org.atool.fluent.mybatis.base.model.FieldMeta;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName UserMP
 * @Description user映射定义
 *
 * @author generate code
 */
public interface UserMP {
    /**
     * 表名称
     */
    String Table_Name = "user";
    /**
    * 实体名称
    */
    String Entity_NAME = "UserEntity";

    /**
     * id字段映射
     * <p>主键id</p>
     */
    FieldMeta id = new FieldMeta("id", "id");
    /**
     * gmtModified字段映射
     * <p>更新时间</p>
     */
    FieldMeta gmtModified = new FieldMeta("gmtModified", "gmt_modified");
    /**
     * isDeleted字段映射
     * <p>是否逻辑删除</p>
     */
    FieldMeta isDeleted = new FieldMeta("isDeleted", "is_deleted");
    /**
     * account字段映射
     * <p>账号</p>
     */
    FieldMeta account = new FieldMeta("account", "account");
    /**
     * avatar字段映射
     * <p>头像</p>
     */
    FieldMeta avatar = new FieldMeta("avatar", "avatar");
    /**
     * birthday字段映射
     * <p>生日</p>
     */
    FieldMeta birthday = new FieldMeta("birthday", "birthday");
    /**
     * eMail字段映射
     * <p>电子邮件</p>
     */
    FieldMeta eMail = new FieldMeta("eMail", "e_mail");
    /**
     * gmtCreate字段映射
     * <p>创建时间</p>
     */
    FieldMeta gmtCreate = new FieldMeta("gmtCreate", "gmt_create");
    /**
     * password字段映射
     * <p>密码</p>
     */
    FieldMeta password = new FieldMeta("password", "password");
    /**
     * phone字段映射
     * <p>电话</p>
     */
    FieldMeta phone = new FieldMeta("phone", "phone");
    /**
     * status字段映射
     * <p>状态(字典)</p>
     */
    FieldMeta status = new FieldMeta("status", "status");
    /**
     * userName字段映射
     * <p>名字</p>
     */
    FieldMeta userName = new FieldMeta("userName", "user_name");

    /**
     * 实例属性和数据库字段对应表
     */
    Map<String, String> Property2Column = new HashMap<String,String>(){
        {
            this.put(id.name, id.column);
            this.put(gmtModified.name, gmtModified.column);
            this.put(isDeleted.name, isDeleted.column);
            this.put(account.name, account.column);
            this.put(avatar.name, avatar.column);
            this.put(birthday.name, birthday.column);
            this.put(eMail.name, eMail.column);
            this.put(gmtCreate.name, gmtCreate.column);
            this.put(password.name, password.column);
            this.put(phone.name, phone.column);
            this.put(status.name, status.column);
            this.put(userName.name, userName.column);
        }
    };

    /**
     * 数据库所有字段列表
     */
    Set<String> ALL_COLUMNS = new HashSet<String>() {
        {
            this.add(id.column);
            this.add(gmtModified.column);
            this.add(isDeleted.column);
            this.add(account.column);
            this.add(avatar.column);
            this.add(birthday.column);
            this.add(eMail.column);
            this.add(gmtCreate.column);
            this.add(password.column);
            this.add(phone.column);
            this.add(status.column);
            this.add(userName.column);
        }
    };
}