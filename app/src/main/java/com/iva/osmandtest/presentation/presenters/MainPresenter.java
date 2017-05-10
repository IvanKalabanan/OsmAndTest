package com.iva.osmandtest.presentation.presenters;

import com.iva.osmandtest.domain.model.Region;
import com.iva.osmandtest.presentation.ContentRequestListener;

import org.xmlpull.v1.XmlPullParser;

import java.util.List;
import java.util.TreeSet;

/**
 * Created by iva on 03.05.17.
 */

public interface MainPresenter {
    interface View extends ContentRequestListener {

        void exit();

        XmlPullParser newInstanceXMLParser();
    }

    void donwloadMap();

    List<Region> getRegions(String name);

    List<Region> getContinentsList();

    void onBackPressed();
}
