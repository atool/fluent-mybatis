package cn.org.atool.mybatis.fluent.demo.mixes;

import cn.org.atool.mybatis.fluent.demo.mixes.mix.AddressTableMix;
import cn.org.atool.mybatis.fluent.demo.mixes.mix.UserTableMix;
import org.test4j.module.spec.annotations.Mix;
import org.test4j.module.spec.annotations.Mixes;

/**
 * Table Mix工具聚合
 *
 * @author generate code
 */
@Mixes
public class TableMixes {
    @Mix
    public AddressTableMix addressTableMix;

    @Mix
    public UserTableMix userTableMix;

    public void cleanAllTable() {
        this.addressTableMix.cleanAddressTable();
        this.userTableMix.cleanUserTable();
    }
}