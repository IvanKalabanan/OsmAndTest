package com.iva.osmandtest.domain;

import com.iva.osmandtest.domain.model.Region;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Z580 on 09.05.2017.
 */

public class XMLParser {

    private final int BASE_ROOT_DEPTH = 2;

    private static XMLParser instance;
    private static XmlPullParser parser;
    private int currentDepth;
    private int rootDepth;

    private final String REGION_TEXT = "region";
    private final String NAME_TEXT = "name";
    private final String CONTINENT_TEXT = "continent";
    private final String DOWNLOAD_PREFIX_TEXT = "inner_download_prefix";

    public static XMLParser getInstance() {
        return instance;
    }

    public static XMLParser getInstance(XmlPullParser xmlParser) {
        XMLParser.parser = xmlParser;
        if (instance == null) {
            instance = new XMLParser();
        }
        return instance;
    }

    private XMLParser() {
        currentDepth = BASE_ROOT_DEPTH;
    }

    public List<Region> getWorldRegionList() {
        List<Region> list = new ArrayList<>();
        try {
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals(REGION_TEXT)) {
                    if (parser.getAttributeValue(0).equals(CONTINENT_TEXT)) {
                        list.add(new Region(parser.getAttributeValue(1), false));
//                        list.add(upperFirstCharacter(xmlParser.getAttributeValue(1)));
                    }
                }
                parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        Collections.sort(list, new Comparator<Region>() {
            @Override
            public int compare(Region o1, Region o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
        return list;
    }

    public List<Region> getRegions(String name) {
        List<Region> list = new ArrayList<>();
        boolean isRightParentRegion = false;
        try {
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (isRightParentRegion &&
                        parser.getEventType() == XmlPullParser.END_TAG &&
                        parser.getName().equals(REGION_TEXT) &&
                        parser.getDepth() == rootDepth) {
                    break;
                }
                if (parser.getEventType() == XmlPullParser.START_TAG &&
                        isRightParentRegion &&
                        parser.getDepth() == currentDepth) {
                    Region region = new Region(parser.getAttributeValue(null, NAME_TEXT));
                    if (parser.getAttributeValue(null, DOWNLOAD_PREFIX_TEXT) == null) {
                        region.setCanDownload(true);
                    }
                    list.add(region);
                }
                if (!isRightParentRegion &&
                        parser.getEventType() == XmlPullParser.START_TAG &&
                        parser.getName().equals(REGION_TEXT) &&
                        parser.getAttributeValue(null, NAME_TEXT).equals(name)
                        ) {
                    isRightParentRegion = true;
                    rootDepth = parser.getDepth();
                    currentDepth = rootDepth + 1;
                }
                parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        Collections.sort(list, new Comparator<Region>() {
            @Override
            public int compare(Region o1, Region o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
        return list;
    }

    public void decreaseDepth() {
        if (currentDepth > BASE_ROOT_DEPTH) {
            currentDepth--;
        }
    }

}
