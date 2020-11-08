package cn.org.atool.fluent.mybatis.entity.base;

import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.TreeMaker;

import javax.annotation.processing.Messager;

/**
 * 编译器相关类
 *
 * @author darui.wu
 */
public interface IProcessor {
    /**
     * 返回Messager
     *
     * @return
     */
    Messager getMessager();

    /**
     * javac 编译器相关类
     *
     * @return
     */
    Trees getTrees();

    TreeMaker getTreeMaker();
}