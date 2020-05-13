package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.util.MybatisInsertUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;

import java.util.ArrayList;
import java.util.List;

import static cn.org.atool.fluent.mybatis.method.SqlFragment.*;
import static java.util.stream.Collectors.joining;

/**
 * InsertSelected 插入非null的字段，忽略null字段
 *
 * @author darui.wu
 * @create 2020/5/12 8:51 下午
 */
public class InsertSelected extends AbstractMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        KeyGenerator keyGenerator = new NoKeyGenerator();
        // 表包含主键处理逻辑， 如果不包含主键当普通字段处理
        String keyProperty = null;
        String keyColumn = null;
        if (StringUtils.isNotEmpty(tableInfo.getKeyProperty())) {
            if (tableInfo.getIdType() == IdType.AUTO) {
                keyGenerator = new Jdbc3KeyGenerator();
                keyProperty = tableInfo.getKeyProperty();
                keyColumn = tableInfo.getKeyColumn();
            } else if (tableInfo.getKeySequence() != null) {
                keyGenerator = TableInfoHelper.genKeyGenerator(tableInfo, builderAssistant, this.getMethodName(), languageDriver);
                keyProperty = tableInfo.getKeyProperty();
                keyColumn = tableInfo.getKeyColumn();
            }
        }

        SqlSource sqlSource = this.getMethodSql(modelClass, tableInfo);
        return super.addMappedStatement(mapperClass, this.getMethodName(), sqlSource, SqlCommandType.INSERT, modelClass, null, Integer.class, keyGenerator, keyProperty, keyColumn);
    }

    /**
     * <pre>
     *      INSERT INTO table_name
     * 		<trim prefix="(" suffix=")" suffixOverrides=",">
     * 			<if test="ew.field != null">
     * 				field,
     * 			</if>
     * 		</trim>
     * 		<trim prefix="values (" suffix=")" suffixOverrides=",">
     * 			<if test="ew.field != null">
     * 				#{field},
     * 			</if>
     * 		</trim>
     * 	</pre>
     *
     * @param modelClass
     * @param table      表结构
     * @return
     */
    private SqlSource getMethodSql(Class<?> modelClass, TableInfo table) {
        String sql = new StringBuilder()
            .append("<script>")
            .append("\nINSERT INTO ").append(table.getTableName())
            .append("\n<trim prefix='(' suffix=')' suffixOverrides=','>\n".replace('\'', '"'))
            .append(this.getInsertFields(table))
            .append("\n</trim>")
            .append("\n<trim prefix='VALUES (' suffix=')' suffixOverrides=','>\n".replace('\'', '"'))
            .append(this.getInsertValues(table))
            .append("\n</trim>")
            .append("\n</script>")
            .toString();
        return languageDriver.createSqlSource(configuration, sql, modelClass);
    }

    /**
     * <pre>
     *      <if test="field != null">field,</if>
     * </pre>
     *
     * @param table 表结构
     * @return
     */
    private String getInsertFields(TableInfo table) {
        List<String> ifList = new ArrayList<>();

        ifList.add(getInsertFieldIfNotNull(table.getKeyProperty(), table.getKeyColumn()));
        table.getFieldList().stream()
            .map(field -> {
                String defaultValue = MybatisInsertUtil.findInsertDefaultValue(field);
                if (defaultValue == null) {
                    return getInsertFieldIfNotNull(field.getProperty(), field.getColumn());
                } else {
                    return field.getColumn() + COMMA;
                }
            })
            .forEach(ifList::add);
        return ifList.stream().collect(joining(NEWLINE));
    }

    /**
     * <pre>
     *     <if test="field != null">#{field},</if>
     * </pre>
     *
     * @param table
     * @return
     */
    private String getInsertValues(TableInfo table) {
        List<String> ifList = new ArrayList<>();

        ifList.add(getInsertValueIfNotNull(table.getKeyProperty()));
        table.getFieldList().stream()
            .map(field -> {
                String defaultValue = MybatisInsertUtil.findInsertDefaultValue(field);
                if (defaultValue == null) {
                    return getInsertValueIfNotNull(field.getProperty());
                } else {
                    return getInsertValueChooseNotNullAndDefault(field.getProperty(), defaultValue);
                }
            })
            .forEach(ifList::add);
        return ifList.stream().collect(joining(NEWLINE));
    }

    private String getMethodName() {
        return "insertSelected";
    }
}