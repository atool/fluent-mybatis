package cn.org.atool.fluent.mybatis.generator.shared2.dao.intf;

import cn.org.atool.fluent.form.annotation.Entry;
import cn.org.atool.fluent.mybatis.base.IBaseDao;
import cn.org.atool.fluent.mybatis.formservice.model.HomeAddress;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.HomeAddressEntity;

import static cn.org.atool.fluent.form.annotation.EntryType.LikeLeft;

/**
 * HomeAddressDao: 数据操作接口
 * <p>
 * 这只是一个减少手工创建的模板文件
 * 可以任意添加方法和实现, 更改作者和重定义类名
 * <p/>@author Powered By Fluent Mybatis
 */
public interface HomeAddressDao extends IBaseDao<HomeAddressEntity> {
    String sayImplement();

    default String sayInterface() {
        return "HomeAddressDao";
    }

    HomeAddress findHomeAddress(@Entry(value = "address", type = LikeLeft) String address);
}