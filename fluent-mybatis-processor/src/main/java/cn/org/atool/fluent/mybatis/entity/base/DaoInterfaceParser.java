package cn.org.atool.fluent.mybatis.entity.base;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import com.sun.tools.javac.code.Attribute;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 解析@FluentMyBatis上定义的 daoInterface属性值
 * 在 Processor中直接获取嵌套的注解, 会报 MirroredTypeException 异常
 *
 * @author darui.wu
 */
public class DaoInterfaceParser {
    private static final String STR_DAO = "daoInterface()";

    /**
     * 获取@FluentMyBatis上定义的 daoInterface属性值
     *
     * @param entity
     * @return key: @DaoInterface value值, value: @DaoInterface args值
     */
    public static List<String> getDaoInterfaces(TypeElement entity) {
        List<String> daoInterfaces = new ArrayList<>();
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
            List<Attribute.Class> list = (List<Attribute.Class>) value.getValue();
            for (Attribute.Class clazz : list) {
                daoInterfaces.add(clazz.getValue().toString());
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
}