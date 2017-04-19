package io.intrepid.contest.screens.entrysubmission.entryimage

import android.net.Uri
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.intrepid.contest.models.Entry
import io.intrepid.contest.rest.EntryRequest
import io.intrepid.contest.rest.EntryResponse
import io.intrepid.contest.screens.entrysubmission.entryimage.EntryImageContract.View
import io.intrepid.contest.testutils.BasePresenterTest
import io.reactivex.Observable
import io.reactivex.Observable.error
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.never
import org.mockito.Mockito.reset
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class EntryImagePresenterTest : BasePresenterTest<EntryImagePresenter>() {
    @Mock
    private lateinit var mockView: View
    @Mock
    private lateinit var mockUri: Uri

    private lateinit var throwable: Throwable
    private lateinit var entryName: String

    @Before
    fun setup() {
        entryName = "Entry Name"
        `when`(mockView.entryName).thenReturn(entryName)

        throwable = Throwable()
        presenter = EntryImagePresenter(mockView, testConfiguration)
    }

    @Test
    fun onBitmapRemovedShouldDisplayChooseImageLayout() {
        presenter.onViewCreated()
        reset<View>(mockView)

        presenter.onBitmapRemoved()

        verify<View>(mockView).displayChooseImageLayout(entryName)
    }

    @Test
    fun onViewCreatedShouldDisplayChooseImageLayoutWhenBitmapsHaveNeverBeenReceived() {
        presenter.onViewCreated()
        verify<View>(mockView).displayChooseImageLayout(entryName)
    }

    @Test
    fun bindingViewShouldStartCropImageLayoutWhenBitmapWasReceived() {
        presenter.onImageReceived(mockUri)
        presenter.onViewBound()
        verify<View>(mockView).startCropImage(any<String>(), eq<Uri>(mockUri))
    }

    @Test
    fun bindingViewShouldDisplayChooseImageLayoutWhenBitmapWasRemoved() {
        presenter.onViewCreated()
        presenter.onBitmapRemoved()
        reset<View>(mockView)

        presenter.onViewBound()

        verify<View>(mockView).displayChooseImageLayout(entryName)
    }

    @Test
    fun onCameraButtonClickedShouldDispatchTakePictureIntent() {
        presenter.onCameraButtonClicked()
        verify<View>(mockView).dispatchTakePictureIntent()
    }

    @Test
    fun onGalleryButtonClickedShouldDispatchChoosePictureIntent() {
        presenter.onGalleryButtonClicked()
        verify<View>(mockView).dispatchChoosePictureIntent()
    }

    @Test
    fun onEntrySubmittedShouldCreateBitmapWhenImageHasBeenSelectedAndCropped() {
        `when`(mockPersistentSettings.currentContestId).thenReturn(UUID.randomUUID())
        `when`(mockRestApi.createEntry(any<String>(), any<EntryRequest>())).thenReturn(error<EntryResponse>(throwable))
        presenter.onImageCropped(mockUri)

        presenter.onEntrySubmitted()

        verify<View>(mockView).makeBitmap(mockUri)
    }

    @Test
    fun onEntrySubmittedShouldNotShowErrorMessageWhenApiResponseIsValid() {
        `when`(mockPersistentSettings.currentContestId).thenReturn(UUID.randomUUID())
        val validEntry = Entry().apply {
            id = UUID.randomUUID()
        }
        val entryResponse = EntryResponse().apply {
            entry = validEntry
        }
        `when`(mockRestApi.createEntry(any<String>(), any<EntryRequest>())).thenReturn(Observable.just(entryResponse))

        presenter.onEntrySubmitted()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView, never()).showInvalidEntryErrorMessage()
    }

    @Test
    fun onEntrySubmittedShouldShowInvalidEntryErrorMessageWhenApiResponseIsInvalid() {
        `when`(mockPersistentSettings.currentContestId).thenReturn(UUID.randomUUID())
        `when`(mockRestApi.createEntry(any<String>(),
                any<EntryRequest>())).thenReturn(Observable.just(EntryResponse()))

        presenter.onEntrySubmitted()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showInvalidEntryErrorMessage()
    }

    @Test
    @Throws(HttpException::class)
    fun onEntrySubmittedShouldShowApiErrorMessageWhenApiCallThrowsError() {
        `when`(mockPersistentSettings.currentContestId).thenReturn(UUID.randomUUID())
        `when`(mockRestApi.createEntry(any<String>(), any<EntryRequest>())).thenReturn(error<EntryResponse>(throwable))
        presenter.onEntrySubmitted()
        testConfiguration.triggerRxSchedulers()

        verify<View>(mockView).showMessage(any(Int::class.javaPrimitiveType!!))
    }

    @Test
    fun onStoragePermissionCheckFalseShouldCauseViewToRequestPermissions() {
        presenter.onStoragePermissionCheck(false)
        verify<View>(mockView).requestStoragePermissions()
    }

    @Test
    fun onImageCroppedShouldCauseViewToPreviewCroppedImageAfterNextViewBind() {
        presenter.onViewCreated()
        presenter.onImageCropped(mockUri)

        presenter.onViewBound()

        verify<View>(mockView).displayPreviewImageLayout(entryName, mockUri)
    }

    @Test
    fun onImageCroppedWithNullParamsShouldCauseViewToShowErrorMsg() {
        presenter.onImageCropped(null)
        verify<View>(mockView).showMessage(anyInt())
    }
}
