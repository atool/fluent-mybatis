package cn.org.atool.fluent.processor.form;

import cn.org.atool.fluent.processor.BaseProcessor;
import cn.org.atool.fluent.form.annotation.Form;
import com.google.auto.service.AutoService;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;

/**
 * 对FormObject对象进行增强
 *
 * @author darui.wu
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("cn.org.atool.fluent.form.annotation.Form")
public class MetaKitProcessor extends BaseProcessor {

    @Override
    protected void doFileProcessor(TypeElement element) throws Exception {
        MetaProcessorKit.generate(element, filer, messager);
    }

    @Override
    protected Class<? extends Annotation> annotation() {
        return Form.class;
    }
}