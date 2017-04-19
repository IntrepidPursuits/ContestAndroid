package io.intrepid.contest.screens.contestcreation.reviewcontest;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseFragment;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.screens.contestcreation.ContestCreationFragment;
import io.intrepid.contest.screens.contestcreation.EditContestContract;
import io.intrepid.contest.screens.contestcreation.categorieslist.CategoryAdapter;

public class ReviewContestFragment extends BaseFragment<ReviewContestPresenter, ReviewContestContract.View>
        implements ReviewContestContract.View, ContestCreationFragment {
    @BindView(R.id.contest_title_button)
    TextView titleButton;
    @BindView(R.id.contest_description_button)
    TextView descriptionButton;
    @BindView(R.id.categories_recycler_view)
    RecyclerView categoriesRecyclerView;
    private CategoryAdapter categoryAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_review_contest;
    }

    @Override
    protected void onViewCreated(@Nullable Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        categoryAdapter = new CategoryAdapter(R.layout.category_overview_row_item, null);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        categoriesRecyclerView.setAdapter(categoryAdapter);
    }

    @NonNull
    @Override
    public ReviewContestPresenter createPresenter(PresenterConfiguration configuration) {
        Contest.Builder contestBuilder = ((EditContestContract) getActivity()).getContestBuilder();
        return new ReviewContestPresenter(this, configuration, contestBuilder);
    }

    @Override
    public void displayReviewPageContent(Contest.Builder contestBuilder) {
        titleButton.setText(contestBuilder.title);
        descriptionButton.setText(contestBuilder.description);
        categoryAdapter.setCategories(contestBuilder.getCategories());
    }

    @Override
    public void showEditTitlePage(Contest.Builder contestBuilder) {
        //todo
    }

    @Override
    public void showEditDescriptionPage(Contest.Builder contestBuilder) {
        //todo
    }

    @OnClick(R.id.contest_title_button)
    public void onContestTitleSelected() {
        getPresenter().onContestTitleSelected();
    }

    @OnClick(R.id.contest_description_button)
    public void onContestDescriptionSelected() {
        getPresenter().onContestDescriptionSelected();
    }

    @Override
    public void onNextClicked() {
        ((EditContestContract) getActivity()).showNextScreen();
    }

    public void onPageSelected() {
        // The presenter can be null if the view pager was reset and did not load this fragment in advance.
        if (getPresenter() != null) {
            getPresenter().onPageSelected();
        }
    }
}
