package cn.org.atool.fluent.mybatis.processor;

import cn.org.atool.fluent.BaseProcessor;
import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.processor.entity.FluentList;
import cn.org.atool.fluent.mybatis.processor.scanner.FluentScanner;
import com.google.auto.service.AutoService;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;

import static javax.tools.Diagnostic.Kind.NOTE;

/**
 * 生成代码处理
 *
 * @author darui.wu
 */
@SuppressWarnings({"rawtypes"})
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("cn.org.atool.fluent.mybatis.annotation.FluentMybatis")
public class FluentMybatisProcessor extends BaseProcessor {

    @Override
    protected void doFileProcessor(TypeElement element) {
        FluentScanner scanner = new FluentScanner(() -> messager);
        scanner.scan(element);
        FluentList.addFluent(scanner.getFluent());
    }

    @Override
    protected void doFileSummary() {
        FluentList.generate(filer, err -> messager.printMessage(NOTE, err));
    }

    @Override
    protected Class<? extends Annotation> annotation() {
        return FluentMybatis.class;
    }
}