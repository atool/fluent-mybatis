package cn.org.atool.fluent.form.annotation;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * API Method Type
 *
 * @author darui.wu
 */
public enum MethodType {
    Auto,
    /**
     * 根据入参和返回值自动判断
     * findOne, listEntity, stdPaged, tagPaged
     */
    Query("top", "find", "count", "list", "stdPaged", "tagPaged", "paged", "exists"),
    /**
     * 更新数据
     */
    Update("update"),
    /**
     * 保存数据
     */
    Save("save", "insert"),
    /**
     * 物理删除操作
     */
    Delete("delete"),
    /**
     * 逻辑删除操作
     */
    LogicDelete("logicDelete");

    private final String[] prefixes;

    MethodType(String... prefixes) {
        this.prefixes = prefixes;
    }

    public boolean match(String method) {
        for (String prefix : prefixes) {
            if (method.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 所有可能自动推断的前缀值列表
     */
    public static Set<String> AUTO_PREFIX = Arrays.stream(MethodType.values()).flatMap(t -> Arrays.stream(t.prefixes)).collect(Collectors.toSet());
}