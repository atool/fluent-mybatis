package cn.org.atool.fluent.mybatis.utility;

import cn.org.atool.fluent.mybatis.base.crud.IWrapper;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;

import java.util.Map;

import static java.lang.Integer.min;

/**
 * SqlProvider帮助类
 *
 * @author wudarui
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class SqlProviderKit {
    public static WrapperData getWrapperData(Map map, String paraName) {
        IWrapper wrapper = getWrapper(map, paraName);
        if (wrapper.getWrapperData() == null) {
            throw new RuntimeException("no query condition found.");
        }
        return wrapper.getWrapperData();
    }

    public static IWrapper getWrapper(Map map, String paraName) {
        IWrapper wrapper = (IWrapper) map.get(paraName);
        if (wrapper == null) {
            throw new RuntimeException("param[" + paraName + "] not found.");
        }
        return wrapper;
    }

    public static <O> O getParas(Map map, String paraName) {
        Object obj = map.get(paraName);
        if (obj == null) {
            throw new RuntimeException("param[" + paraName + "] not found.");
        }
        return (O) obj;
    }

    private final static char[] EW_CONST = "#{ew.".toCharArray();

    private final static String sub = ".wrapperData.parameters.";

    /**
     * 替换变量占位符, 增加ew数组下标
     * #{ew.wrapperData.parameters.xxx}替换为#{ew[0].wrapperData.parameters.xxx}
     * 不采用正则表达式方式替换, 是编码方式替换简单，一次字符串扫描即可完成
     *
     * @param sql    sql语句
     * @param aIndex 变量参数序号
     * @return 改写后的sql
     */
    public static String addEwParaIndex(String sql, String aIndex) {
        StringBuilder buff = new StringBuilder();
        int match = 0;
        int len = sql.length();
        for (int loop = 0; loop < sql.length(); loop++) {
            char ch = sql.charAt(loop);
            if (match == 4) {
                if (sql.substring(loop, min(sub.length() + loop, len)).equals(sub)) {
                    buff.append(aIndex);
                }
                match = 0;
            } else {
                // 匹配字符串 "#{ew.", 完全匹配上 match=4
                match = EW_CONST[match] == ch ? match + 1 : 0;
            }
            buff.append(ch);
        }
        return buff.toString();
    }
}