package cn.org.atool.fluent.common.kits;

/**
 * key类型为string的Pair
 *
 * @author wudarui
 */
public class StrKey<O> extends KeyVal<String, O> {
    public StrKey(String key, O val) {
        super(key, val);
    }

    @Override
    public String key() {
        return super.key();
    }

    public static <O> StrKey<O> kv(String key, O val) {
        return new StrKey<>(key, val);
    }
}
