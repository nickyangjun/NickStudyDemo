package app.study.nick.com.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;

import app.study.nick.com.demo.view.LabelImageView;
import app.study.nick.com.demo.view.LabelView;

/**
 * Created by yangjun1 on 2016/8/11.
 */
public class LabelActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label);

        LabelView.LabelData labelData = new LabelView.LabelData();
        labelData.brand = "爱的达斯";
        labelData.describe = "这是我滴最爱!!!!...";
        labelData.price = "66787.000元";

        LabelImageView labelImageView = (LabelImageView) findViewById(R.id.labelImageView);
        labelImageView.addLabel(labelData);
    }
}
