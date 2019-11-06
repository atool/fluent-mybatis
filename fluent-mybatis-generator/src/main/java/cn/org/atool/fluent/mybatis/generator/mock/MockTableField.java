package cn.org.atool.fluent.mybatis.generator.mock;

import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;

import static cn.org.atool.fluent.mybatis.generator.MybatisGenerator.currTable;

public class MockTableField extends MockUp<TableField> {
    @Mock
    public TableField setColumnType(Invocation it, final IColumnType columnType) {
        String name = ((TableField) it.getInvokedInstance()).getName();
        IColumnType specType = currTable().columnType(name);
        return it.proceed(specType == null ? columnType : specType);
    }

    @Mock
    public String getComment(Invocation it) {
        String comment = it.proceed();
        return comment == null ? null : comment.replaceAll("\\/\\\\", "");
    }

    @Mock
    public String getCapitalName(Invocation it) {
        String propertyName = ((TableField) it.getInvokedInstance()).getPropertyName();
        if (propertyName.length() <= 1) {
            return propertyName.toUpperCase();
        }
        // 第一个字母小写， 第二个字母大写，特殊处理
        String firstChar = propertyName.substring(0, 1);
        if (Character.isLowerCase(firstChar.toCharArray()[0]) && Character.isUpperCase(propertyName.substring(1, 2).toCharArray()[0])) {
            return firstChar.toLowerCase() + propertyName.substring(1);
        } else {
            return firstChar.toUpperCase() + propertyName.substring(1);
        }
    }
}
