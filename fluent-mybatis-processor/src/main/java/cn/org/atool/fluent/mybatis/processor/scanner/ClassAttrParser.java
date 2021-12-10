package cn.org.atool.fluent.mybatis.processor.scanner;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 解析@FluentMyBatis上定义的 daoInterface属性值
 * 在 Processor中直接获取嵌套的注解, 会报 MirroredTypeException 异常
 *
 * @author darui.wu
 */
@SuppressWarnings({"rawtypes"})
public class ClassAttrParser {
    public static final String ATTR_DEFAULTS = "defaults()";

    public static final String ATTR_SUPER_MAPPER = "superMapper()";

    /**
     * 获取@FluentMyBatis上定义的 iDao() 属性值
     *
     * @param entity TypeElement of entity
     * @return key: @DaoInterface value值, value: @DaoInterface args值
     */
    public static String getClassAttr(TypeElement entity, String methodName, Class defaultValue) {
        AnnotationMirror mirror = getFluentMyBatisMirror(entity);
        if (mirror == null) {
            return defaultValue.getName();
        }

        Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = mirror.getElementValues();
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : elementValues.entrySet()) {
            ExecutableElement method = entry.getKey();
            AnnotationValue value = entry.getValue();
            if (!method.toString().contains(methodName)) {
                continue;
            }
            String aClass = value.getValue().toString();
            return Objects.equals(aClass, Object.class.getName()) ? defaultValue.getName() : aClass;
        }
        return defaultValue.getName();
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
}