package com.garfield.weishu.developer.datastructure.tree;

/**
 * Created by gaowei on 2017/5/22.
 */

public class PreOrderTraverse extends BaseTraverse {

    @Override
    void doTraverse(TreeNode p) {
        preOrder(p);
    }

    private void preOrder(TreeNode p) {
        if (p != null) {
            visit(p);
            preOrder(p.lChild);
            preOrder(p.rChild);
        }
    }


}
