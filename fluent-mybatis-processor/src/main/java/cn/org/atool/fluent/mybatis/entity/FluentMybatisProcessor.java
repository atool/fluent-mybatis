package cn.org.atool.fluent.mybatis.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMyBatis;
import cn.org.atool.fluent.mybatis.annotation.TableName;
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
    protected List<JavaFile> generateJavaFile(TypeElement curElement, FluentEntityInfo fluentEntityInfo) {
        List<JavaFile> files = new ArrayList<>();
        files.add(new MapperGenerator(curElement, fluentEntityInfo).javaFile());
        files.add(new MappingGenerator(curElement, fluentEntityInfo).javaFile());
        files.add(new EntityHelperGenerator(curElement, fluentEntityInfo).javaFile());
        files.add(new WrapperHelperGenerator(curElement, fluentEntityInfo).javaFile());
        files.add(new QueryGenerator(curElement, fluentEntityInfo).javaFile());
        files.add(new UpdaterGenerator(curElement, fluentEntityInfo).javaFile());
        files.add(new BaseDaoGenerator(curElement, fluentEntityInfo).javaFile());
        return files;
    }

    @Override
    protected FluentEntityInfo parseEntity(TypeElement entity) {
        try {
            return new FluentEntityInfo()
                .setClassName(this.getCuPackageName(entity), entity.getSimpleName().toString())
                .setFluentMyBatis(entity.getAnnotation(FluentMyBatis.class), entity.getAnnotation(TableName.class), DaoInterfaceParser.getDaoInterfaces(entity))
                .setFields(this.translate(entity, (JCTree) trees.getTree(entity)));
        } catch (Throwable e) {
            messager.printMessage(Diagnostic.Kind.ERROR, entity.getQualifiedName() + "\n" + MybatisUtil.toString(e));
            throw e;
        }
    }
}