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
public class MybatisProcessor extends BaseProcessor {

    public MybatisProcessor() {
    }

    @Override
    protected List<JavaFile> generateJavaFile(TypeElement curElement, EntityKlass entityKlass) {
        List<JavaFile> files = new ArrayList<>();
        files.add(new MapperGenerator(curElement, entityKlass).javaFile());
        files.add(new MappingGenerator(curElement, entityKlass).javaFile());
        files.add(new EntityHelperGenerator(curElement, entityKlass).javaFile());
        files.add(new WrapperHelperGenerator(curElement, entityKlass).javaFile());
        files.add(new QueryGenerator(curElement, entityKlass).javaFile());
        files.add(new UpdaterGenerator(curElement, entityKlass).javaFile());
        files.add(new BaseDaoGenerator(curElement, entityKlass).javaFile());
        return files;
    }

    @Override
    protected EntityKlass parseEntity(TypeElement entity) {
        try {
            return new EntityKlass()
                .setClassName(this.getCuPackageName(entity), entity.getSimpleName().toString())
                .setFluentMyBatis(entity.getAnnotation(FluentMyBatis.class), entity.getAnnotation(TableName.class), DaoInterfaceParser.getDaoInterfaces(entity))
                .setFields(this.translate(entity, (JCTree) trees.getTree(entity)));
        } catch (Throwable e) {
            messager.printMessage(Diagnostic.Kind.ERROR, entity.getQualifiedName() + "\n" + MybatisUtil.toString(e));
            throw e;
        }
    }
}