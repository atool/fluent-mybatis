package cn.org.atool.fluent.mybatis.entity.base;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.entity.FluentEntityInfo;
import cn.org.atool.fluent.mybatis.entity.generator.QueryGenerator;
import cn.org.atool.fluent.mybatis.entity.generator.UpdaterGenerator;
import com.squareup.javapoet.ClassName;
import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Symbol;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.org.atool.fluent.mybatis.annotation.ParaType.*;

/**
 * 解析@FluentMyBatis上定义的 daoInterface属性值
 * 在 Processor中直接获取嵌套的注解, 会报 MirroredTypeException 异常
 *
 * @author darui.wu
 */
public class DaoInterfaceParser {
    private static final String STR_DAO = "daoInterface()";

    private static final String STR_VALUE = "value";

    private static final String STR_ARGS = "args";

    /**
     * 获取@FluentMyBatis上定义的 daoInterface属性值
     *
     * @param entity
     * @return key: @DaoInterface value值, value: @DaoInterface args值
     */
    public static Map<String, List<String>> getDaoInterfaces(TypeElement entity) {
        Map<String, List<String>> daoInterfaces = new HashMap<>();
        AnnotationMirror mirror = getFluentMyBatisMirror(entity);
        if (mirror == null) {
            return daoInterfaces;
        }
        Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = mirror.getElementValues();
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : elementValues.entrySet()) {
            ExecutableElement method = entry.getKey();
            AnnotationValue value = entry.getValue();
            if (!method.toString().contains(STR_DAO)) {
                continue;
            }
            List<Attribute.Compound> nested = (List<Attribute.Compound>) value.getValue();
            for (Attribute.Compound nest : nested) {
                Map<Symbol.MethodSymbol, Attribute> attributes = nest.getElementValues();
                String daoInterface = null;
                List<String> paramTypes = new ArrayList<>();
                for (Map.Entry<Symbol.MethodSymbol, Attribute> attribute : attributes.entrySet()) {
                    String name = attribute.getKey().name.toString();
                    if (name.equalsIgnoreCase(STR_VALUE)) {
                        daoInterface = attribute.getValue().getValue().toString();
                    }
                    if (name.equalsIgnoreCase(STR_ARGS)) {
                        List<Attribute.Enum> args = (List<Attribute.Enum>) attribute.getValue().getValue();
                        for (Attribute.Enum arg : args) {
                            paramTypes.add(arg.value.name.toString());
                        }
                    }
                }
                if (daoInterface != null) {
                    daoInterfaces.put(daoInterface, paramTypes);
                }
            }
        }
        return daoInterfaces;
    }

    private static AnnotationMirror getFluentMyBatisMirror(TypeElement entity) {
        List<? extends AnnotationMirror> annotations = entity.getAnnotationMirrors();
        for (AnnotationMirror annotation : annotations) {
            if (annotation.getAnnotationType().toString().contains(FluentMybatis.class.getSimpleName())) {
                return annotation;
            }
        }
        return null;
    }


    /**
     * 获取泛型对应的ClassName
     *
     * @param fluentEntityInfo
     * @param types
     * @return
     */
    public static List<ClassName> getClassNames(FluentEntityInfo fluentEntityInfo, List<String> types) {
        return types.stream()
            .map(type -> {
                if (type.endsWith(Entity.name())) {
                    return fluentEntityInfo.className();
                } else if (type.endsWith(Query.name())) {
                    return QueryGenerator.className(fluentEntityInfo);
                } else if (type.endsWith(Updater.name())) {
                    return UpdaterGenerator.className(fluentEntityInfo);
                } else {
                    return ClassName.get("", type);
                }
            })
            .collect(Collectors.toList());
    }
}