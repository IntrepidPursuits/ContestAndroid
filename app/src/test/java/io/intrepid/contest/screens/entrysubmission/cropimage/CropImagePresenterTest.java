package io.intrepid.contest.screens.entrysubmission.cropimage;


import android.graphics.Bitmap;
import android.net.Uri;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import io.intrepid.contest.testutils.BasePresenterTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

public class CropImagePresenterTest extends BasePresenterTest<CropImagePresenter> {
    @Mock
    CropImageContract.View mockView;
    @Mock
    Bitmap mockBitmap;
    @Mock
    Uri mockuri;

    @Before
    public void setup() {
        presenter = new CropImagePresenter(mockView, testConfiguration, " Test ");
    }

    @Test
    public void onViewBoundShouldCauseViewToShowCropFrame() {
        presenter.onViewBound();
        verify(mockView).showCropTitle(anyString());
    }

    @Test
    public void onImageCroppedShouldCauseViewToDispatchCroppedImage(){
        presenter.onBitmapCropped(mockuri, 1, 1);
        verify(mockView).dispatchCroppedImage(mockuri);
    }

    @Test
    public void onCancelCroplickedShouldCauseViewToCancelCrop() {
        presenter.onCancelCropClicked();
        verify(mockView).cancelCrop();
    }

    @Test
    public void onCropOkButtonClicked() {
        presenter.onCropOkButtonClicked();
        verify(mockView).performCrop();
    }

    @Test
    public void onCropFailureShouldCaueViewToShowMessage() {
        presenter.onCropFailure(any(Throwable.class));
        verify(mockView).showMessage(anyInt());
    }
}
