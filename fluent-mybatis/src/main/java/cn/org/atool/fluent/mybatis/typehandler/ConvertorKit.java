package cn.org.atool.fluent.mybatis.typehandler;

import cn.org.atool.fluent.common.kits.KeyMap;
import cn.org.atool.fluent.mybatis.utility.ObjectArray;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;

import static cn.org.atool.fluent.mybatis.utility.StrConstant.DateStrFormat;

/**
 * ConvertorKit: 参数转换工具类
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class ConvertorKit {
    private static final KeyMap<IConvertor> convertors = new KeyMap<>();

    /**
     * 将数据 value 转换为对应的type类型对象
     *
     * @param value 输入值
     * @param type  转换成类型
     * @return 转换或的值
     */
    public static Object convertValueToType(Object value, Type type) {
        if (value == null) {
            return null;
        }
        /* 查找预置的类型转换器 */
        IConvertor convertor = findConvertor(type);
        if (convertor != null && convertor.match(value)) {
            return convertor.get(value);
        }
        if (type instanceof Class) {
            return convertValueToClass(value, (Class) type);
        } else {
            return value;
        }
    }

    public static Object convertValueToClass(Object value, Class type) {
        if (type.isAssignableFrom(value.getClass())) {
            return value;
        }
        if (type == Long.class || type == long.class) {
            return Long.parseLong(value.toString());
        } else if (type == Integer.class || type == int.class) {
            return Integer.parseInt(value.toString());
        } else if (type == Boolean.class || type == boolean.class) {
            return ObjectArray.toBoolean(value);
        } else if (type == BigDecimal.class) {
            return new BigDecimal(value.toString());
        } else if (type == BigInteger.class) {
            return new BigInteger(value.toString());
        } else if (value instanceof Timestamp) {
            Timestamp t = (Timestamp) value;
            if (type == LocalDateTime.class) {
                return t.toLocalDateTime();
            } else if (type == LocalDate.class) {
                return LocalDate.from(t.toInstant().atZone(ZoneOffset.systemDefault()));
            } else if (type == LocalTime.class) {
                return LocalTime.from(t.toInstant().atZone(ZoneOffset.systemDefault()));
            } else {
                return value;
            }
        } else if (type.isEnum() && value instanceof String) {
            return Enum.valueOf(type, (String) value);
        } else if (type == String.class) {
            return toString(value);
        } else {
            return value;
        }
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat(DateStrFormat);

    /**
     * 转换为字符串
     *
     * @param value 输入值
     * @return 输出字符串
     */
    public static String toString(Object value) {
        if (value instanceof String) {
            return (String) value;
        } else if (value instanceof Date) {
            return sdf.format((Date) value);
        } else {
            return String.valueOf(value);
        }
    }

    private static IConvertor findConvertor(Type type) {
        return convertors.get(type.getTypeName());
    }

    public static void register(Type type, IConvertor convertor) {
        convertors.put(type.getTypeName(), convertor);
    }

    public static void register(String typeName, IConvertor convertor) {
        convertors.put(typeName, convertor);
    }
}