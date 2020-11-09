package cn.org.atool.fluent.mybatis.entity.field;

import cn.org.atool.fluent.mybatis.base.RichEntity;
import cn.org.atool.fluent.mybatis.entity.FluentEntity;
import cn.org.atool.generator.database.config.impl.RelationConfig;
import com.sun.tools.javac.code.Type;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.methodNameOfEntity;
import static cn.org.atool.generator.util.GeneratorHelper.isBlank;

/**
 * 加了@RefMethod注解的方法
 *
 * @author darui.wu
 */
@Getter
@ToString
@Accessors(chain = true)
public class EntityRefMethod extends FieldOrMethod<EntityRefMethod> {
    private String[] value;

    private Map<String, String> mapping = new HashMap<>();

    public EntityRefMethod(String property, Type javaType) {
        super(property, javaType);
    }

    public void setValue(String value) {
        List<String> list = new ArrayList<>();
        String[] pairs = value.split("&");
        for (String pair : pairs) {
            if (isBlank(pair)) {
                continue;
            }
            if (RelationConfig.isEquation(pair)) {
                String[] items = pair.split("=");
                this.mapping.put(items[0].trim(), items[1].trim());
            } else {
                mapping.clear();
                return;
            }
        }
        this.value = list.toArray(new String[0]);
    }

    /**
     * <pre>
     * @RefField对应的方法名称
     * </pre>
     *
     * @param fluent
     * @return
     */
    public String getRefMethod(FluentEntity fluent) {
        return methodNameOfEntity(this.name, fluent.getClassName());
    }

    public boolean isAutoMapping() {
        if (mapping.isEmpty()) {
            return false;
        }
        return true;
    }

    public String getReturnEntity() {
        List<Type> types = this.javaType.getTypeArguments();
        if (types.isEmpty()) {
            return this.javaType.baseType().tsym.name.toString();
        } else {
            return types.get(0).baseType().tsym.name.toString();
        }
    }

    public boolean returnList() {
        String type = this.javaType.baseType().tsym.name.toString();
        return "List".equals(type);
    }
}