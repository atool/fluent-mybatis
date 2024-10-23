package cn.org.atool.fluent.mybatis.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ApplyFunc {
    private String function;
    private List<Object> args = new ArrayList<>();

    public ApplyFunc(String function) {
        this.function = function;
    }

    public ApplyFunc(String function, Object... args) {
        this.function = function;
        this.args.addAll(Arrays.asList(args));
    }

    public ApplyFunc args(Object arg, Object... args) {
        this.args.add(arg);
        if (args.length > 0) {
            this.args.addAll(Arrays.asList(args));
        }
        return this;
    }

    public Object[] args() {
        return this.args.toArray();
    }
}
