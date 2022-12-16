package cn.org.atool.fluent.utils;

/**
 * key类型为string的Pair
 *
 * @author wudarui
 */
public class StrKey<O> extends KeyVal<String, O> {
    public StrKey(String key, O val) {
        super(key, val);
    }
}
