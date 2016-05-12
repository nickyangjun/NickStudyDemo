package app.study.nick.com.nickstudydemo;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import app.study.nick.com.nickstudydemo.fragment.BaseFragment;

/**
 * Created by yangjun1 on 2016/5/10.
 */
public class BaseActivity extends AppCompatActivity implements BackHandleInterface  {
    private BaseFragment mBaseFragment;
    @Override
    public void onBackPressed() {
        if(mBaseFragment!=null){
            BaseFragment f = mBaseFragment;
            while (!f.onBackPressed()){ //如果当前fragment不对back键处理则把处理权交给它的父控件
                BaseFragment parent = (BaseFragment) f.getParentFragment();
                if(parent != null){
                    f = parent;
                }else {
                    break;
                }
            }
            if(f.onBackPressed()){
                FragmentManager manager = f.getFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                ft.remove(f);
                ft.commit();
                mBaseFragment = null;
            }

//            if(!mBaseFragment.onBackPressed()){
//                FragmentManager manager = mBaseFragment.getFragmentManager();
//                FragmentTransaction ft = manager.beginTransaction();
//                ft.remove(mBaseFragment);
//                ft.commit();
//                mBaseFragment = null;
//            }
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void setCurrentFragment(BaseFragment currentFragment) {
        mBaseFragment = currentFragment;
    }

    @Override
    public void finishCurentFragment() {
        FragmentManager manager = mBaseFragment.getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.remove(mBaseFragment);
        ft.commit();
        mBaseFragment = null;
    }

}
