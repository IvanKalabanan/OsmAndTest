package com.iva.osmandtest.presentation.presenters.impl;

import com.iva.osmandtest.domain.threading.MainThread;
import com.iva.osmandtest.presentation.presenters.MainPresenter;
import com.iva.osmandtest.presentation.presenters.base.AbstractPresenter;

/**
 * Created by iva on 03.05.17.
 */

public class MainPresenterImpl extends AbstractPresenter implements MainPresenter {

    private MainPresenter.View view;

    public MainPresenterImpl(MainThread mainThread, View view) {
        super(mainThread);
        this.view = view;
    }

    @Override
    public void donwloadMap() {

    }
}
