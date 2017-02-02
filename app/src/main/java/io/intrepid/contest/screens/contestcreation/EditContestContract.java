package io.intrepid.contest.screens.contestcreation;

import java.util.List;

import io.intrepid.contest.models.Category;

public interface EditContestContract {

    void setContestName(String contestName);

    void setCategories(List<Category> categories);

    void setContestDescription(String description);

    void setNextEnabled(boolean enabled);

    void showAddCategoryScreen();

    List<Category> getCategories();
}
