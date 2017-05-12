package com.iva.osmandtest.presentation.presenters.impl;

import android.support.v4.view.PagerAdapter;

import com.iva.osmandtest.domain.CacheManager;
import com.iva.osmandtest.domain.XMLParser;
import com.iva.osmandtest.domain.model.Region;
import com.iva.osmandtest.domain.thread.MainThread;
import com.iva.osmandtest.presentation.presenters.MainPresenter;
import com.iva.osmandtest.presentation.presenters.base.AbstractPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iva on 03.05.17.
 */

public class MainPresenterImpl extends AbstractPresenter implements MainPresenter {

    private final String CONTINENTS = "continents";
    private MainPresenter.View view;
    private CacheManager cacheManager;

    // need for BackPress logic
    private List<String> statesList;

    public MainPresenterImpl(MainThread mainThread, View view) {
        super(mainThread);
        this.view = view;
        cacheManager = new CacheManager();
        statesList = new ArrayList<>();
    }

    @Override
    public boolean isRootPage() {
        return statesList.isEmpty();
    }

    @Override
    public void resume() {

    }

    @Override
    public List<Region> getRegions(String name) {
        if (!isSameLastElement(name)) {
            statesList.add(name);
        }
        if (cacheManager.isContaineRegions(name)) {
            return cacheManager.getRegionList(name);
        } else {
            return cacheManager.putRegions(name, XMLParser.getInstance(
                    view.newInstanceXMLParser()).getRegions(name));
        }
    }

    @Override
    public List<Region> getContinentsList() {
        if (cacheManager.isContaineRegions(CONTINENTS)) {
            return cacheManager.getRegionList(CONTINENTS);
        }
        return cacheManager.putRegions(CONTINENTS, XMLParser.getInstance(
                view.newInstanceXMLParser()).getWorldRegionList());
    }

    @Override
    public void onBackPressed() {
        if (statesList.isEmpty()) {
            view.exit();
            return;
        }
        XMLParser.getInstance().decreaseDepth();
        if (statesList.size() == 1) {
            statesList.remove(0);
            view.getContinents();
        } else {
            statesList.remove(statesList.size() - 1);
            view.getRegions(statesList.get(statesList.size() - 1));
        }
    }

    private String upperFirstCharacter(String text) {
        StringBuilder sb = new StringBuilder(text);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

    private boolean isSameLastElement(String name) {
        return !statesList.isEmpty() && statesList.get(statesList.size() - 1).equals(name);
    }
}
