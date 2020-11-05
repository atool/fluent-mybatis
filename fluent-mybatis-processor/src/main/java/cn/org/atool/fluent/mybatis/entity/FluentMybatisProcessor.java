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
    protected List<JavaFile> generateJavaFile(FluentEntity fluent) {
        List<JavaFile> files = new ArrayList<>();
        files.add(new MapperGenerator(fluent).javaFile());
        files.add(new MappingGenerator(fluent).javaFile());
        files.add(new EntityHelperGenerator(fluent).javaFile());
        files.add(new SqlProviderGenerator(fluent).javaFile());
        files.add(new WrapperHelperGenerator(fluent).javaFile());
        files.add(new QueryGenerator(fluent).javaFile());
        files.add(new UpdaterGenerator(fluent).javaFile());
        files.add(new BaseDaoGenerator(fluent).javaFile());
        files.add(new WrapperDefaultGenerator(fluent).javaFile());
        return files;
    }

    @Override
    protected FluentEntity parseEntity(TypeElement entity) {
        FluentEntity entityInfo = null;
        try {
            entityInfo = new FluentEntity();
            entityInfo.setClassName(this.getCuPackageName(entity), entity.getSimpleName().toString());
            String defaults = DaoInterfaceParser.getDefaults(entity);
            entityInfo.setFluentMyBatis(entity.getAnnotation(FluentMybatis.class), defaults);
            entityInfo.setFields(this.translate(entity, (JCTree) trees.getTree(entity)), this);
            return entityInfo;
        } catch (Throwable e) {
            messager.printMessage(Diagnostic.Kind.ERROR, entityInfo + "\n" + MybatisUtil.toString(e));
            throw e;
        }
    }
}