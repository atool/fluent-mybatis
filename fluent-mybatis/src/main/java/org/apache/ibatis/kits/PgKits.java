package org.apache.ibatis.kits;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;

import java.util.Arrays;
import java.util.stream.Collectors;

@SuppressWarnings("rawtypes")
public interface PgKits {
    static String onConflictDoNothing(String statement, FieldMapping... fields) {
        String joins = Arrays.stream(fields)
            .map(f -> f.column)
            .collect(Collectors.joining(", "));
        return String.format("%s ON CONFLICT(%s) DO NOTHING", statement, joins);
    }

    static String onConflictDoNothing(String statement, String... fields) {
        return String.format("%s ON CONFLICT(%s) DO NOTHING", statement, String.join(", ", fields));
    }
}
