package app.study.nick.com.nickstudydemo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import app.study.nick.com.nickstudydemo.BackHandleInterface;


/**
 * Created by yangjun1 on 2016/5/10.
 */
public abstract class BaseFragment extends Fragment  {
    private static final String TAG = BaseFragment.class.getSimpleName();
    protected BackHandleInterface mBackHandledInterface;
    /**
     * 所有继承BaseFragment的子类都将在这个方法中实现物理Back键按下后的逻辑
     * FragmentActivity捕捉到物理返回键点击事件后会首先询问Fragment是否消费该事件
     * 如果没有Fragment消费时FragmentActivity自己才会消费该事件
     */
    public abstract boolean onBackPressed();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG,"################ "+ this.getClass().getSimpleName() +"   onCreate()");
        if(!(getActivity() instanceof BackHandleInterface)){
            throw new ClassCastException("Hosting Activity must implement BackHandledInterface");
        }else{
            this.mBackHandledInterface = (BackHandleInterface)getActivity();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG,"################ "+ this.getClass().getSimpleName() +"   onStart()");
        //告诉FragmentActivity，当前Fragment在栈顶
        //TODO 这里是禁止让fragment的子fragment设置为当前fragment截获back按键，需要重新设置这里逻辑
        if(this.getParentFragment() == null) {
            mBackHandledInterface.setCurrentFragment(this);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //ViewPager fragmentPagerAdapter 会根据当前显示的页面设置这个
        if(isVisibleToUser) {
            mBackHandledInterface.setCurrentFragment(this);
        }
    }

    @Override
    public void onPause() {
        Log.e(TAG,"################ "+ this.getClass().getSimpleName() +"   onPause()");
        super.onPause();
    }

    public void finishFragment(){
        mBackHandledInterface.finishCurentFragment(this);
    }
}
