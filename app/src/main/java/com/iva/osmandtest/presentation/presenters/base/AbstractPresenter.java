package com.iva.osmandtest.presentation.presenters.base;

import com.iva.osmandtest.domain.threading.MainThread;

/**
 * Created by iva on 03.05.17.
 */

public class AbstractPresenter {
    protected MainThread mMainThread;

    public AbstractPresenter(MainThread mainThread) {
        mMainThread = mainThread;
    }
}
