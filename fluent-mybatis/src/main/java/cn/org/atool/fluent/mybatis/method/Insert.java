package cn.org.atool.fluent.mybatis.method;

import cn.org.atool.fluent.mybatis.method.model.MapperParam;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;
import cn.org.atool.fluent.mybatis.util.MybatisInsertUtil;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.ArrayList;
import java.util.List;

import static cn.org.atool.fluent.mybatis.method.model.SqlBuilder.safeParam;
import static java.util.stream.Collectors.joining;

/**
 * InsertSelected 插入非null的字段，忽略null字段
 *
 * @author darui.wu
 * @create 2020/5/12 8:51 下午
 */
public class Insert extends BaseMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo table) {
        MapperParam mapper = MapperParam.insertMapperParam(mapperClass, "insert")
            .setParameterType(modelClass)
            .setResultType(Integer.class)
            .setSql(this.getMethodSql(table));
        super.setPrimaryKey(mapper, table);

        super.setPrimaryKey(mapper, table);
        return super.addMappedStatement(mapper);
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
     * @param table 表结构
     * @return
     */
    @Override
    protected String getMethodSql(TableInfo table) {
        return SqlBuilder.instance()
            .beginScript()
            .insert(table.getTableName())
            .beginTrim("(", ")", ",")
            .append(this.getInsertFields(table))
            .endTrim()
            .beginTrim("VALUES (", ")", ",")
            .append(this.getInsertValues(table))
            .endTrim()
            .endScript()
            .toString();
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

        ifList.add(SqlBuilder.instance()
            .ifValue("", table.getKeyProperty(), table.getKeyColumn())
            .toString()
        );
        table.getFieldList().stream()
            .map(field -> {
                if (MybatisInsertUtil.isInsertDefaultField(field)) {
                    return field.getColumn() + COMMA;
                } else {
                    return SqlBuilder.instance()
                        .ifValue("", field.getProperty(), field.getColumn())
                        .toString();
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

        ifList.add(SqlBuilder.instance()
            .ifValue("", table.getKeyProperty(), safeParam(table.getKeyProperty()))
            .toString()
        );

        table.getFieldList().stream()
            .map(this::fieldValue)
            .forEach(ifList::add);
        return ifList.stream().collect(joining(NEWLINE));
    }

    /**
     * 单个字段值, if or choose 标签
     *
     * @param field
     * @return
     */
    private String fieldValue(TableFieldInfo field) {
        if (MybatisInsertUtil.isInsertDefaultField(field)) {
            return SqlBuilder.instance()
                .choose("", field.getProperty(), field.getUpdate())
                .toString();
        } else {
            return SqlBuilder.instance()
                .ifValue("", field.getProperty(), safeParam(field.getProperty()))
                .toString();
        }
    }
}