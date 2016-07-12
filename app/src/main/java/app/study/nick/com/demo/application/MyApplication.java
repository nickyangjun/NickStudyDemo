package app.study.nick.com.demo.application;

import android.app.Application;

import com.alipay.euler.andfix.patch.PatchManager;

/**
 * Created by yangjun1 on 2016/7/7.
 */
public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        PatchManager patchManager = new PatchManager(this);
        patchManager.init("1.0.0");
        patchManager.loadPatch();
    }
}
