package cn.org.atool.fluent.mybatis.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import ru.yandex.clickhouse.ClickHouseArray;

import java.sql.*;

/**
 * Java sql Array与 ClockHouse Array 转换器
 *
 * @author darui.wu
 */
public class ClickArrayHandler extends BaseTypeHandler<Array> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Array array, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, array);
    }

    @Override
    public Array getNullableResult(ResultSet resultSet, String s) throws SQLException {
        Object obj = resultSet.getObject(s);
        return parseClickHouseArrayToInt(obj);
    }

    @Override
    public Array getNullableResult(ResultSet resultSet, int i) throws SQLException {
        Object obj = resultSet.getObject(i);
        return parseClickHouseArrayToInt(obj);
    }

    @Override
    public Array getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        Object obj = callableStatement.getObject(i);
        return parseClickHouseArrayToInt(obj);
    }

    private Array parseClickHouseArrayToInt(Object obj) {
        if (obj instanceof ClickHouseArray) {
            return (Array) obj;
        }
        throw new RuntimeException(String.format("Object[%s] can't convert to Array.", obj == null ? "<null>" : obj.getClass().getName()));
    }
}