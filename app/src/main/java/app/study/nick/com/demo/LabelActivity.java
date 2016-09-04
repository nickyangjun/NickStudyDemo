package app.study.nick.com.demo;

import android.annotation.TargetApi;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;

import app.study.nick.com.demo.view.LabelData;
import app.study.nick.com.demo.view.LabelImageView;
import app.study.nick.com.demo.view.LabelView;

/**
 * Created by yangjun1 on 2016/8/11.
 */
public class LabelActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = LabelActivity.class.getSimpleName();
    LabelImageView mLabelImageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label);
        Log.e(TAG,"-----------  onCreate -------------");

        ArrayList<LabelData> list = new ArrayList<>();
        LabelData labelData = new LabelData();
        labelData.describe = "游艇!!!!...";
        labelData.price = "66666666";
        labelData.setPosition(0.56f,0.3f);

        LabelData labelData2 = new LabelData();
        labelData2.describe = "美女!!!!...";
        labelData2.price = "88888";
        labelData2.setPosition(0.5f,0.4f);

        list.add(labelData);
        list.add(labelData2);

        mLabelImageView = (LabelImageView) findViewById(R.id.labelImageView);
        mLabelImageView.addLabel(list);

        findViewById(R.id.change_position).setOnClickListener(this);
        findViewById(R.id.change_size).setOnClickListener(this);

        //手动调用测量方法。 制定测量规则 参数表示size + mode
        int width =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int height =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        mLabelImageView.measure(width,height);
        //调用measure方法之后就可以获取宽高。
        height=mLabelImageView.getMeasuredHeight();
        width=mLabelImageView.getMeasuredWidth();
        Log.e(TAG,"-----------  measure  width: "+ width + "  height: " + height);

        mLabelImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                mLabelImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int h = mLabelImageView.getHeight();
                int w = mLabelImageView.getWidth();
                Log.e(TAG,"-----------  onGlobalLayout  width: "+ w + "  height: " + h);
            }
        });

        mLabelImageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mLabelImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                int h = mLabelImageView.getHeight();
                int w = mLabelImageView.getWidth();
                Log.e(TAG,"-----------  onPreDraw  width: "+ w + "  height: " + h);
                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG,"-----------  onStart -------------");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG,"-----------  onResume -------------");
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e(TAG,"-----------  onAttachedToWindow -------------");
    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.change_position){
//            mLabelImageView.offsetTopAndBottom(100);
            mLabelImageView.setLeft(200);
        }else if(id == R.id.change_size){
            mLabelImageView.getLayoutParams().height = 800;
            mLabelImageView.getLayoutParams().width = 800;
            mLabelImageView.setLayoutParams(mLabelImageView.getLayoutParams());
//            mLabelImageView.animate().scaleX(0.5f).start();
        }
    }
}
