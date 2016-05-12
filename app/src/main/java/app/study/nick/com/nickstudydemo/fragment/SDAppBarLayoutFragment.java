package app.study.nick.com.nickstudydemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.study.nick.com.nickstudydemo.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yangjun1 on 2016/5/3.
 */
public class SDAppBarLayoutFragment extends BaseFragment {
    public static String TAG = SDAppBarLayoutFragment.class.getSimpleName();

    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.display_fab)
    TextView mDisplayFab;
    @Bind(R.id.hide_fab)
    TextView mHideFab;
    @Bind(R.id.list_rv)
    RecyclerView mRecyclerView;

    public static SDAppBarLayoutFragment getInstance() {
        SDAppBarLayoutFragment f = new SDAppBarLayoutFragment();
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_support_design_appbarlayout, container, false);
        ButterKnife.bind(this, v);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new RvAdapter());
        return v;
    }

    @OnClick({R.id.fab, R.id.display_fab, R.id.hide_fab})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Snackbar.make(v, "hello, I am SnackBar !!!!", Snackbar.LENGTH_LONG).setAction("delete", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
                break;
            case R.id.display_fab:
                mFab.show();
                break;
            case R.id.hide_fab:
                mFab.hide();
                break;
        }
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

    class RvAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.item_list, parent, false);
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

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
