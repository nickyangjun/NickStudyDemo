package app.study.nick.com.demo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.zip.Inflater;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    String[] titles = {"Android Support Design","Android Elevation","Activity Transition","Network Test"};
    String[] action = {"study.AndroidSupportDesign","study.AndroidElevation","study.ActivityTransition","study.NetworkTestActivity"};

    @Bind(R.id.select_list)
    ListView mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        mList.setAdapter(new Adapter());
    }

    @OnItemClick(R.id.select_list)
    void onItemClick(int position){
        startActivity(position);
    }

    private void startActivity(int position){
        Intent i = new Intent(action[position]);
        startActivity(i);
    }

    class Adapter extends BaseAdapter{
        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_select,parent,false);
                convertView.setTag(new ViewHolder(convertView));
            }
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.mTextView.setText(titles[position]);
            return convertView;
        }

    }
    class ViewHolder{
        @Bind(R.id.name)
        TextView mTextView;
        ViewHolder(View item){
           ButterKnife.bind(this,item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
