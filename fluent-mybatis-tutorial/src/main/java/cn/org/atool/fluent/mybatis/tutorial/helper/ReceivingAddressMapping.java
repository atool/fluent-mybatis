package cn.org.atool.fluent.mybatis.tutorial.helper;

import cn.org.atool.fluent.mybatis.base.model.FieldMeta;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName ReceivingAddressMapping
 * @Description receiving_address映射定义
 *
 * @author generate code
 */
public interface ReceivingAddressMapping {
    /**
     * 表名称
     */
    String Table_Name = "receiving_address";
    /**
    * 实体名称
    */
    String Entity_NAME = "ReceivingAddressEntity";

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
     * city字段映射
     * <p>城市</p>
     */
    FieldMeta city = new FieldMeta("city", "city");
    /**
     * detailAddress字段映射
     * <p>详细住址</p>
     */
    FieldMeta detailAddress = new FieldMeta("detailAddress", "detail_address");
    /**
     * district字段映射
     * <p>区</p>
     */
    FieldMeta district = new FieldMeta("district", "district");
    /**
     * gmtCreate字段映射
     * <p>创建时间</p>
     */
    FieldMeta gmtCreate = new FieldMeta("gmtCreate", "gmt_create");
    /**
     * province字段映射
     * <p>省份</p>
     */
    FieldMeta province = new FieldMeta("province", "province");
    /**
     * userId字段映射
     * <p>用户id</p>
     */
    FieldMeta userId = new FieldMeta("userId", "user_id");

    /**
     * 实例属性和数据库字段对应表
     */
    Map<String, String> Property2Column = new HashMap<String,String>(){
        {
            this.put(id.name, id.column);
            this.put(gmtModified.name, gmtModified.column);
            this.put(isDeleted.name, isDeleted.column);
            this.put(city.name, city.column);
            this.put(detailAddress.name, detailAddress.column);
            this.put(district.name, district.column);
            this.put(gmtCreate.name, gmtCreate.column);
            this.put(province.name, province.column);
            this.put(userId.name, userId.column);
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
            this.add(city.column);
            this.add(detailAddress.column);
            this.add(district.column);
            this.add(gmtCreate.column);
            this.add(province.column);
            this.add(userId.column);
        }
    };
}