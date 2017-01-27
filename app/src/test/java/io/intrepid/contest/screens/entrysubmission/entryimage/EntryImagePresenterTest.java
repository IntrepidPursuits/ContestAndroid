package io.intrepid.contest.screens.entrysubmission.entryimage;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import io.intrepid.contest.screens.entrysubmission.entryimage.EntryImageContract.Presenter;
import io.intrepid.contest.screens.entrysubmission.entryimage.EntryImageContract.View;
import io.intrepid.contest.testutils.BasePresenterTest;
import io.intrepid.contest.testutils.TestPresenterConfiguration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EntryImagePresenterTest extends BasePresenterTest<EntryImagePresenter> {
    @Mock
    View mockView;

    private Presenter presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        presenter = new EntryImagePresenter(mockView, TestPresenterConfiguration.createTestConfiguration());
    }

    @Test
    public void onViewCreatedPreviewLabelShouldShowEntryName() {
        presenter.onViewCreated();
        verify(mockView).showEntryName(any());
    }

    @Test
    public void onBitmapRemovedShouldDisplayChooseImageLayout() {
        presenter.onBitmapRemoved();
        verify(mockView).displayChooseImageLayout();
    }

    @Test
    public void bindingViewShouldDisplayChooseImageLayoutWhenBitmapsHaveNeverBeenReceived() {
        presenter.bindView(mockView);

        verify(mockView).displayChooseImageLayout();
    }

    @Test
    public void bindingViewShouldDisplayChooseImageLayoutWhenBitmapWasReceived() {
        Bitmap bitmap = mock(Bitmap.class);

        presenter.onBitmapReceived(bitmap);
        presenter.bindView(mockView);

        verify(mockView).displayPreviewImageLayout(bitmap);
    }

    @Test
    public void bindingViewShouldDisplayChooseImageLayoutWhenBitmapWasRemoved() {
        presenter.onBitmapRemoved();
        reset(mockView);
        presenter.bindView(mockView);

        verify(mockView).displayChooseImageLayout();
    }

    @Test
    public void onCameraButtonClickedShouldDispatchTakePictureIntent() {
        presenter.onCameraButtonClicked();
        verify(mockView).dispatchTakePictureIntent();
    }

    @Test
    public void onGalleryButtonClickedShouldDispatchChoosePictureIntent() {
        presenter.onGalleryButtonClicked();
        verify(mockView).dispatchChoosePictureIntent();
    }
}
