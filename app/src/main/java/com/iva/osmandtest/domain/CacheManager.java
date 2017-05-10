package com.iva.osmandtest.domain;

import com.iva.osmandtest.domain.model.Region;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by iva on 10.05.17.
 * <p>
 * We parse data from xml only ones time and next time we take ready data from cache
 */

public class CacheManager {

    private Map<String, List<Region>> cache;

    public CacheManager() {
        cache = new HashMap<>();
    }

    public List<Region> getRegionList(String name) {
        return cache.get(name);
    }

    public List<Region> putRegions(String name, List<Region> regions) {
        cache.put(name, regions);
        return regions;
    }

    public boolean isContaineRegions(String name) {
        return cache.containsKey(name);
    }
}
