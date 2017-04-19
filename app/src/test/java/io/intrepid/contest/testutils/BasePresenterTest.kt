package io.intrepid.contest.testutils

import io.intrepid.contest.base.BasePresenter
import io.intrepid.contest.rest.RestApi
import io.intrepid.contest.settings.PersistentSettings
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit

open class BasePresenterTest<P : BasePresenter<*>> {
    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()

    protected lateinit var presenter: P
    protected lateinit var testConfiguration: TestPresenterConfiguration
    protected lateinit var mockRestApi: RestApi
    protected lateinit var mockPersistentSettings: PersistentSettings
    protected lateinit var ioScheduler: TestScheduler
    protected lateinit var uiScheduler: TestScheduler

    /**
     * Extension Function to allow generics when mocking java classes
     */
    protected inline fun <reified T : Any> mock(): T = Mockito.mock(T::class.java)

    @Before
    fun baseSetup() {
        testConfiguration = TestPresenterConfiguration.createTestConfiguration()
        ioScheduler = testConfiguration.ioScheduler
        uiScheduler = testConfiguration.uiScheduler
        mockRestApi = testConfiguration.restApi
        mockPersistentSettings = testConfiguration.persistentSettings
    }
}
