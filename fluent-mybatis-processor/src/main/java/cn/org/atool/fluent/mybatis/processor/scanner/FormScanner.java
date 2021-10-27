package cn.org.atool.fluent.mybatis.processor.scanner;

import cn.org.atool.fluent.form.annotation.Entry;
import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.form.annotation.Form;
import cn.org.atool.fluent.mybatis.processor.FormObjectProcessor;
import cn.org.atool.fluent.mybatis.processor.filer.ClassNames2;
import cn.org.atool.fluent.mybatis.processor.form.FormFieldInfo;
import com.squareup.javapoet.ClassName;
import lombok.Getter;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementScanner8;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * FormScanner 对注解 {@link Form} 的Object进行解析
 *
 * @author darui.wu
 */
public class FormScanner extends ElementScanner8<Void, Void> {
    final Consumer<String> logger;

    @Getter
    private ClassName className;

    @Getter
    private final List<FormFieldInfo> metas = new ArrayList<>();

    public FormScanner(Consumer<String> logger) {
        super();
        this.logger = logger;
    }

    @Override
    public Void visitType(TypeElement entity, Void aVoid) {
        Form form = entity.getAnnotation(Form.class);
        if (form == null) {
            FormObjectProcessor.error("Error in: " + entity.getQualifiedName().toString());
        } else {
            ClassName className = ClassNames2.getClassName(entity.getQualifiedName().toString());
            this.className = ClassName.get(className.packageName(), className.simpleName() + "MetaKit");
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
        String entryName = entry == null ? "" : entry.value().trim();
        if (entryName.isEmpty()) {
            entryName = fieldName;
        }

        FormFieldInfo meta = new FormFieldInfo(entryName, fieldName);
        if (entry == null) {
            meta.setEntryType(EntryType.EQ, true);
        } else {
            meta.setEntryType(entry.type(), entry.ignoreNull());
        }
        metas.add(meta);
        return super.visitVariable(element, aVoid);
    }

    private String getFieldName(String methodName, int index) {
        return methodName.substring(index, index + 1).toLowerCase() + methodName.substring(index + 1);
    }
}