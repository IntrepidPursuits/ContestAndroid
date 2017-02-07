package io.intrepid.contest.utils;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.base.BaseFragment;

public class SlidingTabAdapter extends FragmentStatePagerAdapter {
    private List<BaseFragment> fragments = new ArrayList<>();

    public SlidingTabAdapter(AppCompatActivity activity) {
        super(activity.getSupportFragmentManager());
    }

    @Override
    public BaseFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BaseFragment fragment = (BaseFragment) super.instantiateItem(container, position);
        fragments.set(position, fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(int index, BaseFragment fragment) {
        if(fragments.contains(fragment)){
            fragments.remove(fragment);
            notifyDataSetChanged();
        }
        fragments.add(index, fragment);
        notifyDataSetChanged();
    }

    public void addFragment(BaseFragment fragment) {
        fragments.add(fragment);
        notifyDataSetChanged();
    }

    public void clear() {
        fragments.clear();
    }

}
