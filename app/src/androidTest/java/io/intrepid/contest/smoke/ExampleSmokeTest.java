package io.intrepid.contest.smoke;

import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import io.intrepid.contest.InstrumentationTestApplication;
import io.intrepid.contest.R;
import io.intrepid.contest.rest.TestRestClient;
import io.intrepid.contest.rules.MockServerRule;
import io.intrepid.contest.screens.example1.Example1Activity;
import io.intrepid.contest.settings.UserSettings;
import io.intrepid.contest.testutils.BaseUiTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.when;

@SmallTest
public class ExampleSmokeTest extends BaseUiTest {

    @Rule
    public MockServerRule mockServerRule = new MockServerRule();
    @Rule
    public ActivityTestRule<Example1Activity> activityTestRule = new ActivityTestRule<>(Example1Activity.class, true, false);

    @Mock
    UserSettings mockUserSettings;

    @Before
    public void setUp() {
        InstrumentationTestApplication.overrideRestApi(TestRestClient.getRestApi(mockServerRule));
        InstrumentationTestApplication.overrideUserSettings(mockUserSettings);
    }

    @Test
    public void smokeTest() {
        activityTestRule.launchActivity(null);
        mockServerRule.enqueueResponse(io.intrepid.contest.debug.test.R.raw.mock_ip);

        when(mockUserSettings.getLastIp()).thenReturn("127.0.0.2");

        onView(withId(R.id.example1_button)).perform(click());
        onView(withId(R.id.example2_current_ip)).check(matches(withText("Your current Ip address is 127.0.0.1")));
        onView(withId(R.id.example2_previous_ip)).check(matches(withText("Your previous Ip address is 127.0.0.2")));
    }
}
