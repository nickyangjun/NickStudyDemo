package app.study.nick.com.demo;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yangjun1 on 2016/5/16.
 */
public class SecondActivity extends BaseActivity {
    public static TransitionType TRANSITION_TYPE = TransitionType.EXPLODE;
    public enum TransitionType{
        EXPLODE,SLIDE,FADE,SHARE
    }

    @Bind(R.id.text)
    TextView text;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 允许使用transitions
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        // 设置一个exit transition
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            switch (TRANSITION_TYPE) {
                case EXPLODE:
                    getWindow().setExitTransition(new Explode());
                    getWindow().setEnterTransition(new Explode());
                    break;
                case SLIDE:
                    getWindow().setEnterTransition(new Slide());
                    getWindow().setExitTransition(new Slide());
                    break;
                case FADE:
                    getWindow().setEnterTransition(new Fade());
                    getWindow().setExitTransition(new Fade());
                    break;
                case SHARE:
//                    getWindow().setSharedElementEnterTransition(new ChangeClipBounds());
//                    getWindow().setSharedElementExitTransition(new ChangeBounds());
                    break;
            }
        }
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);
        ViewCompat.setTransitionName(text,"shareName");
    }

    @OnClick(R.id.back)
    void onClick(View v){
        ActivityCompat.finishAfterTransition(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAfterTransition(this);;
    }
}
