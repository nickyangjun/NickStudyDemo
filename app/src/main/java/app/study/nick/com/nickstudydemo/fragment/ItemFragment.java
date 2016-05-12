package app.study.nick.com.nickstudydemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.study.nick.com.nickstudydemo.R;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yangjun1 on 2016/5/11.
 */
public class ItemFragment extends BaseFragment {
    private static final String TAG = ItemFragment.class.getSimpleName();
    private int id=-1;


    @Bind(R.id.list_rv)
    RecyclerView mRecycleView;

    public static ItemFragment getInstance(int id) {
        ItemFragment f = new ItemFragment();
        Bundle b = new Bundle();
        b.putInt("id",id);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        id = b.getInt("id");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_item, container, false);
        ButterKnife.bind(this, v);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycleView.setAdapter(new RvAdapter());
        return v;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e(TAG," --------TabFragment"+ id +" isVisibleToUser "+ isVisibleToUser);
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
