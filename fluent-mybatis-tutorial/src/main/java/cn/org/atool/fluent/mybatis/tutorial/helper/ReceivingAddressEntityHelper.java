package cn.org.atool.fluent.mybatis.tutorial.helper;

import cn.org.atool.fluent.mybatis.tutorial.entity.ReceivingAddressEntity;
import cn.org.atool.fluent.mybatis.tutorial.helper.ReceivingAddressMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;

/**
 * <p>
 * 用户收货地址
 * </p>
 *
 * @author generate code
 */
public class ReceivingAddressEntityHelper {
    /**
     * ReceivingAddressEntity对象转换为HashMap，key值是对象属性
     *
     * @param entity
     * @return
     */
    public static Map<String, Object> map(ReceivingAddressEntity entity){
        Map<String, Object> map = new HashMap<>();
        if (entity.getId() != null) {
            map.put(ReceivingAddressMapping.id.name, entity.getId());
        }
        if (entity.getGmtModified() != null) {
            map.put(ReceivingAddressMapping.gmtModified.name, entity.getGmtModified());
        }
        if (entity.getIsDeleted() != null) {
            map.put(ReceivingAddressMapping.isDeleted.name, entity.getIsDeleted());
        }
        if (entity.getCity() != null) {
            map.put(ReceivingAddressMapping.city.name, entity.getCity());
        }
        if (entity.getDetailAddress() != null) {
            map.put(ReceivingAddressMapping.detailAddress.name, entity.getDetailAddress());
        }
        if (entity.getDistrict() != null) {
            map.put(ReceivingAddressMapping.district.name, entity.getDistrict());
        }
        if (entity.getGmtCreate() != null) {
            map.put(ReceivingAddressMapping.gmtCreate.name, entity.getGmtCreate());
        }
        if (entity.getProvince() != null) {
            map.put(ReceivingAddressMapping.province.name, entity.getProvince());
        }
        if (entity.getUserId() != null) {
            map.put(ReceivingAddressMapping.userId.name, entity.getUserId());
        }
        return map;
    }

    /**
     * ReceivingAddressEntity对象转换为HashMap，key值是数据库字段
     *
     * @param entity
     * @return
     */
    public static Map<String, Object> columnMap(ReceivingAddressEntity entity){
        Map<String, Object> map = new HashMap<>();
        if (entity.getId() != null) {
            map.put(ReceivingAddressMapping.id.column, entity.getId());
        }
        if (entity.getGmtModified() != null) {
            map.put(ReceivingAddressMapping.gmtModified.column, entity.getGmtModified());
        }
        if (entity.getIsDeleted() != null) {
            map.put(ReceivingAddressMapping.isDeleted.column, entity.getIsDeleted());
        }
        if (entity.getCity() != null) {
            map.put(ReceivingAddressMapping.city.column, entity.getCity());
        }
        if (entity.getDetailAddress() != null) {
            map.put(ReceivingAddressMapping.detailAddress.column, entity.getDetailAddress());
        }
        if (entity.getDistrict() != null) {
            map.put(ReceivingAddressMapping.district.column, entity.getDistrict());
        }
        if (entity.getGmtCreate() != null) {
            map.put(ReceivingAddressMapping.gmtCreate.column, entity.getGmtCreate());
        }
        if (entity.getProvince() != null) {
            map.put(ReceivingAddressMapping.province.column, entity.getProvince());
        }
        if (entity.getUserId() != null) {
            map.put(ReceivingAddressMapping.userId.column, entity.getUserId());
        }
        return map;
    }

    /**
     * map对应属性值设置到ReceivingAddressEntity对象中
     *
     * @param map
     * @return
     */
    public static ReceivingAddressEntity entity(Map<String, Object> map){
        ReceivingAddressEntity entity = new ReceivingAddressEntity();
        if (map.containsKey(ReceivingAddressMapping.id.name)) {
            entity.setId((Long) map.get(ReceivingAddressMapping.id.name));
        }
        if (map.containsKey(ReceivingAddressMapping.gmtModified.name)) {
            entity.setGmtModified((Date) map.get(ReceivingAddressMapping.gmtModified.name));
        }
        if (map.containsKey(ReceivingAddressMapping.isDeleted.name)) {
            entity.setIsDeleted((Boolean) map.get(ReceivingAddressMapping.isDeleted.name));
        }
        if (map.containsKey(ReceivingAddressMapping.city.name)) {
            entity.setCity((String) map.get(ReceivingAddressMapping.city.name));
        }
        if (map.containsKey(ReceivingAddressMapping.detailAddress.name)) {
            entity.setDetailAddress((String) map.get(ReceivingAddressMapping.detailAddress.name));
        }
        if (map.containsKey(ReceivingAddressMapping.district.name)) {
            entity.setDistrict((String) map.get(ReceivingAddressMapping.district.name));
        }
        if (map.containsKey(ReceivingAddressMapping.gmtCreate.name)) {
            entity.setGmtCreate((Date) map.get(ReceivingAddressMapping.gmtCreate.name));
        }
        if (map.containsKey(ReceivingAddressMapping.province.name)) {
            entity.setProvince((String) map.get(ReceivingAddressMapping.province.name));
        }
        if (map.containsKey(ReceivingAddressMapping.userId.name)) {
            entity.setUserId((Long) map.get(ReceivingAddressMapping.userId.name));
        }
        return entity;
    }

    public static ReceivingAddressEntity copy(ReceivingAddressEntity entity) {
        ReceivingAddressEntity copy = new ReceivingAddressEntity();
        {
            copy.setId(entity.getId());
            copy.setGmtModified(entity.getGmtModified());
            copy.setIsDeleted(entity.getIsDeleted());
            copy.setCity(entity.getCity());
            copy.setDetailAddress(entity.getDetailAddress());
            copy.setDistrict(entity.getDistrict());
            copy.setGmtCreate(entity.getGmtCreate());
            copy.setProvince(entity.getProvince());
            copy.setUserId(entity.getUserId());
        }
        return copy;
    }
}