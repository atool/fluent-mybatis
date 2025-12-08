package cn.org.atool.fluent.processor.mybatis;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.processor.BaseProcessor;
import cn.org.atool.fluent.processor.mybatis.entity.FluentList;
import cn.org.atool.fluent.processor.mybatis.scanner.FluentScanner2;
import com.google.auto.service.AutoService;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;

import static javax.tools.Diagnostic.Kind.NOTE;

/**
 * 生成代码处理
 *
 * @author darui.wu
 */
@SuppressWarnings("unused")
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("cn.org.atool.fluent.mybatis.annotation.FluentMybatis")
public class FluentMybatisProcessor extends BaseProcessor {
    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        this.detectSpringBootVersion();
    }

    @Override
    protected void doFileProcessor(TypeElement element) {
        FluentScanner2 scanner = new FluentScanner2(super.messager);
        scanner.scan(element);
        FluentList.addFluent(scanner.getFluent().sort());
    }

    @Override
    protected void doFileSummary() {
        FluentList.generate(filer, err -> messager.printMessage(NOTE, err));
    }

    @Override
    protected Class<? extends Annotation> annotation() {
        return FluentMybatis.class;
    }

    /**
     * use Jakarta Annotation
     */
    public static boolean useJakartaAnnotation = false;
    private boolean jakartaDetected = false;

    private void detectSpringBootVersion() {
        if (jakartaDetected)
            return;

        // 尝试查找 jakarta 类
        javax.lang.model.util.Elements elementUtils = processingEnv.getElementUtils();
        TypeElement jakartaServlet = elementUtils.getTypeElement("jakarta.servlet.Servlet");
        TypeElement jakartaPostConstruct = elementUtils.getTypeElement("jakarta.annotation.PostConstruct");
        // 尝试查找 javax 类
        TypeElement javaxServlet = elementUtils.getTypeElement("javax.servlet.Servlet");

        if (jakartaServlet != null || jakartaPostConstruct != null) {
            useJakartaAnnotation = true;
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.NOTE, "Detected Spring Boot 3.x (Jakarta EE)");
        } else if (javaxServlet != null) {
            useJakartaAnnotation = false;
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.NOTE, "Detected Spring Boot 2.x (Java EE)");
        }
        jakartaDetected = true;
    }
}