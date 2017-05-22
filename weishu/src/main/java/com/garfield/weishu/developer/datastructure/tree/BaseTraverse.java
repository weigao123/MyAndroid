package com.garfield.weishu.developer.datastructure.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaowei on 2017/5/22.
 */

public abstract class BaseTraverse {

    private List<Character> result = new ArrayList<>();

    abstract void doTraverse(TreeNode treeNode);

    public void traverse() {
        result.clear();
        doTraverse(init());
    }

    private TreeNode init() {
        TreeNode g = new TreeNode('G');
        TreeNode h = new TreeNode('H');
        TreeNode i = new TreeNode('I');
        TreeNode d = new TreeNode('D', g, h);
        TreeNode e = new TreeNode('E', null, i);
        TreeNode f = new TreeNode('F');
        TreeNode b = new TreeNode('B', d, null);
        TreeNode c = new TreeNode('C', e, f);
        TreeNode a = new TreeNode('A', b, c);
        return a;
    }

    void visit(TreeNode p) {
        result.add(p.data);
        System.out.print(p.data + " ");
    }

    public List<Character> getResult() {
        return result;
    }
}
