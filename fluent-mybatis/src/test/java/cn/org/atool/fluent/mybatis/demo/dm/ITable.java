package cn.org.atool.fluent.mybatis.demo.dm;

import cn.org.atool.fluent.mybatis.demo.mapping.AddressMP;
import cn.org.atool.fluent.mybatis.demo.mapping.UserMP;

/**
 *
 * @author: generate code
 */
public interface ITable {

    String t_address = AddressMP.Table_Name;

    String t_user = UserMP.Table_Name;
}