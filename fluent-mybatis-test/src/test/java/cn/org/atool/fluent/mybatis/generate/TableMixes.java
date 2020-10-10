package cn.org.atool.fluent.mybatis.generate;

import cn.org.atool.fluent.mybatis.generate.mix.*;
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
    public NoPrimaryTableMix noPrimaryTableMix;

    @Mix
    public UserTableMix userTableMix;

    @Mix
    public NoAutoIdTableMix noAutoIdTableMix;

    public void cleanAllTable() {
        this.addressTableMix.cleanAddressTable();
        this.noPrimaryTableMix.cleanNoPrimaryTable();
        this.userTableMix.cleanUserTable();
        this.noAutoIdTableMix.cleanNoAutoIdTable();
    }
}
