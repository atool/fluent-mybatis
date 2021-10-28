package cn.org.atool.fluent.form.processor;

import cn.org.atool.fluent.form.annotation.Form;
import com.google.auto.service.AutoService;

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
 * 对FormObject对象进行增强
 *
 * @author darui.wu
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("cn.org.atool.fluent.form.annotation.Form")
public class MetaKitProcessor extends AbstractProcessor {
    protected Filer filer;

    private static Messager messager;

    private boolean generated = false;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        this.filer = env.getFiler();
        MetaKitProcessor.messager = env.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver() || generated) {
            return true;
        }
        try {
            /* 判断classpath路径是否引用了javapoet包 */
            Class.forName("com.squareup.javapoet.JavaFile");
        } catch (ClassNotFoundException e) {
            generated = true;
            return true;
        }
        messager.printMessage(NOTE, "FormObject process begin !!!");
        for (Element element : roundEnv.getRootElements()) {
            Form form = element.getAnnotation(Form.class);
            if (form == null) {
                continue;
            }
            messager.printMessage(NOTE, "@FormObject file analysis...");
            try {
                MetaProcessorKit.generate(element, filer);
            } catch (Exception e) {
                messager.printMessage(ERROR, element.asType().toString() + ":\n" + toString(e));
                throw new RuntimeException(e);
            }
        }
        messager.printMessage(NOTE, "FormObject process end !!!");
        this.generated = true;
        return false;
    }


    public static void error(String message) {
        messager.printMessage(ERROR, message);
    }

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