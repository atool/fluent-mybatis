package cn.org.atool.fluent.mybatis.generator;


import lombok.extern.slf4j.Slf4j;
import org.test4j.generator.mybatis.BaseGenerator;
import org.test4j.generator.mybatis.Generator;
import org.test4j.generator.mybatis.MyBatisGenerator;
import org.test4j.generator.mybatis.template.BaseTemplate;
import org.test4j.generator.mybatis.template.TemplateList;
import org.test4j.generator.mybatis.template.dao.BaseDaoTemplate;
import org.test4j.generator.mybatis.template.dao.DaoImplTemplate;
import org.test4j.generator.mybatis.template.dao.DaoIntfTemplate;
import org.test4j.generator.mybatis.template.datamap.EntityMapTemplate;
import org.test4j.generator.mybatis.template.datamap.TableMapTemplate;
import org.test4j.generator.mybatis.template.entity.EntityHelperTemplate;
import org.test4j.generator.mybatis.template.entity.EntityTemplate;
import org.test4j.generator.mybatis.template.mapper.MapperTemplate;
import org.test4j.generator.mybatis.template.mapper.PartitionMapperTemplate;
import org.test4j.generator.mybatis.template.mapping.MappingTemplate;
import org.test4j.generator.mybatis.template.mix.TableMixTemplate;
import org.test4j.generator.mybatis.template.query.EntityQueryTemplate;
import org.test4j.generator.mybatis.template.query.EntityUpdateTemplate;
import org.test4j.generator.mybatis.template.query.EntityWrapperHelperTemplate;
import org.test4j.generator.mybatis.template.summary.SummaryTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库表列表
 *
 * @author wudarui
 */
@Slf4j
public class MybatisGenerator extends BaseGenerator<MyBatisGenerator> {
    private MybatisGenerator() {
    }

    /**
     * 生成fluent mybatis文件
     *
     * @return
     */
    public static Generator build() {
        return new MyBatisGenerator();
    }

    @Override
    protected List<BaseTemplate> getAllTemplates() {
        return Arrays.asList(
            new MappingTemplate(),
            new EntityTemplate(),
            new EntityHelperTemplate(),
            new MapperTemplate(),
            new PartitionMapperTemplate(),
            new EntityWrapperHelperTemplate(),
            new EntityQueryTemplate(),
            new EntityUpdateTemplate(),
            new BaseDaoTemplate(),
            new DaoIntfTemplate(),
            new DaoImplTemplate(),
            new EntityMapTemplate(),
            new TableMapTemplate(),
            new TableMixTemplate()
        );
    }

    /**
     * 生成汇总文件
     *
     * @param allContext
     */
    @Override
    protected void generateSummary(List<Map<String, Object>> allContext) {
        Map<String, Object> wrapper = new HashMap<>();
        {
            wrapper.put("configs", allContext);
            wrapper.put("basePackage", this.globalConfig.getBasePackage());
        }
        for (SummaryTemplate summary : TemplateList.summaries) {
            summary.setGlobalConfig(this.globalConfig);
            templateEngine.output(summary.getTemplateId(), wrapper, summary.getFilePath());
        }
    }
}