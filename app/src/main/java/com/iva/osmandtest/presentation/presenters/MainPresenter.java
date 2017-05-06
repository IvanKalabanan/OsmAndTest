package com.iva.osmandtest.presentation.presenters;

/**
 * Created by iva on 03.05.17.
 */

public interface MainPresenter {
    interface View {

        void showProgress();

        void hideProgress();
    }

    void donwloadMap();

}
