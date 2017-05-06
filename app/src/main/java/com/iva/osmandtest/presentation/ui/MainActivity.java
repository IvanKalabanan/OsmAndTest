package com.iva.osmandtest.presentation.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.iva.osmandtest.R;
import com.iva.osmandtest.domain.threading.MainThreadImpl;
import com.iva.osmandtest.presentation.presenters.MainPresenter;
import com.iva.osmandtest.presentation.presenters.impl.MainPresenterImpl;

public class MainActivity extends AppCompatActivity implements MainPresenter.View {
    private final String TAG = "MainActivity";

    private MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter = new MainPresenterImpl(
                MainThreadImpl.getInstance(),
                this
        );

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
}
