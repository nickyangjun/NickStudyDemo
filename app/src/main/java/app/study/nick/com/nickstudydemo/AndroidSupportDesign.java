package app.study.nick.com.nickstudydemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yangjun1 on 2016/4/22.
 */
public class AndroidSupportDesign extends AppCompatActivity {
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.display_fab)
    TextView mDisplayFab;
    @Bind(R.id.hide_fab)
    TextView mHideFab;
    @Bind(R.id.list_rv)
    RecyclerView mRecyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_design);
        ButterKnife.bind(this);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"hello, I am SnackBar !!!!",Snackbar.LENGTH_LONG).setAction("delete", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();

            }
        });

        mDisplayFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFab.show();
            }
        });

        mHideFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFab.hide();
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new RvAdapter());
    }

    class RvAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view =  getLayoutInflater().inflate(R.layout.item_list,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 30;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
