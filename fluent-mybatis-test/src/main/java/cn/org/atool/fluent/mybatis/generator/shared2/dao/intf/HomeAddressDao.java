package cn.org.atool.fluent.mybatis.generator.shared2.dao.intf;

import cn.org.atool.fluent.form.annotation.Entry;
import cn.org.atool.fluent.mybatis.base.IBaseDao;
import cn.org.atool.fluent.mybatis.formservice.model.HomeAddress;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.HomeAddressEntity;

import javax.validation.constraints.NotNull;
import java.util.List;

import static cn.org.atool.fluent.form.annotation.EntryType.IN;
import static cn.org.atool.fluent.form.annotation.EntryType.StartWith;

/**
 * HomeAddressDao: 数据操作接口
 * <p>
 * 这只是一个减少手工创建的模板文件
 * 可以任意添加方法和实现, 更改作者和重定义类名
 * <p/>@author Powered By Fluent Mybatis
 */
@SuppressWarnings("UnusedReturnValue")
public interface HomeAddressDao extends IBaseDao<HomeAddressEntity> {
    /**
     * 根据city and district查询列表
     *
     * @return ignore
     */
    List<HomeAddress> findByCityAndDistrict(
        @NotNull String city, @Entry(type = StartWith) String district);

    /**
     * 根据city and district查询列表
     *
     * @return ignore
     */
    List<HomeAddress> findByCityOrDistrictOrAddress(
        @NotNull String city,
        @Entry(type = IN) String district,
        @Entry(type = StartWith) String address);


    List<HomeAddress> top3ByCityOrDistrictOrAddressOrderByCityAscDistrictDesc(
        @NotNull String city,
        @Entry(type = IN) String district,
        @Entry(type = StartWith) String address);

    List<HomeAddress> distinctByCityOrderByCityAscDistrict(@NotNull String city);

    boolean existsByCity(@NotNull String city);

    HomeAddress findHomeAddress(
        @Entry(value = "address", type = StartWith) String address);

    String sayImplement();

    default String sayInterface() {
        return "HomeAddressDao";
    }
}