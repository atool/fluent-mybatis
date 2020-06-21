package cn.org.atool.fluent.mybatis.condition.apply;

import cn.org.atool.fluent.mybatis.condition.base.BaseSetter;
import cn.org.atool.fluent.mybatis.interfaces.IUpdate;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotEmpty;

/**
 * SetString: 更新字符串字段值
 *
 * @param <U> 更新器
 * @author darui.wu
 */
public class SetString<U extends IUpdate> extends SetObject<String, U> {
    public SetString(BaseSetter setter, String column, String property) {
        super(setter, column, property);
    }

    /**
     * 当value为非空字符串时，更新记录值等于value
     *
     * @param value 更新值
     * @return 更新器
     */
    public U is_IfNotBlank(String value) {
        return super.is_If(isNotEmpty(value), value);
    }
}