package cn.org.atool.fluent.mybatis.processor;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.processor.base.IProcessor;
import cn.org.atool.fluent.mybatis.processor.entity.FluentList;
import cn.org.atool.fluent.mybatis.processor.scanner.FluentScanner;
import com.google.auto.service.AutoService;
import lombok.Getter;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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

    @Getter
    protected Messager messager;

    private boolean generated = false;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        this.filer = env.getFiler();
        this.messager = env.getMessager();
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
                messager.printMessage(ERROR, element.asType().toString() + ":\n" + toString(e));
                throw new RuntimeException(e);
            }
        }
        messager.printMessage(NOTE, "@FluentMybatis file generating...");
        FluentList.generate(filer, err -> messager.printMessage(NOTE, err));
        messager.printMessage(NOTE, "FluentMybatis process end !!!");
        this.generated = true;
        return true;
    }

    /**
     * 将异常日志转换为字符串
     *
     * @param e
     * @return
     */
    public static String toString(Throwable e) {
        try (StringWriter writer = new StringWriter(); PrintWriter print = new PrintWriter(writer)) {
            e.printStackTrace(print);
            return writer.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
}