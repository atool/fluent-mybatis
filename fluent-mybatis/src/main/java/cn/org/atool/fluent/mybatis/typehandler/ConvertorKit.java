package cn.org.atool.fluent.mybatis.typehandler;

import cn.org.atool.fluent.common.kits.KeyMap;
import cn.org.atool.fluent.mybatis.utility.ObjectArray;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.Temporal;
import java.util.Date;

import static cn.org.atool.fluent.mybatis.utility.StrConstant.DateStrFormat;

/**
 * ConvertorKit: 参数转换工具类
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class ConvertorKit {
    private static final KeyMap<IConvertor> CONVERTORS = new KeyMap<>();

    /**
     * 将数据 value 转换为对应的type类型对象
     *
     * @param value 输入值
     * @param type  转换成类型
     * @return 转换或的值
     */
    public static <O> O convertValueToType(Object value, Type type) {
        if (value == null) {
            return null;
        }
        /* 查找预置的类型转换器 */
        IConvertor convertor = findConvertor(type);
        if (convertor != null && convertor.match(value)) {
            return (O) convertor.get(value);
        }
        if (type instanceof Class) {
            return convertValueToClass(value, (Class) type);
        } else {
            return (O) value;
        }
    }

    public static <O> O convertValueToClass(Object value, Class type) {
        if (value == null) {
            return null;
        }
        if (type.isAssignableFrom(value.getClass())) {
            return (O) value;
        }
        if (type == Long.class || type == long.class) {
            return (O) Long.valueOf(value.toString());
        } else if (type == Integer.class || type == int.class) {
            return (O) Integer.valueOf(value.toString());
        } else if (type == Boolean.class || type == boolean.class) {
            return (O) ObjectArray.toBoolean(value);
        } else if (type == BigDecimal.class) {
            return (O) new BigDecimal(value.toString());
        } else if (type == BigInteger.class) {
            return (O) new BigInteger(value.toString());
        } else if (Temporal.class.isAssignableFrom(type) || Date.class.isAssignableFrom(type)) {
            return (O) convertDateTime(value, type);
        } else if (type.isEnum() && value instanceof String) {
            return (O) Enum.valueOf(type, (String) value);
        } else if (type == String.class) {
            return (O) toString(value);
        } else {
            return (O) value;
        }
    }

    private static Object convertDateTime(Object value, Class type) {
        if (value instanceof Date) {
            Date t = (Date) value;
            if (type == LocalDateTime.class) {
                return t.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            } else if (type == LocalDate.class) {
                return t.toInstant().atZone(ZoneOffset.systemDefault()).toLocalDate();
            } else if (type == LocalTime.class) {
                return t.toInstant().atZone(ZoneOffset.systemDefault()).toLocalTime();
            } else if (type == OffsetDateTime.class) {
                return t.toInstant().atZone(ZoneOffset.systemDefault()).toOffsetDateTime();
            } else {
                return value;
            }
        } else if (value instanceof String) {
            String str = (String) value;
            //noinspection AlibabaUndefineMagicConstant
            if (str.matches("\\d+")) {
                long ms = Long.parseLong(str);
                return convertDateTime(ms, type);
            } else {
                Date date = parseDate(str);
                return convertDateTime(date, type);
            }
        } else if (value instanceof Number) {
            long ms = ((Number) value).longValue();
            Date date = new Date();
            date.setTime(ms);
            return convertDateTime(date, type);
        } else {
            return value;
        }
    }

    @SuppressWarnings("AlibabaUndefineMagicConstant")
    private static Date parseDate(String str) {
        try {
            if (str.contains(".")) {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(str);
            } else if (str.contains(":")) {
                return new SimpleDateFormat(DateStrFormat).parse(str);
            } else {
                return new SimpleDateFormat("yyyy-MM-dd").parse(str);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

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
            return new SimpleDateFormat(DateStrFormat).format((Date) value);
        } else {
            return String.valueOf(value);
        }
    }

    private static IConvertor findConvertor(Type type) {
        return CONVERTORS.get(type.getTypeName());
    }

    public static void register(Type type, IConvertor convertor) {
        CONVERTORS.put(type.getTypeName(), convertor);
    }

    public static void register(String typeName, IConvertor convertor) {
        CONVERTORS.put(typeName, convertor);
    }
}