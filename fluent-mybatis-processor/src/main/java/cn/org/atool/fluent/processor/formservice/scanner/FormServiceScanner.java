package cn.org.atool.fluent.processor.formservice.scanner;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.processor.AScanner;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeName;
import lombok.Getter;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import java.util.*;

import static cn.org.atool.fluent.mybatis.utility.StrConstant.DOT_STR;
import static cn.org.atool.fluent.mybatis.utility.StrConstant.EMPTY;

/**
 * FormServiceScanner
 *
 * @author wudarui
 */
public class FormServiceScanner extends AScanner {
    private final Queue<TypeElement> interfaces = new LinkedList<>();
    private final Set<String> hasParsed = new HashSet<>();

    @Getter
    private final List<ExecutableElement> abstractMethods = new ArrayList<>();

    /**
     * 构造函数
     *
     * @param env ProcessingEnvironment
     */
    public FormServiceScanner(ProcessingEnvironment env) {
        super(env.getMessager());
    }

    @Override
    public FormServiceScanner scan(TypeElement element) {
        while (!Objects.equals(element.toString(), Object.class.getName())) {
            this.parseAbstractMethod(element);

            TypeMirror typeMirror = element.getSuperclass();
            if (typeMirror instanceof NoType) {
                break;
            }
            element = this.asTypeElement(typeMirror);
        }
        while (!interfaces.isEmpty()) {
            TypeElement type = interfaces.poll();
            parseAbstractMethod(type);
        }
        return this;
    }

    private void parseAbstractMethod(TypeElement element) {
        for (Element item : element.getEnclosedElements()) {
            if (!(item instanceof ExecutableElement)) {
                continue;
            }
            ExecutableElement executable = (ExecutableElement) item;
            Set<Modifier> modifiers = executable.getModifiers();
            String signature = signature(executable);
            if (!hasParsed.contains(signature) && modifiers.contains(Modifier.ABSTRACT)) {
                abstractMethods.add(executable);
            }
            // if (signature.contains("updateEntityByIds")) {
            // messager.printMessage(Diagnostic.Kind.ERROR, signature);
            // }
            hasParsed.add(signature);
        }
        for (TypeMirror typeMirror : element.getInterfaces()) {
            interfaces.offer(this.asTypeElement(typeMirror));
        }
    }

    private String signature(ExecutableElement element) {
        List<String> paras = new ArrayList<>();
        for (VariableElement para : element.getParameters()) {
            paras.add(className(para.asType()));
        }
        return element.getSimpleName() + "(" + String.join(";", paras) + ")";
    }

    private String className(TypeMirror type) {
        TypeName typeName = ClassName.get(type);
        return className(typeName);
    }

    /**
     * 获取TypeName对应的类名
     *
     * @param type TypeName
     * @return class name
     */
    public static String className(TypeName type) {
        if (type instanceof ClassName) {
            String pack = ((ClassName) type).packageName();
            return pack + (If.isBlank(pack) ? EMPTY : DOT_STR) + ((ClassName) type).simpleName();
        } else if (type instanceof ParameterizedTypeName) {
            ParameterizedTypeName pType = (ParameterizedTypeName) type;
            return className(pType.rawType());
        }
        return type.toString();
    }
}