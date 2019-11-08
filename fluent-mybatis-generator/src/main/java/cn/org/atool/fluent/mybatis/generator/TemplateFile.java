package cn.org.atool.fluent.mybatis.generator;

import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

@Accessors(chain = true)
public class TemplateFile {
    public static final List<TemplateFile> TEMPLATE_FILE_LIST = new ArrayList<TemplateFile>() {
        {
            this.add(new TemplateFile("table_Mapping.java.vm", "mapping/*MP.java"));
            this.add(new TemplateFile("entityHelper.java.vm", "helper/*EntityHelper.java"));
            this.add(new TemplateFile("query.java.vm", "query/*EntityQuery.java"));
            this.add(new TemplateFile("update.java.vm", "query/*EntityUpdate.java"));
            this.add(new TemplateFile("entity_wrapper_helper.java.vm", "query/*EntityWrapperHelper.java"));

            this.add(new TemplateFile("dao.base.java.vm", "dao/base/*BaseDao.java")
                    .setBaseDao(true)
            );
            this.add(new TemplateFile("mapper_partition.java.vm", "mapper/*PartitionMapper.java")
                    .setPartition(true)
            );
            //test
            this.add(new TemplateFile("dao.java.vm", "dao/intf/*Dao.java", TemplateType.Dao));
            this.add(new TemplateFile("dao.impl.java.vm", "dao/impl/*DaoImpl.java", TemplateType.Dao));
            this.add(new TemplateFile("table_tableMap.java.vm", "datamap/table/*TableMap.java", TemplateType.Test));
            this.add(new TemplateFile("table_entityMap.java.vm", "datamap/entity/*EntityMap.java", TemplateType.Test));
            this.add(new TemplateFile("table_mix.java.vm", "mix/*TableMix.java", TemplateType.Test));
        }
    };

    private String template;

    private String fileNameReg;

    private TemplateType templateType = TemplateType.Base;
    /**
     * 是否分库
     */
    @Setter
    private boolean isPartition = false;
    /**
     * 是否base dao
     */
    @Setter
    private boolean isBaseDao = false;
    /**
     * 文件子路径
     */
    private String fileSubPath;
    /**
     * 文件后缀
     */
    private String fileSuffix;
    /**
     * 文件名称(不包含路径和后缀)
     */
    private String fileName;
    /**
     * 模板标识
     */
    private String templateId;

    public TemplateFile(String template, String fileNameReg) {
        this.template = template;
        this.fileNameReg = fileNameReg;
    }

    public TemplateFile(String template, String fileNameReg, TemplateType templateType) {
        this.template = template;
        this.fileNameReg = fileNameReg;
        this.templateType = templateType;
    }

    public static List<FileOutConfig> parseConfigList(MybatisGenerator generator, Map<String, Object> config) {

        return TEMPLATE_FILE_LIST.stream()
                .filter(template -> !template.isPartition || generator.currTable().isPartition())
                .map(template -> template.parse(generator, config))
                .collect(Collectors.toList());
    }

    private FileOutConfig parse(MybatisGenerator generator, Map<String, Object> config) {
        return new FileOutConfig("/templates/" + template) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                parse(generator.currTable().getWithoutSuffixEntity());
                config.put("withoutSuffixEntity", generator.currTable().getWithoutSuffixEntity());
                config.put("withSuffixEntity", generator.currTable().getWithSuffixEntity());
                String _package = getFilePack(generator.getPackage(templateType));

                config.put("pack" + templateId, _package);
                config.put("file" + templateId, fileName);
                setBaseDaoImports(generator, config);
                return getFullFileName(generator);
            }
        };
    }

    private void setBaseDaoImports(MybatisGenerator generator, Map<String, Object> config) {
        if (!this.isBaseDao || generator.getBaseDaoInterfaces().size() == 0) {
            return;
        }
        String imports = generator.getBaseDaoInterfaces().values().stream().map(item -> "import " + item + ";").collect(joining("\n"));
        config.put("baseDaoInterfaceImports", imports);

        String implement = generator.getBaseDaoInterfaces().keySet().stream().map(item -> {
            item = item.replaceAll("\\$\\{entity\\}", (String) config.get("withSuffixEntity"));
            item = item.replaceAll("\\$\\{update\\}", (String) config.get("fileEntityUpdate"));
            item = item.replaceAll("\\$\\{query\\}", (String) config.get("fileEntityQuery"));
            return item;
        }).collect(joining(", "));
        config.put("baseDaoInterfaceImplement", implement);
    }

    private void parse(String entityName) {
        int start = this.fileNameReg.lastIndexOf('/');
        start = start < 0 ? 0 : start;
        int end = this.fileNameReg.indexOf('.');
        end = end < 0 ? this.fileNameReg.length() : end;

        this.fileSubPath = this.fileNameReg.substring(0, start);
        String fileRegName = this.fileNameReg.substring(start + 1, end);
        this.fileSuffix = this.fileNameReg.substring(end + 1);

        this.fileName = fileRegName.replace("*", entityName);
        this.templateId = fileRegName.replace("*", "");
    }

    private String getFilePack(String basePackage) {
        return basePackage + "." + fileSubPath.replace('/', '.');
    }

    private String getFullFileName(MybatisGenerator generator) {
        String output = generator.getOutputDir();
        if (this.templateType == TemplateType.Test) {
            output = generator.getTestOutputDir();
        } else if (this.templateType == TemplateType.Dao) {
            output = generator.getDaoOutputDir();
        }
        String parentPath = generator.getPackage(templateType).replaceAll("\\.", "/");
        return String.format("%s/%s/%s/%s.%s", output, parentPath, fileSubPath, fileName, fileSuffix);
    }

    public enum TemplateType {
        Base,
        Test,
        Dao;
    }
}
