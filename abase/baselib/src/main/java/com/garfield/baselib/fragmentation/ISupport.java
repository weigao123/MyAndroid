package com.garfield.baselib.fragmentation;


/**
 * Created by YoKeyword on 16/6/1.
 */
public interface ISupport {

    int STANDARD = 1;
    int SINGLETOP = 2;
    int SINGLETASK = 3;

    /**
     * 加载根Fragment, 即Activity内的第一个Fragment 或 Fragment内的第一个子Fragment
     *
     * @param containerId 容器id
     * @param toFragment  目标Fragment
     */
    void loadRootFragment(int containerId, SupportFragment toFragment);

    /**
     * 以replace方式加载根Fragment
     */
    void replaceLoadRootFragment(int containerId, SupportFragment toFragment, boolean addToBack);

    /**
     * 加载多个根Fragment
     *
     * @param containerId 容器id
     * @param toFragments  目标Fragments
     */
    void loadMultiRootFragment(int containerId, int showPosition, SupportFragment... toFragments);

    /**
     * show一个Fragment,hide一个Fragment ; 主要用于类似微信主页那种 切换tab的情况
     *
     * @param showFragment 需要show的Fragment
     * @param hideFragment 需要hide的Fragment
     */
    void showHideFragment(SupportFragment showFragment, SupportFragment hideFragment);

    /**
     * 启动目标Fragment
     * 如果在Fragment里，from就是自己
     * 如果在Activity里，from是最上层的Fragment
     */
    void startFragment(SupportFragment toFragment);

    /**
     * @param toFragment 目标Fragment
     * @param launchMode 启动模式
     */
    void startFragment(SupportFragment toFragment, int launchMode);

    /**
     * 启动目标Fragment,并pop当前Fragment
     *
     * @param toFragment 目标Fragment
     */
    void startFragmentWithPop(SupportFragment toFragment);

    /**
     * @param toFragment  目标Fragment
     * @param requestCode requsetCode
     */
    void startFragmentForResult(SupportFragment toFragment, int requestCode);

    /**
     * @return 栈顶Fragment
     */
    SupportFragment getTopFragment();

    /**
     * @return 栈顶第二个Fragment
     */
    SupportFragment getPreOfTopFragment();

    /**
     * @param fragmentClass 目标Fragment的Class
     * @param <T>           继承自SupportFragment的Fragment
     * @return 目标Fragment
     */
    <T extends SupportFragment> T findFragment(Class<T> fragmentClass);

    /**
     * 出栈
     */
    void popFragment();

    void popChildFragment();

    /**
     * 出栈到目标Fragment
     *
     * @param fragmentClass 目标Fragment的Class
     * @param includeSelf   是否包含目标Fragment
     */
    void popToFragment(Class<?> fragmentClass, boolean includeSelf);

    /**
     * 出栈到目标Fragment,并在出栈后立即进行Fragment事务(可以防止出栈后,直接进行Fragment事务的异常)
     *
     * @param fragmentClass               目标Fragment的Class
     * @param includeSelf                 是否包含目标Fragment
     * @param afterPopTransactionRunnable 出栈后紧接着的Fragment事务
     */
    void popToFragment(Class<?> fragmentClass, boolean includeSelf, Runnable afterPopTransactionRunnable);
}
