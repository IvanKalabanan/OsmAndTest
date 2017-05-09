package com.iva.osmandtest.domain.model;

/**
 * Created by Z580 on 08.05.2017.
 */

public class Region {

    private String name;
    private boolean canDownload;

    public Region(String name, boolean canDownload) {
        this.name = name;
        this.canDownload = canDownload;
    }

    public Region(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCanDownload() {
        return canDownload;
    }

    public void setCanDownload(boolean canDownload) {
        this.canDownload = canDownload;
    }
}
