package cn.org.atool.fluent.mybatis.demo.generate.mix;

import cn.org.atool.fluent.mybatis.demo.generate.datamap.table.NoAutoIdTableMap;
import org.test4j.hamcrest.matcher.property.reflection.EqMode;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

import static cn.org.atool.fluent.mybatis.demo.generate.mapping.NoAutoIdMP.Table_Name;

/**
 * 数据库no_auto_id表数据准备和校验通用方法
 *
 * @author generate code
 */
public class NoAutoIdTableMix implements IMix {
    @Step("清空表[" + Table_Name + "]数据")
    public NoAutoIdTableMix cleanNoAutoIdTable() {
        db.table(Table_Name).clean();
        return this;
    }

    @Step("准备表[" + Table_Name + "]数据{1}")
    public NoAutoIdTableMix readyNoAutoIdTable(NoAutoIdTableMap data) {
        db.table(Table_Name).insert(data);
        return this;
    }

    @Step("验证表[" + Table_Name + "]有全表数据{1}")
    public NoAutoIdTableMix checkNoAutoIdTable(NoAutoIdTableMap data) {
        db.table(Table_Name).query().eqDataMap(data, EqMode.IGNORE_ORDER);
        return this;
    }

    @Step("验证表[" + Table_Name + "]有符合条件{1}的数据{2}")
    public NoAutoIdTableMix checkNoAutoIdTable(String where, NoAutoIdTableMap data) {
        db.table(Table_Name).queryWhere(where).eqDataMap(data, EqMode.IGNORE_ORDER);
        return this;
    }

    @Step("验证表[" + Table_Name + "]有符合条件{1}的数据{2}")
    public NoAutoIdTableMix checkNoAutoIdTable(NoAutoIdTableMap where, NoAutoIdTableMap data) {
        db.table(Table_Name).queryWhere(where).eqDataMap(data, EqMode.IGNORE_ORDER);
        return this;
    }

    @Step("验证表[" + Table_Name + "]有{1}条符合条件{2}的数据")
    public NoAutoIdTableMix countNoAutoIdTable(int count, NoAutoIdTableMap where) {
        db.table(Table_Name).queryWhere(where).sizeEq(count);
        return this;
    }

    @Step("验证表[" + Table_Name + "]有{1}条符合条件{2}的数据")
    public NoAutoIdTableMix countNoAutoIdTable(int count, String where) {
        db.table(Table_Name).queryWhere(where).sizeEq(count);
        return this;
    }

    @Step("验证表[" + Table_Name + "]有{1}条数据")
    public NoAutoIdTableMix countNoAutoIdTable(int count) {
        db.table(Table_Name).query().sizeEq(count);
        return this;
    }
}