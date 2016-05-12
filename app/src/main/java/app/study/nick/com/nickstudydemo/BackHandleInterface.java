package app.study.nick.com.nickstudydemo;

import app.study.nick.com.nickstudydemo.fragment.BaseFragment;

/**
 * Created by yangjun1 on 2016/5/10.
 */
public interface BackHandleInterface {
    public abstract void setCurrentFragment(BaseFragment currentFragment);
    public abstract void finishCurentFragment();
}
