package io.intrepid.contest.screens.conteststatus;

import io.intrepid.contest.rest.ContestResponse;
import io.reactivex.functions.Consumer;

public interface ContestStatusActivityContract {
    void requestContestDetails(Consumer<ContestResponse> responseCallback, Consumer<Throwable> throwableCallback);
}
