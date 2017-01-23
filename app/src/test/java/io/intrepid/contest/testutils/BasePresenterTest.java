package io.intrepid.contest.testutils;

import org.junit.Before;
import org.junit.Rule;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.rest.RestApi;

public class BasePresenterTest<P extends BasePresenter> {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    protected P presenter;
    protected TestPresenterConfiguration testConfiguration;
    protected RestApi mockRestApi;

    @Before
    public void baseSetup() {
        testConfiguration = TestPresenterConfiguration.createTestConfiguration();
        mockRestApi = testConfiguration.getRestApi();
    }
}
