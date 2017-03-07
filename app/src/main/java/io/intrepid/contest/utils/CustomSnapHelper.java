package io.intrepid.contest.utils;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class CustomSnapHelper extends PagerSnapHelper {
    private final OnPageSwipeListener listener;

    public CustomSnapHelper(OnPageSwipeListener listener) {
        this.listener = listener;
    }

    @Override
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) throws IllegalStateException {
        super.attachToRecyclerView(recyclerView);

        if (recyclerView != null && recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == SCROLL_STATE_IDLE) {
                        int currentPosition = layoutManager.findFirstVisibleItemPosition();
                        listener.onSnapViewSwiped(currentPosition);
                    }
                }
            });
        }
    }

    public interface OnPageSwipeListener {
        void onSnapViewSwiped(int currentIndex);
    }
}
