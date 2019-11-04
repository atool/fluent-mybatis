package cn.org.atool.mybatis.fluent.demo.dm;

import cn.org.atool.mybatis.fluent.demo.mapping.AddressMP;
import cn.org.atool.mybatis.fluent.demo.mapping.UserMP;

/**
 *
 * @author: generate code
 */
public interface ITable {

    String t_address = AddressMP.Table_Name;

    String t_user = UserMP.Table_Name;
}