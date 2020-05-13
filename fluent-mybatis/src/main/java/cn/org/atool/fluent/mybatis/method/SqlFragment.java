package cn.org.atool.fluent.mybatis.method;

/**
 * SqlFragment
 *
 * @author darui.wu
 * @create 2020/5/13 4:59 下午
 */
public class SqlFragment {
    /**
     * 字段非空时，插入字段定义
     * <pre>
     *      < if test="field != null">field,< /if>
     * </pre>
     */
    final static String INSERT_FIELD_IF_NOT_NULL = "<if test='%s != null'>%s,</if>".replace('\'', '"');

    /**
     * 字段非空时，插入字段定义
     * <pre>
     *      < if test="field != null">field,< /if>
     * </pre>
     *
     * @param property 实体属性名称
     * @param column   数据库字段名称
     * @return
     */
    public static String getInsertFieldIfNotNull(String property, String column) {
        return String.format(INSERT_FIELD_IF_NOT_NULL, property, column);
    }

    /**
     * 字段非空时，插入字段值定义
     * <pre>
     *      < if test="field != null">#{field},< /if>
     * </pre>
     */
    final static String INSERT_VALUE_IF_NOT_NULL = "<if test='%s != null'>#{%s},</if>".replace('\'', '"');

    /**
     * 字段非空时，插入字段值定义
     * <pre>
     *      < if test="field != null">#{field},< /if>
     * </pre>
     *
     * @param fieldName 字段名称
     * @return
     */
    public static String getInsertValueIfNotNull(String fieldName) {
        return String.format(INSERT_VALUE_IF_NOT_NULL, fieldName, fieldName);
    }

    final static String INSERT_VALUE_CHOOSE_NOT_NULL_AND_DEFAULT = new StringBuilder()
        .append("<choose><when test='%s != null'>#{%s},</when><otherwise>%s,</otherwise></choose>".replace('\'', '"'))
        .toString();

    /**
     * 当字段不为空时，插入字段值
     * 当字段为空时，插入默认值
     *
     * @param fieldName
     * @param defaultValue
     * @return
     */
    public static String getInsertValueChooseNotNullAndDefault(String fieldName, String defaultValue) {
        return String.format(INSERT_VALUE_CHOOSE_NOT_NULL_AND_DEFAULT, fieldName, fieldName, defaultValue);
    }
}