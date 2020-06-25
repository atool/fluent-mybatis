package cn.org.atool.fluent.mybatis.tutorial.mix;

import cn.org.atool.fluent.mybatis.tutorial.datamap.table.ReceivingAddressTableMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库receiving_address表数据准备和校验通用方法
 *
 * @author generate code
 */
public class ReceivingAddressTableMix implements IMix {
    @Step("清空表[receiving_address]数据")
    public ReceivingAddressTableMix cleanReceivingAddressTable() {
        db.table("receiving_address").clean();
        return this;
    }

    @Step("准备表[receiving_address]数据{1}")
    public ReceivingAddressTableMix readyReceivingAddressTable(ReceivingAddressTableMap data) {
        db.table("receiving_address").insert(data);
        return this;
    }

    @Step("验证表[receiving_address]有全表数据{1}")
    public ReceivingAddressTableMix checkReceivingAddressTable(ReceivingAddressTableMap data) {
        db.table("receiving_address").query().eqDataMap(data, EqMode.IGNORE_ORDER);
        return this;
    }

    @Step("验证表[receiving_address]有符合条件{1}的数据{2}")
    public ReceivingAddressTableMix checkReceivingAddressTable(String where, ReceivingAddressTableMap data) {
        db.table("receiving_address").queryWhere(where).eqDataMap(data, EqMode.IGNORE_ORDER);
        return this;
    }

    @Step("验证表[receiving_address]有符合条件{1}的数据{2}")
    public ReceivingAddressTableMix checkReceivingAddressTable(ReceivingAddressTableMap where, ReceivingAddressTableMap data) {
        db.table("receiving_address").queryWhere(where).eqDataMap(data, EqMode.IGNORE_ORDER);
        return this;
    }

    @Step("验证表[receiving_address]有{1}条符合条件{2}的数据")
    public ReceivingAddressTableMix countReceivingAddressTable(int count, ReceivingAddressTableMap where) {
        db.table("receiving_address").queryWhere(where).sizeEq(count);
        return this;
    }

    @Step("验证表[receiving_address]有{1}条符合条件{2}的数据")
    public ReceivingAddressTableMix countReceivingAddressTable(int count, String where) {
        db.table("receiving_address").queryWhere(where).sizeEq(count);
        return this;
    }

    @Step("验证表[receiving_address]有{1}条数据")
    public ReceivingAddressTableMix countReceivingAddressTable(int count) {
        db.table("receiving_address").query().sizeEq(count);
        return this;
    }
}