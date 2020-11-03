package cn.org.atool.fluent.mybatis.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.entity.base.BaseProcessor;
import cn.org.atool.fluent.mybatis.entity.base.DaoInterfaceParser;
import cn.org.atool.fluent.mybatis.entity.generator.*;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.sun.tools.javac.tree.JCTree;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.ArrayList;
import java.util.List;

/**
 * 生成代码处理
 *
 * @author darui.wu
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class FluentMybatisProcessor extends BaseProcessor {

    public FluentMybatisProcessor() {
    }

    @Override
    protected List<JavaFile> generateJavaFile(TypeElement curElement, FluentEntityInfo fluent) {
        List<JavaFile> files = new ArrayList<>();
        files.add(new MapperGenerator(curElement, fluent).javaFile());
        files.add(new MappingGenerator(curElement, fluent).javaFile());
        files.add(new EntityHelperGenerator(curElement, fluent).javaFile());
        files.add(new SqlProviderGenerator(curElement, fluent).javaFile());
        files.add(new WrapperHelperGenerator(curElement, fluent).javaFile());
        files.add(new QueryGenerator(curElement, fluent).javaFile());
        files.add(new UpdaterGenerator(curElement, fluent).javaFile());
        files.add(new BaseDaoGenerator(curElement, fluent).javaFile());
        files.add(new WrapperDefaultGenerator(curElement, fluent).javaFile());
        return files;
    }

    @Override
    protected FluentEntityInfo parseEntity(TypeElement entity) {
        FluentEntityInfo entityInfo = null;
        try {
            entityInfo = new FluentEntityInfo();
            entityInfo.setClassName(this.getCuPackageName(entity), entity.getSimpleName().toString());
            String defaults = DaoInterfaceParser.getDefaults(entity);
            entityInfo.setFluentMyBatis(entity.getAnnotation(FluentMybatis.class), defaults);
            entityInfo.setFields(this.translate(entity, (JCTree) trees.getTree(entity)));
            return entityInfo;
        } catch (Throwable e) {
            messager.printMessage(Diagnostic.Kind.ERROR, entityInfo + "\n" + MybatisUtil.toString(e));
            throw e;
        }
    }
}