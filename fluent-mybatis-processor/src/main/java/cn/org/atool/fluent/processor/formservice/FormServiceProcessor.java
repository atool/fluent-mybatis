package cn.org.atool.fluent.processor.formservice;

import cn.org.atool.fluent.form.annotation.FormService;
import cn.org.atool.fluent.processor.BaseProcessor;
import cn.org.atool.fluent.processor.formservice.filer.FormServiceImplFiler;
import cn.org.atool.fluent.processor.formservice.scanner.FormServiceScanner;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * {@link cn.org.atool.fluent.form.annotation.FormService} 生成代码处理
 *
 * @author darui.wu
 */
@SuppressWarnings({"rawtypes"})
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("cn.org.atool.fluent.form.annotation.FormService")
public class FormServiceProcessor extends BaseProcessor {

    @Override
    protected void doFileProcessor(TypeElement element) throws IOException {
        FormService annotation = element.getAnnotation(FormService.class);
        if (annotation.proxy()) {
            return;
        }
        List<ExecutableElement> methods = new FormServiceScanner(env).scan(element).getAbstractMethods();

        JavaFile javaFile = new FormServiceImplFiler(element, methods).javaFile();
        javaFile.writeTo(filer);
    }

    @Override
    protected Class<? extends Annotation> annotation() {
        return FormService.class;
    }
}