package app.study.nick.com.demo.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import app.study.nick.com.demo.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yangjun1 on 2016/5/3.
 */
public class SDCollapsingToolbarLayoutFragment extends BaseFragment {
    public static String TAG = SDCollapsingToolbarLayoutFragment.class.getSimpleName();
    private List<BaseFragment> mFragmentsList = new ArrayList<>();

    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.collapsinglayout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tab)
    TabLayout mTab;
    @Bind(R.id.viewpager)
    ViewPager mViewPager;

    public static SDCollapsingToolbarLayoutFragment getInstance() {
        SDCollapsingToolbarLayoutFragment f = new SDCollapsingToolbarLayoutFragment();
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_support_design_collapsingtoolbarlayout, container, false);
        ButterKnife.bind(this, v);
        mCollapsingToolbarLayout.setTitle("Nick");

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.chrysan);

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(final Palette palette) {
                int defaultColor = getResources().getColor(R.color.colorPrimary);
                int defaultTitleColor = getResources().getColor(R.color.white);
                int bgColor = palette.getDarkVibrantColor(defaultColor);
                int titleColor = palette.getLightVibrantColor(defaultTitleColor);
                mCollapsingToolbarLayout.setContentScrimColor(bgColor);
                mCollapsingToolbarLayout.setCollapsedTitleTextColor(titleColor);
                mCollapsingToolbarLayout.setExpandedTitleColor(titleColor);
            }
        });

        mToolbar.setNavigationIcon(R.drawable.goback);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishFragment();
            }
        });

        mFragmentsList.add(ItemFragment.getInstance(0));
        mFragmentsList.add(ItemFragment.getInstance(1));
        mFragmentsList.add(ItemFragment.getInstance(2));
        mFragmentsList.add(ItemFragment.getInstance(3));
        mFragmentsList.add(ItemFragment.getInstance(4));
        mFragmentsList.add(ItemFragment.getInstance(5));
        mFragmentsList.add(ItemFragment.getInstance(6));

        mViewPager.setAdapter(new FargmentAdapter(getChildFragmentManager()));
        mTab.setupWithViewPager(mViewPager);
        return v;
    }

    @OnClick(R.id.fab)
    void onClick(View v) {
        Snackbar.make(v, "hello, I am SnackBar !!!!", Snackbar.LENGTH_LONG).setAction("delete", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

    class FargmentAdapter extends FragmentPagerAdapter{

        public FargmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentsList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Tab"+position;
        }

    }
}
