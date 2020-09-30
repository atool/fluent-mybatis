package cn.org.atool.fluent.mybatis.mapper;

import cn.org.atool.fluent.mybatis.base.IWrapper;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class MapperUtils {

    public static final String Double_Quota_Str = String.valueOf('"');

    public static String Param_Map = "map";

    public static String Param_Id = "id";

    public static String Param_Ids = "ids";

    public static String Param_Coll = "coll";

    public static String Param_List = "list";

    public static String Param_CM = "cm";

    public static String Param_EW = "ew";

    public static String Param_ET = "et";

    public static String Param_Query = "query";

    public static String Param_Entity = "entity";

    public static WrapperData getWrapperData(Map map, String paraName) {
        IWrapper wrapper = (IWrapper) map.get(paraName);
        if (wrapper == null) {
            throw new RuntimeException("param[" + paraName + "] not found.");
        }
        if (wrapper == null || wrapper.getWrapperData() == null) {
            throw new RuntimeException("no query condition found.");
        }
        return wrapper.getWrapperData();
    }

    public static WrapperData getWrapperData(IWrapper wrapper) {
        if (wrapper == null || wrapper.getWrapperData() == null) {
            throw new RuntimeException("no query condition found.");
        }
        return wrapper.getWrapperData();
    }

    public static <O> O getParas(Map map, String paraName) {
        Object obj = map.get(paraName);
        if (obj == null) {
            throw new RuntimeException("param[" + paraName + "] not found.");
        }
        return (O) obj;
    }

    /**
     * 表达式 #{prefix[index].property} 构建
     * "#{" + prefix + "[" + index+ "]." + property + "}"
     *
     * @param prefix
     * @param property
     * @param index
     * @return
     */
    public static String listIndexEl(String prefix, String property, String index) {
        return new StringBuilder()
            .append(Double_Quota_Str)
            .append("#{")
            .append(prefix).append("[")
            .append(Double_Quota_Str)
            .append(" + ").append(index).append(" + ")
            .append(Double_Quota_Str)
            .append("].").append(property)
            .append("}")
            .append(Double_Quota_Str).toString();
    }

    public static String quotaJoining(List<String> list) {
        return list.stream().map(c -> '"' + c + '"').collect(joining(", "));
    }

    public static String quotaJoining(String[] list) {
        return Stream.of(list).map(c -> '"' + c + '"').collect(joining(", "));
    }
}