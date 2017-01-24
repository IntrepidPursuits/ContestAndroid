package io.intrepid.contest.splash;

import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.Toast;

import butterknife.OnClick;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;

public class SplashActivity extends BaseMvpActivity<SplashContract.Presenter> implements SplashContract.View {

    @NonNull
    @Override
    public SplashContract.Presenter createPresenter(PresenterConfiguration configuration) {
        return new SplashPresenter(this, configuration);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_example1;
    }

    @OnClick(R.id.example1_button)
    public void onExampleButtonClicked(Button button){
        presenter.exampleButtonClicked();
    }

    @Override
    public void showMessage(String string) {
        Toast.makeText(this,string, Toast.LENGTH_LONG).show();
    }
}
