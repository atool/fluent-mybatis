package cn.org.atool.fluent.mybatis.entity.field;

import cn.org.atool.fluent.mybatis.entity.FluentEntity;
import com.sun.tools.javac.code.Type;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 加了@RefMethod注解的字段
 *
 * @author darui.wu
 */
@Getter
@ToString
@Accessors(chain = true)
public class EntityRefField extends BaseField<EntityRefField> {
    private String[] value;

    private Map<String, String> mapping = new HashMap<>();

    public EntityRefField(String property, Type javaType) {
        super(property, javaType);
    }

    public void setValue(String[] value) {
        this.value = value;
        for (String item : value) {
            if (!item.contains(":")) {
                mapping.clear();
                return;
            }
            String[] pair = item.split(":");
            this.mapping.put(pair[0].trim(), pair[1].trim());
        }
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
        return String.format("%sOf%s", this.property, fluent.getClassName());
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