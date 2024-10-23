package cn.org.atool.fluent.mybatis.kits;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings({"rawtypes", "unused"})
public interface PgKit {
    static <T> String joining(T[] fields, Function<T, String> mapping) {
        return Arrays.stream(fields)
            .map(mapping)
            .collect(Collectors.joining(", "));
    }

    static String onConflictDoNothing(String statement, FieldMapping... fields) {
        String uIndex = joining(fields, f -> f.column);
        return String.format("%s ON CONFLICT(%s) DO NOTHING", statement, uIndex);
    }

    static String onConflictDoNothing(String statement, String... fields) {
        String uIndex = joining(fields, f -> f);
        return String.format("%s ON CONFLICT(%s) DO NOTHING", statement, uIndex);
    }

    static String onConflictDoUpdate(String statement, FieldMapping[] fields, String updateBy) {
        String uIndex = joining(fields, f -> f.column);
        return String.format("%s ON CONFLICT(%s) DO UPDATE SET %s", statement, uIndex, updateBy);
    }

    static String onConflictDoUpdate(String statement, String[] fields, String updateBy) {
        String uIndex = joining(fields, f -> f);
        return String.format("%s ON CONFLICT(%s) DO UPDATE SET %s", statement, uIndex, updateBy);
    }

    static String onConflictDoUpdate(String statement, FieldMapping[] fields, FieldMapping... updates) {
        String uIndex = joining(fields, f -> f.column);
        String updateBy = joining(updates, f -> String.format("%s = excluded.%s", f.column, f.column));
        return String.format("%s ON CONFLICT(%s) DO UPDATE SET %s", statement, uIndex, updateBy);
    }

    static String onConflictDoUpdate(String statement, String[] fields, String[] updates) {
        String uIndex = String.join(", ", fields);
        String updateBy = joining(updates, f -> String.format("%s = excluded.%s", f, f));
        return String.format("%s ON CONFLICT(%s) DO UPDATE SET %s", statement, uIndex, updateBy);
    }
}
