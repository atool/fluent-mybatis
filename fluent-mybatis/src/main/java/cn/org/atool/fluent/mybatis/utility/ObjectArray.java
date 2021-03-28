package cn.org.atool.fluent.mybatis.utility;

import java.util.ArrayList;
import java.util.List;

/**
 * 将原生类型的数组转换成对象数组
 *
 * @author darui.wudr
 */
@SuppressWarnings({"rawtypes"})
public class ObjectArray {
    /**
     * An empty immutable <code>Class</code> array.
     */

    // boolean
    // byte
    // char
    // short int long
    // float double
    private static Object[] toPrimitiveArray(char values[]) {
        List<Object> objs = new ArrayList<>();
        for (Character value : values) {
            objs.add(value);
        }
        return objs.toArray();
    }

    private static Object[] toPrimitiveArray(float values[]) {
        List<Object> objs = new ArrayList<Object>();
        for (Float value : values) {
            objs.add(value);
        }
        return objs.toArray();
    }

    private static Object[] toPrimitiveArray(long values[]) {
        List<Object> objs = new ArrayList<Object>();
        for (Long value : values) {
            objs.add(value);
        }
        return objs.toArray();
    }

    private static Object[] toPrimitiveArray(short values[]) {
        List<Object> objs = new ArrayList<Object>();
        for (Short value : values) {
            objs.add(value);
        }
        return objs.toArray();
    }

    private static Object[] toPrimitiveArray(int values[]) {
        List<Object> objs = new ArrayList<Object>();
        for (Integer value : values) {
            objs.add(value);
        }
        return objs.toArray();
    }

    private static Object[] toPrimitiveArray(double values[]) {
        List<Object> objs = new ArrayList<Object>();
        for (Double value : values) {
            objs.add(value);
        }
        return objs.toArray();
    }

    private static Object[] toPrimitiveArray(boolean values[]) {
        List<Object> objs = new ArrayList<Object>();
        for (Boolean value : values) {
            objs.add(value);
        }
        return objs.toArray();
    }

    private static Object[] toPrimitiveArray(byte values[]) {
        List<Object> objs = new ArrayList<Object>();
        for (Byte value : values) {
            objs.add(value);
        }
        return objs.toArray();
    }

    /**
     * 将primitive对象数组转换为object类型数组
     *
     * @param value
     * @return
     */
    public static Object[] array(Object value) {
        if (value instanceof int[]) {
            return toPrimitiveArray((int[]) value);
        } else if (value instanceof long[]) {
            return toPrimitiveArray((long[]) value);
        } else if (value instanceof short[]) {
            return toPrimitiveArray((short[]) value);
        } else if (value instanceof float[]) {
            return toPrimitiveArray((float[]) value);
        } else if (value instanceof double[]) {
            return toPrimitiveArray((double[]) value);
        } else if (value instanceof char[]) {
            return toPrimitiveArray((char[]) value);
        } else if (value instanceof byte[]) {
            return toPrimitiveArray((byte[]) value);
        } else if (value instanceof boolean[]) {
            return toPrimitiveArray((boolean[]) value);
        } else if (value instanceof Object[]) {
            return (Object[]) value;
        } else {
            throw new RuntimeException("object isn't an array.");
        }
    }

    public static Boolean toBoolean(Object value) {
        if (value == null) {
            return null;
        } else {
            String _value = String.valueOf(value).trim();
            if (_value.matches("\\-?\\d+")) {
                return !_value.equals("0");
            } else {
                return _value.equalsIgnoreCase("true");
            }
        }
    }
}