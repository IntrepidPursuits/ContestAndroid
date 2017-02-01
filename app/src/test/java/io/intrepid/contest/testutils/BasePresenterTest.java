package io.intrepid.contest.testutils;


import org.junit.Before;
import org.junit.Rule;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.rest.RestApi;
import io.intrepid.contest.settings.PersistentSettings;
import io.reactivex.schedulers.TestScheduler;

public class BasePresenterTest<P extends BasePresenter> {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    protected P presenter;
    protected TestPresenterConfiguration testConfiguration;
    protected RestApi mockRestApi;
    protected PersistentSettings mockPersistentSettings;
    private TestScheduler ioScheduler;
    private TestScheduler uiScheduler;

    @Before
    public void baseSetup() {
        testConfiguration = TestPresenterConfiguration.createTestConfiguration();
        ioScheduler = testConfiguration.getIoScheduler();
        uiScheduler = testConfiguration.getUiScheduler();
        mockRestApi = testConfiguration.getRestApi();
        mockPersistentSettings = testConfiguration.getPersistentSettings();
    }
}
