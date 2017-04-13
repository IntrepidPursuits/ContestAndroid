package io.intrepid.contest.screens.entrysubmission.cropimage

import android.graphics.Bitmap
import android.net.Uri
import io.intrepid.contest.screens.entrysubmission.cropimage.CropImageContract.View
import io.intrepid.contest.testutils.BasePresenterTest
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.Mockito.verify

class CropImagePresenterTest : BasePresenterTest<CropImagePresenter>() {
    @Mock
    private lateinit var mockView: View
    @Mock
    private lateinit var mockBitmap: Bitmap
    @Mock
    private lateinit var mockUri: Uri

    @Before
    fun setup() {
        presenter = CropImagePresenter(mockView, testConfiguration, " Test ")
    }

    @Test
    fun onViewBoundShouldCauseViewToShowCropFrame() {
        presenter.onViewBound()
        verify<View>(mockView).showCropTitle(anyString())
    }

    @Test
    fun onImageCroppedShouldCauseViewToDispatchCroppedImage() {
        presenter.onBitmapCropped(mockUri, 1, 1)
        verify<View>(mockView).dispatchCroppedImage(mockUri)
    }

    @Test
    fun onCancelCroplickedShouldCauseViewToCancelCrop() {
        presenter.onCancelCropClicked()
        verify<View>(mockView).cancelCrop()
    }

    @Test
    fun onCropOkButtonClicked() {
        presenter.onCropOkButtonClicked()
        verify<View>(mockView).performCrop()
    }

    @Test
    fun onCropFailureShouldCaueViewToShowMessage() {
        presenter.onCropFailure(any(Throwable::class.java))
        verify<View>(mockView).showMessage(anyInt())
    }
}
