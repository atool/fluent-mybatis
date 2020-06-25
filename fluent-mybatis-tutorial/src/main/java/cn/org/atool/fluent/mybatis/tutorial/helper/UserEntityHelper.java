package cn.org.atool.fluent.mybatis.tutorial.helper;

import cn.org.atool.fluent.mybatis.tutorial.entity.UserEntity;
import cn.org.atool.fluent.mybatis.tutorial.mapping.UserMP;

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
            map.put(UserMP.id.name, entity.getId());
        }
        if (entity.getGmtModified() != null) {
            map.put(UserMP.gmtModified.name, entity.getGmtModified());
        }
        if (entity.getIsDeleted() != null) {
            map.put(UserMP.isDeleted.name, entity.getIsDeleted());
        }
        if (entity.getAccount() != null) {
            map.put(UserMP.account.name, entity.getAccount());
        }
        if (entity.getAvatar() != null) {
            map.put(UserMP.avatar.name, entity.getAvatar());
        }
        if (entity.getBirthday() != null) {
            map.put(UserMP.birthday.name, entity.getBirthday());
        }
        if (entity.getEMail() != null) {
            map.put(UserMP.eMail.name, entity.getEMail());
        }
        if (entity.getGmtCreate() != null) {
            map.put(UserMP.gmtCreate.name, entity.getGmtCreate());
        }
        if (entity.getPassword() != null) {
            map.put(UserMP.password.name, entity.getPassword());
        }
        if (entity.getPhone() != null) {
            map.put(UserMP.phone.name, entity.getPhone());
        }
        if (entity.getStatus() != null) {
            map.put(UserMP.status.name, entity.getStatus());
        }
        if (entity.getUserName() != null) {
            map.put(UserMP.userName.name, entity.getUserName());
        }
        return map;
    }

    /**
     * UserEntity对象转换为HashMap，key值是数据库字段
     *
     * @param entity
     * @return
     */
    public static Map<String, Object> column(UserEntity entity){
        Map<String, Object> map = new HashMap<>();
        if (entity.getId() != null) {
            map.put(UserMP.id.column, entity.getId());
        }
        if (entity.getGmtModified() != null) {
            map.put(UserMP.gmtModified.column, entity.getGmtModified());
        }
        if (entity.getIsDeleted() != null) {
            map.put(UserMP.isDeleted.column, entity.getIsDeleted());
        }
        if (entity.getAccount() != null) {
            map.put(UserMP.account.column, entity.getAccount());
        }
        if (entity.getAvatar() != null) {
            map.put(UserMP.avatar.column, entity.getAvatar());
        }
        if (entity.getBirthday() != null) {
            map.put(UserMP.birthday.column, entity.getBirthday());
        }
        if (entity.geteMail() != null) {
            map.put(UserMP.eMail.column, entity.geteMail());
        }
        if (entity.getGmtCreate() != null) {
            map.put(UserMP.gmtCreate.column, entity.getGmtCreate());
        }
        if (entity.getPassword() != null) {
            map.put(UserMP.password.column, entity.getPassword());
        }
        if (entity.getPhone() != null) {
            map.put(UserMP.phone.column, entity.getPhone());
        }
        if (entity.getStatus() != null) {
            map.put(UserMP.status.column, entity.getStatus());
        }
        if (entity.getUserName() != null) {
            map.put(UserMP.userName.column, entity.getUserName());
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
        if (map.containsKey(UserMP.id.name)) {
            entity.setId((Long) map.get(UserMP.id.name));
        }
        if (map.containsKey(UserMP.gmtModified.name)) {
            entity.setGmtModified((Date) map.get(UserMP.gmtModified.name));
        }
        if (map.containsKey(UserMP.isDeleted.name)) {
            entity.setIsDeleted((Boolean) map.get(UserMP.isDeleted.name));
        }
        if (map.containsKey(UserMP.account.name)) {
            entity.setAccount((String) map.get(UserMP.account.name));
        }
        if (map.containsKey(UserMP.avatar.name)) {
            entity.setAvatar((String) map.get(UserMP.avatar.name));
        }
        if (map.containsKey(UserMP.birthday.name)) {
            entity.setBirthday((Date) map.get(UserMP.birthday.name));
        }
        if (map.containsKey(UserMP.eMail.name)) {
            entity.seteMail((String) map.get(UserMP.eMail.name));
        }
        if (map.containsKey(UserMP.gmtCreate.name)) {
            entity.setGmtCreate((Date) map.get(UserMP.gmtCreate.name));
        }
        if (map.containsKey(UserMP.password.name)) {
            entity.setPassword((String) map.get(UserMP.password.name));
        }
        if (map.containsKey(UserMP.phone.name)) {
            entity.setPhone((String) map.get(UserMP.phone.name));
        }
        if (map.containsKey(UserMP.status.name)) {
            entity.setStatus((String) map.get(UserMP.status.name));
        }
        if (map.containsKey(UserMP.userName.name)) {
            entity.setUserName((String) map.get(UserMP.userName.name));
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
            copy.seteMail(entity.geteMail());
            copy.setGmtCreate(entity.getGmtCreate());
            copy.setPassword(entity.getPassword());
            copy.setPhone(entity.getPhone());
            copy.setStatus(entity.getStatus());
            copy.setUserName(entity.getUserName());
        }
        return copy;
    }
}