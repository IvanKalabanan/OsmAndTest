package com.iva.osmandtest.presentation.presenters.impl;

import com.iva.osmandtest.domain.XMLParser;
import com.iva.osmandtest.domain.model.Region;
import com.iva.osmandtest.domain.thread.MainThread;
import com.iva.osmandtest.presentation.presenters.MainPresenter;
import com.iva.osmandtest.presentation.presenters.base.AbstractPresenter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @Override
    public List<Region> getRegions(String name) {
        return XMLParser.getInstance(view.newInstanceXMLParser()).getRegions(name);
    }

    @Override
    public List<Region> getWorldRegionList() {
        return XMLParser.getInstance(view.newInstanceXMLParser()).getWorldRegionList();
    }

    private String upperFirstCharacter(String text) {
        StringBuilder sb = new StringBuilder(text);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

}
