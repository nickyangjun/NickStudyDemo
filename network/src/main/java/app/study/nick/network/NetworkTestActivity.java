package app.study.nick.network;

import android.app.Activity;
import android.os.Bundle;
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
    }

    private boolean getDebugValue(){
        return true;
    }

    private boolean testAndFix(){
        return true;
    }
}
