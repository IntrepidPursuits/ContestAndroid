package io.intrepid.contest.screens.conteststatus;

import io.intrepid.contest.rest.ContestWrapper;
import io.reactivex.functions.Consumer;

public interface ContestStatusActivityContract {
    void requestContestDetails(Consumer<ContestWrapper> responseCallback, Consumer<Throwable> throwableCallback);
}
