package cn.org.atool.fluent.mybatis.method.normal;

import cn.org.atool.fluent.mybatis.metadata.FieldInfo;
import cn.org.atool.fluent.mybatis.metadata.PrimaryInfo;
import cn.org.atool.fluent.mybatis.metadata.TableInfo;
import cn.org.atool.fluent.mybatis.method.AbstractMethod;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;
import cn.org.atool.fluent.mybatis.method.model.StatementType;
import cn.org.atool.fluent.mybatis.util.MybatisUtil;

import static cn.org.atool.fluent.mybatis.method.model.StatementId.Method_Insert;
import static cn.org.atool.fluent.mybatis.util.Constants.COMMA;

/**
 * InsertSelected 插入非null的字段，忽略null字段
 *
 * @author darui.wu
 * @create 2020/5/12 8:51 下午
 */
public class Insert extends AbstractMethod {

    @Override
    public String statementId() {
        return Method_Insert;
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
    public String getMethodSql(Class entity, TableInfo table) {
        SqlBuilder builder = SqlBuilder.instance();
        if (table.getPrimary() == null) {
            this.insertNotPrimary(builder, entity);
        } else {
            this.insertHasPrimary(builder, entity, table.getPrimary());
        }
        builder.insert(table, this.isSpecTable())
            .brackets(() -> {
                builder
                    .ifThen("@property != null", "@column,", table.getKeyProperty(), table.getKeyColumn())
                    .eachJoining(table.getFields(), (field) -> field(builder, field));
            })
            .append("VALUES")
            .brackets(() -> {
                builder
                    .ifThen("@property != null", "#{@property},", table.getKeyProperty(), table.getKeyColumn())
                    .eachJoining(table.getFields(), (field) -> value(builder, field));
            })
            .end(StatementType.insert);
        String text = builder.toString();
        return text;
    }

    /**
     * 无主键的表插入
     *
     * @param builder
     * @param entity
     */
    private void insertNotPrimary(SqlBuilder builder, Class entity) {
        builder.quotas("<insert id='%s' parameterType='%s'>", statementId(), entity.getName());
    }

    /**
     * 有主键的表插入
     *
     * @param builder
     * @param entity
     * @param primary
     */
    private void insertHasPrimary(SqlBuilder builder, Class entity, PrimaryInfo primary) {
        if (primary.isAutoIncrease()) {
            builder.quotas("<insert id='%s' keyColumn='%s' keyProperty='%s' parameterType='%s' useGeneratedKeys='true'>",
                statementId(), primary.getColumn(), primary.getProperty(), entity.getName()
            );
        } else {
//            if (primary.getSeqName().isEmpty()) {
            builder.quotas("<insert id='%s' keyProperty='%s' parameterType='%s'>",
                statementId(), primary.getProperty(), entity.getName()
            );
            //        else {
//            builder
//                .quotas("<insert id='%s' parameterType='%s'>", statementId(), entity.getName()).newLine()
//                .quotas("<selectKey keyColumn='%s' keyProperty='%s' resultType='%s' order='BEFORE'>", primary.getColumn(), primary.getProperty(), primary.getProperty()).newLine()
//                .append("select SEQ_ID.nextval from %s", primary.getSeqName()).newLine()
//                .append("</selectKey>").newLine();
//        }
        }

    }


    private void value(SqlBuilder builder, FieldInfo field) {
        String insert = field.getInsert();
        if (MybatisUtil.isEmpty(insert)) {
            builder.ifThen("@property != null", "#{@property},", field.getProperty(), field.getColumn());
        } else {
            builder.choose("@property != null", "#{@property},", insert + COMMA,
                field.getProperty(), field.getColumn());
        }
    }

    private void field(SqlBuilder builder, FieldInfo field) {
        if (isInsertDefault(field)) {
            builder.append(field.getColumn() + COMMA);
        } else {
            builder.ifThen("@property != null", "@column,", field.getProperty(), field.getColumn());
        }
    }
}