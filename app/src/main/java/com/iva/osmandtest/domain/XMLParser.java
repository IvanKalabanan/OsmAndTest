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

    private static XMLParser instance;
    private static XmlPullParser parser;
    private int currentDepth;
    private int rootDepth = 2;

    public static XMLParser getInstance(XmlPullParser xmlParser) {
        XMLParser.parser = xmlParser;
        if (instance == null) {
            instance = new XMLParser();
        }
        return instance;
    }

    private XMLParser() {
        currentDepth = 2;
    }

    public List<Region> getWorldRegionList() {
        List<Region> list = new ArrayList<>();
        try {
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("region")) {
                    if (parser.getAttributeValue(0).equals("continent")) {
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
        rootDepth = currentDepth;
        currentDepth++;
        List<Region> list = new ArrayList<>();
        boolean isRightParentRegion = false;
        try {
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (isRightParentRegion &&
                        parser.getEventType() == XmlPullParser.END_TAG &&
                        parser.getName().equals("region") &&
                        parser.getDepth() == rootDepth) {
                    break;
                }
                // this difficult check need because we must get different field if root TAG has continent type or no
                if (parser.getEventType() == XmlPullParser.START_TAG &&
                        parser.getName().equals("region") &&
                        ((isContinentTag() && parser.getAttributeValue(1).equals(name)) || (!isContinentTag() && parser.getAttributeValue(0).equals(name)))
                        ) {
                    isRightParentRegion = true;
                    parser.next();
                    continue;
                }
                if (parser.getEventType() == XmlPullParser.START_TAG &&
                        isRightParentRegion &&
                        parser.getDepth() == currentDepth) {
                    Region region = new Region(parser.getAttributeValue(0));
                    if (parser.getAttributeValue(null, "inner_download_prefix") == null) {
                        region.setCanDownload(true);
                    }
                    list.add(region);
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


    /*
    Is root depth equals 2 (if 2 that it`s continent type) so we need get 1st parameter
    else get 0`s
     */
    private boolean isContinentTag() {
        return rootDepth == 2;
    }

}
