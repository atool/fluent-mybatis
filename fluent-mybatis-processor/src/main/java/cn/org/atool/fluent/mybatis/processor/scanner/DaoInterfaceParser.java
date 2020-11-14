package cn.org.atool.fluent.mybatis.processor.scanner;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.base.crud.IDefaultSetter;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.util.List;
import java.util.Map;

/**
 * 解析@FluentMyBatis上定义的 daoInterface属性值
 * 在 Processor中直接获取嵌套的注解, 会报 MirroredTypeException 异常
 *
 * @author darui.wu
 */
public class DaoInterfaceParser {
    private static final String ATTR_DEFAULTS = "defaults()";

    /**
     * 获取@FluentMyBatis上定义的 iDao() 属性值
     *
     * @param entity
     * @return key: @DaoInterface value值, value: @DaoInterface args值
     */
    public static String getDefaults(TypeElement entity) {
        AnnotationMirror mirror = getFluentMyBatisMirror(entity);
        if (mirror == null) {
            return IDefaultSetter.class.getName();
        }

        Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = mirror.getElementValues();
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : elementValues.entrySet()) {
            ExecutableElement method = entry.getKey();
            AnnotationValue value = entry.getValue();
            if (!method.toString().contains(ATTR_DEFAULTS)) {
                continue;
            }
            DeclaredType aClass = (DeclaredType) value.getValue();
            return aClass.toString();
        }
        return IDefaultSetter.class.getName();
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