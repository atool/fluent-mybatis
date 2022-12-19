package cn.org.atool.fluent.common.kits;

/**
 * key类型为string的Pair
 *
 * @author wudarui
 */
public class KeyStr<O> extends KeyVal<String, O> {
    public KeyStr(String key, O val) {
        super(key, val);
    }

    @Override
    public String key() {
        return super.key();
    }

    public static <O> KeyStr<O> kv(String key, O val) {
        return new KeyStr<>(key, val);
    }
}
