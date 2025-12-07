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
 * PostgreSQL JSONB 类型处理器 - List<String>
 * <p>
 * 将 Java 的 List<String> 类型映射为 PostgreSQL 的 JSONB 类型
 * <p>
 * 使用示例：
 *
 * <pre>
 * {@code
 * @TableField(value = "options", typeHandler = JsonbStringListTypeHandler.class)
 * private List<String> options;
 * }
 * </pre>
 *
 * @author darui.wu
 */
@SuppressWarnings("unused")
@MappedJdbcTypes(JdbcType.OTHER)
public class PgJsonbStringListTypeHandler extends BaseTypeHandler<List<String>> {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final TypeReference<List<String>> TYPE_REFERENCE = new TypeReference<>() {
    };

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType)
        throws SQLException {
        try {
            PGobject jsonObject = new PGobject();
            jsonObject.setType("jsonb");
            jsonObject.setValue(MAPPER.writeValueAsString(parameter));
            ps.setObject(i, jsonObject);
        } catch (JsonProcessingException e) {
            throw new SQLException("Error converting List<String> to JSONB: " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseJsonb(rs.getString(columnName));
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseJsonb(rs.getString(columnIndex));
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseJsonb(cs.getString(columnIndex));
    }

    /**
     * 解析 JSONB 字符串为 List<String>
     *
     * @param json JSONB 字符串，如 ["option1", "option2", "option3"]
     * @return List<String> 对象，如果 json 为 null 或空则返回空列表
     * @throws SQLException 解析失败时抛出
     */
    private List<String> parseJsonb(String json) throws SQLException {
        if (json == null || json.isEmpty()) {
            return List.of();
        }
        try {
            return MAPPER.readValue(json, TYPE_REFERENCE);
        } catch (IOException e) {
            throw new SQLException("Error parsing JSONB to List<String>: " + json + ", error: " + e.getMessage(), e);
        }
    }
}
