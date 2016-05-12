package app.study.nick.com.nickstudydemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import app.study.nick.com.nickstudydemo.fragment.SDAppBarLayoutFragment;
import app.study.nick.com.nickstudydemo.fragment.SDCollapsingToolbarLayoutFragment;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by yangjun1 on 2016/4/22.
 */
public class AndroidSupportDesign extends BaseActivity {
    String[] titles = {"Android Support Design  -- AppBarLayout","Android Support Design  -- CollapsingToolbarLayout"};
    @Bind(R.id.list_rv)
    RecyclerView mRecyclerView;
    @Bind(R.id.fragment_container)
    FrameLayout mFragmentsLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_design);
        ButterKnife.bind(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new RvAdapter());
    }

    private void startFragment(int position){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        Fragment f = null;
        String Tag = null;
        switch (position){
            case 0:
                f = SDAppBarLayoutFragment.getInstance();
                Tag = SDAppBarLayoutFragment.TAG;
                break;
            case 1:
                f = SDCollapsingToolbarLayoutFragment.getInstance();
                Tag = SDCollapsingToolbarLayoutFragment.TAG;
                break;
            default:
                return;
        }
        fragmentTransaction.add(R.id.fragment_container,f,Tag);
        fragmentTransaction.commit();
        mFragmentsLayout.setVisibility(View.VISIBLE);
    }

    class RvAdapter extends RecyclerView.Adapter{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view =  getLayoutInflater().inflate(R.layout.item_select,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ((ViewHolder)holder).name.setText(titles[position]);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   startFragment(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return titles.length;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.name)
        TextView name;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
