package app.study.nick.network;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class NetworkTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_test);

        if(BuildConfig.DEBUG){
            Toast.makeText(this," is Debug version !!!",Toast.LENGTH_LONG).show();
        }
    }
}
