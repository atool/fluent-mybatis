package cn.org.atool.fluent.mybatis.tutorial.helper;

import cn.org.atool.fluent.mybatis.tutorial.entity.UserEntity;
import cn.org.atool.fluent.mybatis.tutorial.helper.UserMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author generate code
 */
public class UserEntityHelper {
    /**
     * UserEntity对象转换为HashMap，key值是对象属性
     *
     * @param entity
     * @return
     */
    public static Map<String, Object> map(UserEntity entity){
        Map<String, Object> map = new HashMap<>();
        if (entity.getId() != null) {
            map.put(UserMapping.id.name, entity.getId());
        }
        if (entity.getGmtModified() != null) {
            map.put(UserMapping.gmtModified.name, entity.getGmtModified());
        }
        if (entity.getIsDeleted() != null) {
            map.put(UserMapping.isDeleted.name, entity.getIsDeleted());
        }
        if (entity.getAccount() != null) {
            map.put(UserMapping.account.name, entity.getAccount());
        }
        if (entity.getAvatar() != null) {
            map.put(UserMapping.avatar.name, entity.getAvatar());
        }
        if (entity.getBirthday() != null) {
            map.put(UserMapping.birthday.name, entity.getBirthday());
        }
        if (entity.getEMail() != null) {
            map.put(UserMapping.eMail.name, entity.getEMail());
        }
        if (entity.getGmtCreate() != null) {
            map.put(UserMapping.gmtCreate.name, entity.getGmtCreate());
        }
        if (entity.getPassword() != null) {
            map.put(UserMapping.password.name, entity.getPassword());
        }
        if (entity.getPhone() != null) {
            map.put(UserMapping.phone.name, entity.getPhone());
        }
        if (entity.getStatus() != null) {
            map.put(UserMapping.status.name, entity.getStatus());
        }
        if (entity.getUserName() != null) {
            map.put(UserMapping.userName.name, entity.getUserName());
        }
        return map;
    }

    /**
     * UserEntity对象转换为HashMap，key值是数据库字段
     *
     * @param entity
     * @return
     */
    public static Map<String, Object> columnMap(UserEntity entity){
        Map<String, Object> map = new HashMap<>();
        if (entity.getId() != null) {
            map.put(UserMapping.id.column, entity.getId());
        }
        if (entity.getGmtModified() != null) {
            map.put(UserMapping.gmtModified.column, entity.getGmtModified());
        }
        if (entity.getIsDeleted() != null) {
            map.put(UserMapping.isDeleted.column, entity.getIsDeleted());
        }
        if (entity.getAccount() != null) {
            map.put(UserMapping.account.column, entity.getAccount());
        }
        if (entity.getAvatar() != null) {
            map.put(UserMapping.avatar.column, entity.getAvatar());
        }
        if (entity.getBirthday() != null) {
            map.put(UserMapping.birthday.column, entity.getBirthday());
        }
        if (entity.getEMail() != null) {
            map.put(UserMapping.eMail.column, entity.getEMail());
        }
        if (entity.getGmtCreate() != null) {
            map.put(UserMapping.gmtCreate.column, entity.getGmtCreate());
        }
        if (entity.getPassword() != null) {
            map.put(UserMapping.password.column, entity.getPassword());
        }
        if (entity.getPhone() != null) {
            map.put(UserMapping.phone.column, entity.getPhone());
        }
        if (entity.getStatus() != null) {
            map.put(UserMapping.status.column, entity.getStatus());
        }
        if (entity.getUserName() != null) {
            map.put(UserMapping.userName.column, entity.getUserName());
        }
        return map;
    }

    /**
     * map对应属性值设置到UserEntity对象中
     *
     * @param map
     * @return
     */
    public static UserEntity entity(Map<String, Object> map){
        UserEntity entity = new UserEntity();
        if (map.containsKey(UserMapping.id.name)) {
            entity.setId((Long) map.get(UserMapping.id.name));
        }
        if (map.containsKey(UserMapping.gmtModified.name)) {
            entity.setGmtModified((Date) map.get(UserMapping.gmtModified.name));
        }
        if (map.containsKey(UserMapping.isDeleted.name)) {
            entity.setIsDeleted((Boolean) map.get(UserMapping.isDeleted.name));
        }
        if (map.containsKey(UserMapping.account.name)) {
            entity.setAccount((String) map.get(UserMapping.account.name));
        }
        if (map.containsKey(UserMapping.avatar.name)) {
            entity.setAvatar((String) map.get(UserMapping.avatar.name));
        }
        if (map.containsKey(UserMapping.birthday.name)) {
            entity.setBirthday((Date) map.get(UserMapping.birthday.name));
        }
        if (map.containsKey(UserMapping.eMail.name)) {
            entity.setEMail((String) map.get(UserMapping.eMail.name));
        }
        if (map.containsKey(UserMapping.gmtCreate.name)) {
            entity.setGmtCreate((Date) map.get(UserMapping.gmtCreate.name));
        }
        if (map.containsKey(UserMapping.password.name)) {
            entity.setPassword((String) map.get(UserMapping.password.name));
        }
        if (map.containsKey(UserMapping.phone.name)) {
            entity.setPhone((String) map.get(UserMapping.phone.name));
        }
        if (map.containsKey(UserMapping.status.name)) {
            entity.setStatus((String) map.get(UserMapping.status.name));
        }
        if (map.containsKey(UserMapping.userName.name)) {
            entity.setUserName((String) map.get(UserMapping.userName.name));
        }
        return entity;
    }

    public static UserEntity copy(UserEntity entity) {
        UserEntity copy = new UserEntity();
        {
            copy.setId(entity.getId());
            copy.setGmtModified(entity.getGmtModified());
            copy.setIsDeleted(entity.getIsDeleted());
            copy.setAccount(entity.getAccount());
            copy.setAvatar(entity.getAvatar());
            copy.setBirthday(entity.getBirthday());
            copy.setEMail(entity.getEMail());
            copy.setGmtCreate(entity.getGmtCreate());
            copy.setPassword(entity.getPassword());
            copy.setPhone(entity.getPhone());
            copy.setStatus(entity.getStatus());
            copy.setUserName(entity.getUserName());
        }
        return copy;
    }
}