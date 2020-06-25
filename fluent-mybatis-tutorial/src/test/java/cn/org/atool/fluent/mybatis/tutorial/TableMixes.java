package cn.org.atool.fluent.mybatis.tutorial;

import cn.org.atool.fluent.mybatis.tutorial.mix.*;
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
    public ReceivingAddressTableMix receivingAddressTableMix;

    @Mix
    public UserTableMix userTableMix;

    public void cleanAllTable() {
        this.receivingAddressTableMix.cleanReceivingAddressTable();
        this.userTableMix.cleanUserTable();
    }
}