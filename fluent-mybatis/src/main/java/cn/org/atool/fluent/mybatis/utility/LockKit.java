package cn.org.atool.fluent.mybatis.utility;

import cn.org.atool.fluent.mybatis.functions.IExecutor;

import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * 分段锁执行工具
 *
 * @author darui.wu
 */
@SuppressWarnings({"unused"})
public class LockKit<T> {
    private final int lock_size;
    /**
     * 分段锁对象
     */
    private final Object[] LOCKS;

    public LockKit() {
        this(8);
    }

    public LockKit(int lockSize) {
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
}