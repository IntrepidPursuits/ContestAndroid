package io.intrepid.contest.screens.entrysubmission.entryimage;

import android.graphics.Bitmap;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import io.intrepid.contest.models.Entry;
import io.intrepid.contest.rest.EntryResponse;
import io.intrepid.contest.screens.entrysubmission.entryimage.EntryImageContract.Presenter;
import io.intrepid.contest.screens.entrysubmission.entryimage.EntryImageContract.View;
import io.intrepid.contest.testutils.BasePresenterTest;
import io.reactivex.Observable;

import static io.reactivex.Observable.error;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EntryImagePresenterTest extends BasePresenterTest<EntryImagePresenter> {
    @Mock
    View mockView;

    private Presenter presenter;
    private Throwable throwable;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        throwable = new Throwable();
        presenter = new EntryImagePresenter(mockView, testConfiguration);
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

    @Test
    public void onEntrySubmittedShouldNotShowErrorMessageWhenApiResponseIsValid() {
        when(mockPersistentSettings.getCurrentContestId()).thenReturn(UUID.randomUUID());
        Entry entry = new Entry();
        entry.id = UUID.randomUUID();
        EntryResponse entryResponse = new EntryResponse();
        entryResponse.setEntry(entry);
        when(mockRestApi.createEntry(any(), any())).thenReturn(Observable.just(entryResponse));

        presenter.onEntrySubmitted();
        testConfiguration.triggerRxSchedulers();

        verify(mockView, never()).showInvalidEntryErrorMessage();
    }

    @Test
    public void onEntrySubmittedShouldShowInvalidEntryErrorMessageWhenApiResponseIsInvalid() {
        when(mockPersistentSettings.getCurrentContestId()).thenReturn(UUID.randomUUID());
        when(mockRestApi.createEntry(any(), any())).thenReturn(Observable.just(new EntryResponse()));

        presenter.onEntrySubmitted();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showInvalidEntryErrorMessage();
    }

    @Test
    public void onEntrySubmittedShouldShowApiErrorMessageWhenApiCallThrowsError() throws HttpException {
        when(mockPersistentSettings.getCurrentContestId()).thenReturn(UUID.randomUUID());
        when(mockRestApi.createEntry(any(), any())).thenReturn(error(throwable));

        presenter.onEntrySubmitted();
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showMessage(any(int.class));
    }
}
