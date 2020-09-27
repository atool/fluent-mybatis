package cn.org.atool.fluent.mybatis.customize;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;

/**
 * StringArrayTypeHandler: 将string处理为string数组
 *
 * @author wudarui
 */
public class StringArrayTypeHandler implements TypeHandler<String[]> {

    @Override
    public String[] getResult(ResultSet rs, String columnName)
        throws SQLException {
        String columnValue = rs.getString(columnName);
        return this.getStringArray(columnValue);
    }

    @Override
    public String[] getResult(ResultSet rs, int columnIndex)
        throws SQLException {
        String columnValue = rs.getString(columnIndex);
        return this.getStringArray(columnValue);
    }

    @Override
    public String[] getResult(CallableStatement cs, int columnIndex)
        throws SQLException {
        String columnValue = cs.getString(columnIndex);
        return this.getStringArray(columnValue);
    }

    @Override
    public void setParameter(PreparedStatement ps, int index, String[] parameter,
                             JdbcType jdbcType) throws SQLException {
        if (parameter == null)
            ps.setNull(index, Types.VARCHAR);
        else {
            String result = String.join(DELIMITER, parameter);
            ps.setString(index, result);
        }
    }

    private String[] getStringArray(String columnValue) {
        return columnValue == null ? null : columnValue.split(DELIMITER);
    }

    static final String DELIMITER = ",";
}
