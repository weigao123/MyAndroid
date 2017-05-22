package com.garfield.weishu.developer.datastructure.tree;

/**
 * Created by gaowei on 2017/5/22.
 */

public class TreeNode {
    public char data;
    public TreeNode lChild;
    public TreeNode rChild;

    public TreeNode(char key) {
        this(key, null, null);
    }

    public TreeNode(char data, TreeNode lChild, TreeNode rChild) {
        this.data = data;
        this.lChild = lChild;
        this.rChild = rChild;
    }
}
