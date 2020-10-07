package cn.org.atool.fluent.mybatis.generator;

import cn.org.atool.fluent.mybatis.generator.template.dao.DaoImplTemplate;
import cn.org.atool.fluent.mybatis.generator.template.dao.DaoIntfTemplate;
import cn.org.atool.fluent.mybatis.generator.template.entity.EntityTemplate;
import lombok.extern.slf4j.Slf4j;
import org.test4j.generator.mybatis.DataMapGenerator;
import org.test4j.generator.mybatis.config.IGlobalConfig;
import org.test4j.generator.mybatis.template.BaseTemplate;
import org.test4j.generator.mybatis.template.DataMapTemplateList;
import org.test4j.generator.mybatis.template.summary.SummaryTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库表列表
 *
 * @author wudarui
 */
@Slf4j
public class EntityGenerator2 extends DataMapGenerator {
    private EntityGenerator2() {
    }

    private boolean withTest = false;

    /**
     * 生成fluent mybatis文件
     *
     * @return
     */
    public static IGlobalConfig buildWithTest() {
        EntityGenerator2 generator = new EntityGenerator2();
        generator.withTest = true;
        return generator;
    }

    /**
     * 生成fluent mybatis文件
     *
     * @return
     */
    public static IGlobalConfig build() {
        return new EntityGenerator2();
    }

    private List<BaseTemplate> list = new ArrayList<>();

    @Override
    protected synchronized List<BaseTemplate> getAllTemplates() {
        if (list.size() == 0) {
            list.add(new EntityTemplate());
            list.add(new DaoIntfTemplate());
            list.add(new DaoImplTemplate());
            if (withTest) {
                list.addAll(DataMapTemplateList.datamap_list);
            }
        }
        return list;
    }

    @Override
    protected void generateSummary(List<Map<String, Object>> allContext) {
        Map<String, Object> wrapper = new HashMap<>();
        {
            wrapper.put("configs", allContext);
            wrapper.put("basePackage", this.globalConfig.getBasePackage());
        }
        if (!withTest) {
            return;
        }
        for (SummaryTemplate summary : DataMapTemplateList.summaries) {
            summary.setGlobalConfig(this.globalConfig);
            templateEngine.output(summary.getTemplateId(), wrapper, summary.getFilePath());
        }
    }
}