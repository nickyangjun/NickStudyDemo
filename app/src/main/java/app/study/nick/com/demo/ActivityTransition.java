package app.study.nick.com.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yangjun1 on 2016/5/16.
 */
public class ActivityTransition extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.transition_explode,R.id.transition_slide,R.id.transition_fade,R.id.transition_share})
    void onClick(View v){
        switch (v.getId()){
            case R.id.transition_explode:
                startActivity(SecondActivity.TransitionType.EXPLODE);
                break;
            case R.id.transition_slide:
                startActivity(SecondActivity.TransitionType.SLIDE);
                break;
            case R.id.transition_fade:
                startActivity(SecondActivity.TransitionType.FADE);
                break;
            case R.id.transition_share:
                startActivity(SecondActivity.TransitionType.SHARE,v,"shareName");
                break;
        }
    }

    void startActivity(SecondActivity.TransitionType type){
        SecondActivity.TRANSITION_TYPE = type;
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this);
        Intent intent = new Intent(this, SecondActivity.class);
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }
    void startActivity(SecondActivity.TransitionType type,View v,String url){
        SecondActivity.TRANSITION_TYPE = type;
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,v,url);
        Intent intent = new Intent(this, SecondActivity.class);
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }
}
