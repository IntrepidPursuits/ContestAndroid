package io.intrepid.contest.testutils

import io.intrepid.contest.base.PresenterConfiguration
import io.intrepid.contest.rest.RestApi
import io.intrepid.contest.settings.PersistentSettings
import io.reactivex.schedulers.TestScheduler
import org.mockito.Mockito

class TestPresenterConfiguration(persistentSettings: PersistentSettings, restApi: RestApi) :
        PresenterConfiguration(TestScheduler(), TestScheduler(), persistentSettings, restApi) {

    override val ioScheduler: TestScheduler
        get() = super.ioScheduler as TestScheduler

    override val uiScheduler: TestScheduler
        get() = super.uiScheduler as TestScheduler

    /**
     * Helper method for triggering pending Rx events
     */
    fun triggerRxSchedulers() {
        ioScheduler.triggerActions()
        uiScheduler.triggerActions()
    }

    companion object {
        fun createTestConfiguration(): TestPresenterConfiguration {
            val mockApi = Mockito.mock(RestApi::class.java)
            val mockSettings = Mockito.mock(PersistentSettings::class.java)
            return TestPresenterConfiguration(
                    mockSettings,
                    mockApi
            )
        }
    }
}

