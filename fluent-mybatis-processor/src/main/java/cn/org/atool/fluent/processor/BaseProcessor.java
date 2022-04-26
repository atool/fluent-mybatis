package cn.org.atool.fluent.processor;

import cn.org.atool.fluent.mybatis.processor.base.IProcessor;
import cn.org.atool.generator.util.GeneratorHelper;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.Set;

import static javax.tools.Diagnostic.Kind.ERROR;
import static javax.tools.Diagnostic.Kind.NOTE;

public abstract class BaseProcessor extends AbstractProcessor implements IProcessor {
    protected ProcessingEnvironment env;

    protected Filer filer;

    protected Messager messager;

    private boolean generated = false;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        this.env = env;
        this.filer = env.getFiler();
        this.messager = env.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver() || generated) {
            return true;
        }
        messager.printMessage(NOTE, this.annotation().getSimpleName() + " process begin !!!");
        for (Element element : roundEnv.getRootElements()) {
            Annotation annotation = element.getAnnotation(this.annotation());
            if (annotation == null) {
                continue;
            }
            messager.printMessage(NOTE, this.annotation().getSimpleName() + " file analysis...");
            try {
                if (element instanceof TypeElement) {
                    this.doFileProcessor((TypeElement) element);
                }
            } catch (Exception e) {
                messager.printMessage(ERROR, element.asType().toString() + ":\n" + GeneratorHelper.toString(e));
                throw new RuntimeException(e);
            }
        }
        this.doFileSummary();
        messager.printMessage(NOTE, this.annotation().getSimpleName() + " process end !!!");
        this.generated = true;
        return true;
    }

    /**
     * 单个文件处理
     */
    protected abstract void doFileProcessor(TypeElement element) throws Exception;

    /**
     * 汇总处理
     */
    protected void doFileSummary() {
    }

    protected abstract Class<? extends Annotation> annotation();

    @Override
    public Messager getMessager() {
        return messager;
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

    /**
     * 编译文件
     */
    @SuppressWarnings({"unused", "unchecked"})
    private void compile(String path) throws IOException {
        //拿到编译器
        JavaCompiler complier = ToolProvider.getSystemJavaCompiler();
        //文件管理者
        StandardJavaFileManager fileMgr = complier.getStandardFileManager(null, null, null);
        //获取文件
        Iterable units = fileMgr.getJavaFileObjects(path);
        //编译任务
        JavaCompiler.CompilationTask t = complier.getTask(null, fileMgr, null, null, null, units);
        //进行编译
        t.call();
        fileMgr.close();
    }

    public void error(String message) {
        messager.printMessage(ERROR, message);
    }
}