package cn.org.atool.fluent.form.processor;

import cn.org.atool.fluent.form.annotation.Entry;
import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.form.annotation.Form;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import lombok.Getter;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementScanner8;
import java.util.ArrayList;
import java.util.List;

/**
 * FormScanner 对注解 {@link Form} 的Object进行解析
 *
 * @author darui.wu
 */
public class FormScanner extends ElementScanner8<Void, Void> {
    @Getter
    private ClassName className;

    @Getter
    private final List<FormField> metas = new ArrayList<>();


    @Override
    public Void visitType(TypeElement entity, Void aVoid) {
        Form form = entity.getAnnotation(Form.class);
        if (form == null) {
            MetaKitProcessor.error("Error in: " + entity.getQualifiedName().toString());
        } else {
            String fullName = entity.getQualifiedName().toString();
            String simpleName = entity.getSimpleName().toString();
            String packName = fullName.substring(0, fullName.length() - simpleName.length() - 1);
            this.className = ClassName.get(packName, simpleName);
        }
        return super.visitType(entity, aVoid);
    }

    @Override
    public Void visitVariable(VariableElement element, Void aVoid) {
        if (element.getKind() != ElementKind.FIELD ||
            element.getModifiers().contains(Modifier.STATIC) ||
            element.getModifiers().contains(Modifier.TRANSIENT)) {
            return super.visitVariable(element, aVoid);
        }
        Entry entry = element.getAnnotation(Entry.class);
        String fieldName = element.getSimpleName().toString();
        String entryName = entry == null ? "" : entry.name().trim();
        if (entryName.isEmpty()) {
            entryName = fieldName;
        }
        String fieldType = getJavaType(element).toString();
        FormField meta = new FormField(entryName, fieldName, fieldType);
        if (entry == null) {
            meta.setEntryType(EntryType.EQ, true);
        } else {
            meta.setEntryType(entry.type(), entry.ignoreNull());
        }
        metas.add(meta);
        return super.visitVariable(element, aVoid);
    }

    private TypeName getJavaType(VariableElement var) {
        TypeName type = ClassName.get(var.asType());
        return type instanceof ParameterizedTypeName ? ((ParameterizedTypeName) type).rawType : type;
    }
}