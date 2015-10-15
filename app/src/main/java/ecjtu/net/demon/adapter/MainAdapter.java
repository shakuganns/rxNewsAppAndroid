package ecjtu.net.demon.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by homker on 2015/5/3.
 * 日新网新闻客户端
 */
public class MainAdapter extends FragmentPagerAdapter {

    private String[] titles = {"日新新闻", "学院专题", "日新图说"};
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private FragmentTransaction mCurTransaction = null;
    private FragmentManager mFragmentManager;
    private boolean[] isInit = {false,true,true,true};
    private ViewGroup viewGroup;

    public MainAdapter(FragmentManager fragmentManager, ArrayList<Fragment> fragments) {
        super(fragmentManager);
        mFragmentManager =fragmentManager;
        this.fragments = fragments;
    }

    public void setFragment(int index,Fragment fragment) {
        fragments.set(index, fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
