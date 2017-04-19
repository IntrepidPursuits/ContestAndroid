package io.intrepid.contest.base

import io.intrepid.contest.rest.RestApi
import io.intrepid.contest.settings.PersistentSettings
import io.reactivex.Scheduler

/**
 * Wrapper class for common dependencies that all presenters are expected to have
 */
open class PresenterConfiguration(open val ioScheduler: Scheduler,
                                  open val uiScheduler: Scheduler,
                                  val persistentSettings: PersistentSettings,
                                  val restApi: RestApi)
