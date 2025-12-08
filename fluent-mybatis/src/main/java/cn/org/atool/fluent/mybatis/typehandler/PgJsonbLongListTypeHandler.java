package cn.org.atool.fluent.mybatis.typehandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.postgresql.util.PGobject;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * PostgreSQL JSONB 类型处理器 - List&lt;Long&gt;
 * <p>
 * 将 Java 的 List&lt;Long&gt; 类型映射为 PostgreSQL 的 JSONB 类型
 * <p>
 * 使用示例：
 *
 * <pre>
 * {@code
 * @TableField(value = "tag_ids", typeHandler = JsonbLongListTypeHandler.class)
 * private List<Long> tagIds;
 * }
 * </pre>
 *
 * @author darui.wu
 */
@SuppressWarnings("unused")
@MappedJdbcTypes(JdbcType.OTHER)
public class PgJsonbLongListTypeHandler extends BaseTypeHandler<List<Long>> {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final TypeReference<List<Long>> TYPE_REFERENCE = new TypeReference<>() {
    };

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Long> parameter, JdbcType jdbcType)
            throws SQLException {
        try {
            PGobject jsonObject = new PGobject();
            jsonObject.setType("jsonb");
            jsonObject.setValue(MAPPER.writeValueAsString(parameter));
            ps.setObject(i, jsonObject);
        } catch (JsonProcessingException e) {
            throw new SQLException("Error converting List<Long> to JSONB: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseJsonb(rs.getString(columnName));
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseJsonb(rs.getString(columnIndex));
    }

    @Override
    public List<Long> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseJsonb(cs.getString(columnIndex));
    }

    /**
     * 解析 JSONB 字符串为 List<Long>
     *
     * @param json JSONB 字符串，如 "[1, 2, 3]"
     * @return List<Long> 对象，如果 json 为 null 或空则返回空列表
     * @throws SQLException 解析失败时抛出
     */
    private List<Long> parseJsonb(String json) throws SQLException {
        if (json == null || json.isEmpty()) {
            return List.of();
        }
        try {
            return MAPPER.readValue(json, TYPE_REFERENCE);
        } catch (IOException e) {
            throw new SQLException("Error parsing JSONB to List<Long>: " + json + ", error: " + e.getMessage(), e);
        }
    }
}
