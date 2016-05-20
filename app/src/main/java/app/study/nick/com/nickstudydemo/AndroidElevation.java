package app.study.nick.com.nickstudydemo;

import android.animation.StateListAnimator;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yangjun1 on 2016/5/13.
 */
public class AndroidElevation extends BaseActivity {

    @Bind(R.id.text3)
    TextView mTextView3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elevation);
        ButterKnife.bind(this);
        mTextView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
