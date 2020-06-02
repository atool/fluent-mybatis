package cn.org.atool.fluent.mybatis.and;

import cn.org.atool.fluent.mybatis.base.IEntityUpdate;

import static cn.org.atool.fluent.mybatis.util.MybatisUtil.isNotEmpty;


public class SetString<U extends IEntityUpdate> extends SetObject<String, U> {
    public SetString(U wrapper, String column, String property) {
        super(wrapper, column, property);
    }

    /**
     * 当value为非空字符串时，更新记录值等于value
     *
     * @param value
     * @return
     */
    public U is_IfNotBlank(String value) {
        return super.is_If(isNotEmpty(value), value);
    }
}