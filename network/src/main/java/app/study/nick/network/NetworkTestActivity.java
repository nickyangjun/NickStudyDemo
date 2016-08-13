package app.study.nick.network;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


public class NetworkTestActivity extends Activity {

    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_test);

        if(getDebugValue()){
            Toast.makeText(this," is Debug version !!!",Toast.LENGTH_LONG).show();
        }

        if(testAndFix()){
            Toast.makeText(this,"you are success test android fix @@@@@@@@@@@@@ !!!",Toast.LENGTH_LONG).show();
        }
        img = (ImageView) findViewById(R.id.imgView);
        Glide.with(this).load("http://img14.360buyimg.com/da/jfs/t2776/61/2855395343/27826/15e3a27e/57762f73N72e59b5f.jpg").into(img);
        MyLooperThread myLooperThread = new MyLooperThread();
        myLooperThread.start();
        Message msg = new Message();
        msg.what = 1;
        myLooperThread.sendMessage(msg);
    }

    private boolean getDebugValue(){
        return true;
    }

    private boolean testAndFix(){
        return true;
    }

    class MyLooperThread extends Thread{
        private final String TAG = "MyLooperThread";
        private MyHandler mHandler;
        public MyLooperThread(){
            Log.i(TAG,"MyLooperThread-----------"+Thread.currentThread().getId());
        }
        @Override
        public void run() {
            super.run();
            Log.i(TAG,"run-----------"+Thread.currentThread().getId());
            Looper.prepare();
            mHandler = new MyHandler(Looper.myLooper());
            Looper.loop();
        }

        public void sendMessage(Message msg){
            Log.i(TAG,"sendMessage -----------"+Thread.currentThread().getId());
            while (mHandler == null){
                try {
                    sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            mHandler.sendMessage(msg);
        }
        class MyHandler extends Handler{
            MyHandler(Looper looper){
                super(looper);
                Log.i(TAG,"MyHandler -----------"+Thread.currentThread().getId());
            }
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        Log.i(TAG,"handleMessage -----------"+Thread.currentThread().getId());
                        Log.i(TAG,"1111111111111");
                        break;
                    default:
                }
            }
        }
    }
}
