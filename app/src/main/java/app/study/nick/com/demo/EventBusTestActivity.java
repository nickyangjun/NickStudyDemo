package app.study.nick.com.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import app.study.nick.com.demo.events.EventNum;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yangjun1 on 2016/7/12.
 */
public class EventBusTestActivity extends BaseActivity {

    private CalcThread mCalcThread;
    private volatile int num;

    @Bind(R.id.num)
    TextView mNumTextView;
    @Bind(R.id.start_btn)
    Button mStartBtn;
    @Bind(R.id.manual_btn)
    Button mManualBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventbus_test);
        ButterKnife.bind(this);
        //注册
        EventBus.getDefault().register(this);
    }
    //这里接收启动它的activity传过来的值
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void getInitEventNum(EventNum eventNum){
        num = eventNum.getNum();
        mNumTextView.setText(eventNum.getNum()+"");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventNum(EventNum eventNum){
        mNumTextView.setText(eventNum.getNum()+"");
        EventBus.getDefault().cancelEventDelivery(eventNum);
    }

    @OnClick({R.id.start_btn})
    void onStartBtnClick(){
        if(mStartBtn.getText().toString().startsWith("start")){
            mStartBtn.setText("stop");
            if(mCalcThread == null) {
                mCalcThread = new CalcThread();
                mCalcThread.start();
            }
        }else {
            mStartBtn.setText("start");
            mCalcThread.stopThread();
        }
    }

    @OnClick({R.id.manual_btn})
    void onManualBtnClick(){
        EventBus.getDefault().post(new EventNum(1000));
    }

    class CalcThread extends Thread{
        private volatile boolean stop = false;

        @Override
        public synchronized void start() {
            stop = false;
            super.start();
        }

        public void stopThread(){
            stop = true;
            interrupt();
        }

        @Override
        public void run() {
            super.run();
            while (!stop){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                num ++;
                EventBus.getDefault().post(new EventNum(num));
            }
            mCalcThread = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }
}
