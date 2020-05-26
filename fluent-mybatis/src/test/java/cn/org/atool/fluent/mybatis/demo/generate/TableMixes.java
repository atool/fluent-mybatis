package cn.org.atool.fluent.mybatis.demo.generate;

import cn.org.atool.fluent.mybatis.demo.generate.mix.*;
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

    @Mix
    public NoPrimaryTableMix noPrimaryTableMix;

    @Mix
    public NoAutoIdTableMix noAutoIdTableMix;

    public void cleanAllTable() {
        this.addressTableMix.cleanAddressTable();
        this.userTableMix.cleanUserTable();
        this.noPrimaryTableMix.cleanNoPrimaryTable();
        this.noAutoIdTableMix.cleanNoAutoIdTable();
    }
}