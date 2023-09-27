package com.yiyan.boot.common.utils.tree;

import cn.hutool.core.lang.tree.Tree;

import java.util.Iterator;
import java.util.List;

/**
 * 树工具类
 *
 * @author MENGJIAO
 * @createDate 2023-05-05 10:11
 */
public class TreeUtils {

    /**
     * Iterator转树
     */
    public static <T extends Tree<T>> T buildTree(T root, Iterator<T> nodes) {
        if (root == null) {
            return null;
        }
        while (nodes.hasNext()) {
            T node = nodes.next();
            if (root.getId().equals(node.getParentId())) {
                root.getChildren().add(buildTree(node, nodes));
            }
        }
        return root;
    }

    /**
     * Iterable转树
     */
    public static <T extends Tree<T>> T buildTree(T root, Iterable<T> nodes) {
        if (root == null) {
            return null;
        }
        for (T node : nodes) {
            if (root.getId().equals(node.getParentId())) {
                root.getChildren().add(buildTree(node, nodes));
            }
        }
        return root;
    }

    /**
     * 数组转树
     */
    public static <T extends Tree<T>> T buildTree(T root, T[] nodes) {
        if (root == null) {
            return null;
        }
        for (T node : nodes) {
            if (root.getId().equals(node.getParentId())) {
                root.getChildren().add(buildTree(node, nodes));
            }
        }
        return root;
    }

    /**
     * List转树
     */
    public static <T extends Tree<T>> T buildTree(T root, List<T> nodes) {
        if (root == null) {
            return null;
        }
        for (T node : nodes) {
            if (root.getId().equals(node.getParentId())) {
                root.getChildren().add(buildTree(node, nodes));
            }
        }
        return root;
    }

    /**
     * Set转树
     */
    public static <T extends Tree<T>> T buildTree(T root, java.util.Set<T> nodes) {
        if (root == null) {
            return null;
        }
        for (T node : nodes) {
            if (root.getId().equals(node.getParentId())) {
                root.getChildren().add(buildTree(node, nodes));
            }
        }
        return root;
    }
}
