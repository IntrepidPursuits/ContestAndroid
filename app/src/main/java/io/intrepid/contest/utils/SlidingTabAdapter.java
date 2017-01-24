package io.intrepid.contest.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.intrepid.contest.BaseSlideFragment;
import timber.log.Timber;

public class SlidingTabAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener{
    private final AppCompatActivity activity;
    private int currentIndex = 0;
    private ArrayList<BaseSlideFragment> fragments = new ArrayList<>();

    public SlidingTabAdapter(AppCompatActivity activity) {
        super(activity.getSupportFragmentManager());
        this.activity = activity;
    }

    @Override
    public BaseSlideFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BaseSlideFragment fragment = (BaseSlideFragment) super.instantiateItem(container, position);
        fragments.set(position, fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(BaseSlideFragment fragment) {
        fragments.add(getCount(), fragment);
        notifyDataSetChanged();
    }

    public int getLastItemPosition() {
        return getCount() - 1;
    }

    public boolean isLastSlide(int position) {
        return position == getCount() - 1;
    }

    public boolean shouldFinish(int position) {
        return position == getCount() && getItem(getCount() - 1).canMoveFurther();
    }

    public boolean canAdvance() {
        BaseSlideFragment fragment = getItem(currentIndex);
        return !fragment.canMoveFurther();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        currentIndex = position;
        Timber.d("Current index " + currentIndex);
    }

    @Override
    public void onPageSelected(int position) {
        currentIndex = position;
        Timber.d("Current index " + currentIndex);
        switch (currentIndex){
            case 0: activity.getSupportActionBar().setTitle("New Title");
                break;
            case 1: activity.getSupportActionBar().setTitle("Description");
                break;
            case 2: activity.getSupportActionBar().setTitle("");
                break;
            case 3: activity.getSupportActionBar().setTitle("Scoring Categories");
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public int advance() {
        onPageSelected(++currentIndex);
        return currentIndex;
    }

    public Fragment getCurrentFragment() {
        return fragments.get(currentIndex);
    }

    public int revertToLastPage() {
        onPageSelected(--currentIndex);
        return currentIndex;
    }
}
