package cn.org.atool.fluent.mybatis.processor;

import cn.org.atool.fluent.form.annotation.Form;
import cn.org.atool.fluent.mybatis.processor.base.IProcessor;
import cn.org.atool.fluent.mybatis.processor.form.FormMetaFiler;
import cn.org.atool.fluent.mybatis.processor.scanner.FormScanner;
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
 * 对FormObject对象进行增强
 *
 * @author darui.wu
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("cn.org.atool.fluent.form.annotation.Form")
public class FormObjectProcessor extends AbstractProcessor implements IProcessor {
    protected Filer filer;

    private static Messager messager;

    private boolean generated = false;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        this.filer = env.getFiler();
        FormObjectProcessor.messager = env.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver() || generated) {
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
                FormScanner scanner = new FormScanner(err -> messager.printMessage(NOTE, err));
                scanner.scan(element);
                new FormMetaFiler(scanner.getClassName(), scanner.getMetas()).javaFile().writeTo(filer);
            } catch (Exception e) {
                messager.printMessage(ERROR, element.asType().toString() + ":\n" + GeneratorHelper.toString(e));
                throw new RuntimeException(e);
            }
        }
        messager.printMessage(NOTE, "FormObject process end !!!");
        this.generated = true;
        return false;
    }

    @Override
    public Messager getMessager() {
        return messager;
    }

    public static void error(String message) {
        messager.printMessage(ERROR, message);
    }
}