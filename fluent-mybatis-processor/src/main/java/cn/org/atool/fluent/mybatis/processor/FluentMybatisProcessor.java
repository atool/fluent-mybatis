package cn.org.atool.fluent.mybatis.processor;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.processor.base.IProcessor;
import cn.org.atool.fluent.mybatis.processor.entity.FluentList;
import cn.org.atool.fluent.mybatis.processor.scanner.FluentScanner;
import cn.org.atool.generator.util.GeneratorHelper;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

import static javax.tools.Diagnostic.Kind.ERROR;
import static javax.tools.Diagnostic.Kind.NOTE;

/**
 * 生成代码处理
 *
 * @author darui.wu
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("cn.org.atool.fluent.mybatis.annotation.FluentMybatis")
public class FluentMybatisProcessor extends AbstractProcessor implements IProcessor {
    protected Filer filer;

    private static Messager messager;

    private boolean generated = false;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        this.filer = env.getFiler();
        FluentMybatisProcessor.messager = env.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver() || generated) {
            return true;
        }
        messager.printMessage(NOTE, "FluentMybatis process begin !!!");
        for (Element element : roundEnv.getRootElements()) {
            FluentMybatis fluent = element.getAnnotation(FluentMybatis.class);
            if (fluent == null) {
                continue;
            }
            messager.printMessage(NOTE, "@FluentMybatis file analysis...");
            try {
                FluentScanner scanner = new FluentScanner(err -> messager.printMessage(NOTE, err));
                scanner.scan(element);
                FluentList.addFluent(scanner.getFluent());
            } catch (Exception e) {
                messager.printMessage(ERROR, element.asType().toString() + ":\n" + GeneratorHelper.toString(e));
                throw new RuntimeException(e);
            }
        }
        messager.printMessage(NOTE, "@FluentMybatis file generating...");
        FluentList.generate(filer, err -> messager.printMessage(NOTE, err));
        messager.printMessage(NOTE, "FluentMybatis process end !!!");
        this.generated = true;
        return true;
    }

    public static void error(String message) {
        messager.printMessage(ERROR, message);
    }

    @Override
    public Messager getMessager() {
        return messager;
    }
}