package cn.org.atool.fluent.mybatis.tutorial.datamap.entity;

import org.test4j.module.ICore.DataMap;
import org.test4j.tools.datagen.KeyValue;

/**
 * @ClassName ReceivingAddressEntityMap
 * @Description ReceivingAddressEntityMap
 *
 * @author generate code
 */
public class ReceivingAddressEntityMap extends DataMap<ReceivingAddressEntityMap> {
    /**
     * 设置ReceivingAddressEntity对象id字段值
     */
    public transient final KeyValue<ReceivingAddressEntityMap> id = new KeyValue(this, "id");
    /**
     * 设置ReceivingAddressEntity对象gmtModified字段值
     */
    public transient final KeyValue<ReceivingAddressEntityMap> gmtModified = new KeyValue(this, "gmtModified");
    /**
     * 设置ReceivingAddressEntity对象isDeleted字段值
     */
    public transient final KeyValue<ReceivingAddressEntityMap> isDeleted = new KeyValue(this, "isDeleted");
    /**
     * 设置ReceivingAddressEntity对象city字段值
     */
    public transient final KeyValue<ReceivingAddressEntityMap> city = new KeyValue(this, "city");
    /**
     * 设置ReceivingAddressEntity对象detailAddress字段值
     */
    public transient final KeyValue<ReceivingAddressEntityMap> detailAddress = new KeyValue(this, "detailAddress");
    /**
     * 设置ReceivingAddressEntity对象district字段值
     */
    public transient final KeyValue<ReceivingAddressEntityMap> district = new KeyValue(this, "district");
    /**
     * 设置ReceivingAddressEntity对象gmtCreate字段值
     */
    public transient final KeyValue<ReceivingAddressEntityMap> gmtCreate = new KeyValue(this, "gmtCreate");
    /**
     * 设置ReceivingAddressEntity对象province字段值
     */
    public transient final KeyValue<ReceivingAddressEntityMap> province = new KeyValue(this, "province");
    /**
     * 设置ReceivingAddressEntity对象userId字段值
     */
    public transient final KeyValue<ReceivingAddressEntityMap> userId = new KeyValue(this, "userId");

    public ReceivingAddressEntityMap() {
        super();
    }

    public ReceivingAddressEntityMap(int size) {
        super(size);
    }

    public static ReceivingAddressEntityMap create() {
        return new ReceivingAddressEntityMap();
    }

    public static ReceivingAddressEntityMap create(int size) {
        return new ReceivingAddressEntityMap(size);
    }

    public static class Factory {
        public ReceivingAddressEntityMap create() {
            return ReceivingAddressEntityMap.create();
        }

        public ReceivingAddressEntityMap create(int size) {
            return ReceivingAddressEntityMap.create(size);
        }
    }
}