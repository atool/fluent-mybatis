package cn.org.atool.fluent.mybatis.demo.generate;

import cn.org.atool.fluent.mybatis.demo.generate.mapping.*;

/**
 *
 * @author: generate code
 */
public interface ITable {

    String t_address = AddressMP.Table_Name;

    String t_user = UserMP.Table_Name;

    String t_no_auto_id = NoAutoIdMP.Table_Name;

    String t_no_primary = NoPrimaryMP.Table_Name;
}