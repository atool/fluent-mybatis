package cn.org.atool.fluent.mybatis.tutorial.helper;

import cn.org.atool.fluent.mybatis.tutorial.entity.ReceivingAddressEntity;
import cn.org.atool.fluent.mybatis.tutorial.mapping.ReceivingAddressMP;

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
            map.put(ReceivingAddressMP.id.name, entity.getId());
        }
        if (entity.getGmtModified() != null) {
            map.put(ReceivingAddressMP.gmtModified.name, entity.getGmtModified());
        }
        if (entity.getIsDeleted() != null) {
            map.put(ReceivingAddressMP.isDeleted.name, entity.getIsDeleted());
        }
        if (entity.getCity() != null) {
            map.put(ReceivingAddressMP.city.name, entity.getCity());
        }
        if (entity.getDetailAddress() != null) {
            map.put(ReceivingAddressMP.detailAddress.name, entity.getDetailAddress());
        }
        if (entity.getDistrict() != null) {
            map.put(ReceivingAddressMP.district.name, entity.getDistrict());
        }
        if (entity.getGmtCreate() != null) {
            map.put(ReceivingAddressMP.gmtCreate.name, entity.getGmtCreate());
        }
        if (entity.getProvince() != null) {
            map.put(ReceivingAddressMP.province.name, entity.getProvince());
        }
        if (entity.getUserId() != null) {
            map.put(ReceivingAddressMP.userId.name, entity.getUserId());
        }
        return map;
    }

    /**
     * ReceivingAddressEntity对象转换为HashMap，key值是数据库字段
     *
     * @param entity
     * @return
     */
    public static Map<String, Object> column(ReceivingAddressEntity entity){
        Map<String, Object> map = new HashMap<>();
        if (entity.getId() != null) {
            map.put(ReceivingAddressMP.id.column, entity.getId());
        }
        if (entity.getGmtModified() != null) {
            map.put(ReceivingAddressMP.gmtModified.column, entity.getGmtModified());
        }
        if (entity.getIsDeleted() != null) {
            map.put(ReceivingAddressMP.isDeleted.column, entity.getIsDeleted());
        }
        if (entity.getCity() != null) {
            map.put(ReceivingAddressMP.city.column, entity.getCity());
        }
        if (entity.getDetailAddress() != null) {
            map.put(ReceivingAddressMP.detailAddress.column, entity.getDetailAddress());
        }
        if (entity.getDistrict() != null) {
            map.put(ReceivingAddressMP.district.column, entity.getDistrict());
        }
        if (entity.getGmtCreate() != null) {
            map.put(ReceivingAddressMP.gmtCreate.column, entity.getGmtCreate());
        }
        if (entity.getProvince() != null) {
            map.put(ReceivingAddressMP.province.column, entity.getProvince());
        }
        if (entity.getUserId() != null) {
            map.put(ReceivingAddressMP.userId.column, entity.getUserId());
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
        if (map.containsKey(ReceivingAddressMP.id.name)) {
            entity.setId((Long) map.get(ReceivingAddressMP.id.name));
        }
        if (map.containsKey(ReceivingAddressMP.gmtModified.name)) {
            entity.setGmtModified((Date) map.get(ReceivingAddressMP.gmtModified.name));
        }
        if (map.containsKey(ReceivingAddressMP.isDeleted.name)) {
            entity.setIsDeleted((Boolean) map.get(ReceivingAddressMP.isDeleted.name));
        }
        if (map.containsKey(ReceivingAddressMP.city.name)) {
            entity.setCity((String) map.get(ReceivingAddressMP.city.name));
        }
        if (map.containsKey(ReceivingAddressMP.detailAddress.name)) {
            entity.setDetailAddress((String) map.get(ReceivingAddressMP.detailAddress.name));
        }
        if (map.containsKey(ReceivingAddressMP.district.name)) {
            entity.setDistrict((String) map.get(ReceivingAddressMP.district.name));
        }
        if (map.containsKey(ReceivingAddressMP.gmtCreate.name)) {
            entity.setGmtCreate((Date) map.get(ReceivingAddressMP.gmtCreate.name));
        }
        if (map.containsKey(ReceivingAddressMP.province.name)) {
            entity.setProvince((String) map.get(ReceivingAddressMP.province.name));
        }
        if (map.containsKey(ReceivingAddressMP.userId.name)) {
            entity.setUserId((Long) map.get(ReceivingAddressMP.userId.name));
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