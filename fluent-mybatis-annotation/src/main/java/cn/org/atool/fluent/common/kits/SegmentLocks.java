package cn.org.atool.fluent.common.kits;

import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * SegmentLocks: 分段锁执行工具
 *
 * @author darui.wu
 */
@SuppressWarnings({"unused"})
public class SegmentLocks<T> {
    private final int lock_size;
    /**
     * 分段锁对象
     */
    private final Object[] LOCKS;

    public SegmentLocks() {
        this(8);
    }

    public SegmentLocks(int lockSize) {
        this.lock_size = lockSize;
        this.LOCKS = IntStream.range(0, lock_size).mapToObj(i -> new Object()).toArray();
    }

    /**
     * 在分段锁中执行
     *
     * @param unExecutePredicate 不执行锁判断
     * @param lockValue          锁对象
     * @param executor           执行器
     */
    public void lockDoing(Predicate<T> unExecutePredicate, T lockValue, IExecutor executor) {
        if (unExecutePredicate.test(lockValue)) {
            return;
        }
        int hash = Math.abs(lockValue.hashCode()) % lock_size;
        synchronized (LOCKS[hash]) {
            if (!unExecutePredicate.test(lockValue)) {
                executor.execute();
            }
        }
    }

    /**
     * IExecutor: 外部执行器
     *
     * @author wudarui
     */
    @FunctionalInterface
    public interface IExecutor {
        void execute();
    }
}