package cn.org.atool.fluent.common.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * kv列表
 *
 * @author wudarui
 */
@SuppressWarnings({"unchecked", "rawtypes", "unused"})
public class KvList {
    private final List<StrKey> kvs = new ArrayList<>(16);

    public <O> KvList kv(String key, O val) {
        this.kvs.add(StrKey.kv(key, val));
        return this;
    }

    public <O> KvList kv(StrKey... kvs) {
        this.kvs.addAll(Arrays.asList(kvs));
        return this;
    }

    public <O> List<StrKey<O>> list() {
        return (List) kvs;
    }
}
