package cn.org.atool.fluent.form.meta.entry;

import cn.org.atool.fluent.form.meta.ArgumentMeta;
import cn.org.atool.fluent.form.meta.EntryMeta;

/**
 * 参数EntryMeta
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked"})
public class ArgEntryMeta extends EntryMeta {
    private final ArgumentMeta arg;

    private final EntryMeta meta;

    public ArgEntryMeta(ArgumentMeta arg) {
        super(arg.entryName, arg.isAnd, arg.entryType, arg.ignoreNull);
        this.arg = arg;
        this.meta = null;
    }

    public ArgEntryMeta(ArgumentMeta arg, EntryMeta meta) {
        super(meta.name, arg.isAnd, meta.type, meta.ignoreNull);
        this.arg = arg;
        this.meta = meta;
    }

    @Override
    protected ArgumentGetter getter() {
        return args -> {
            if (this.meta == null) {
                return args[arg.index];
            } else if (this.meta.getter == null) {
                return null;
            } else {
                Object object = args[arg.index];
                return meta.getter.apply(object);
            }
        };
    }
}