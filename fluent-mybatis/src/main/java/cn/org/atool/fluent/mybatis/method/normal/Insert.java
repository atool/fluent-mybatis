package cn.org.atool.fluent.mybatis.method.normal;

import cn.org.atool.fluent.mybatis.method.AbstractMethod;
import cn.org.atool.fluent.mybatis.method.metadata.DbType;
import cn.org.atool.fluent.mybatis.method.metadata.FieldMeta;
import cn.org.atool.fluent.mybatis.method.metadata.PrimaryMeta;
import cn.org.atool.fluent.mybatis.method.metadata.TableMeta;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;
import cn.org.atool.fluent.mybatis.method.model.StatementType;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;

import static cn.org.atool.fluent.mybatis.condition.model.Constants.COMMA;
import static cn.org.atool.fluent.mybatis.method.model.StatementId.Method_Insert;

/**
 * InsertSelected 插入非null的字段，忽略null字段
 *
 * @author darui.wu
 * @create 2020/5/12 8:51 下午
 */
public class Insert extends AbstractMethod {
    public Insert(DbType dbType) {
        super(dbType);
    }

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
    public String getMethodSql(Class entity, TableMeta table) {
        SqlBuilder builder = SqlBuilder.instance();

        this.insertStatement(builder, entity, table.getPrimary());
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
     * <pre>
     *     <insert id="insert" keyColumn="id" keyProperty="id"
     *             parameterType="...entity.XyzEntity" useGeneratedKeys="true">
     * </pre>
     *
     * @param builder
     * @param entity
     * @param primary
     */
    protected void insertStatement(SqlBuilder builder, Class entity, PrimaryMeta primary) {
        if (primary == null) {
            builder.quotas("<insert id='%s' parameterType='%s'>", statementId(), entity.getName());
            return;
        }
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

    private void value(SqlBuilder builder, FieldMeta field) {
        String insert = field.getInsert();
        if (MybatisUtil.isEmpty(insert)) {
            builder.ifThen("@property != null", "#{@property},", field.getProperty(), field.getColumn());
        } else {
            builder.choose("@property != null", "#{@property},", insert + COMMA,
                field.getProperty(), field.getColumn());
        }
    }

    private void field(SqlBuilder builder, FieldMeta field) {
        if (isInsertDefault(field)) {
            builder.append(field.getColumn() + COMMA);
        } else {
            builder.ifThen("@property != null", "@column,", field.getProperty(), field.getColumn());
        }
    }
}