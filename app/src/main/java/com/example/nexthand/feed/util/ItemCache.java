package com.example.nexthand.feed.util;

import android.util.LruCache;

public class ItemCache {
    public static final int CACHE_MAX_SIZE = 1024;
    private static ItemCache mInstance;
    private LruCache<Object, Object> mCache;

    private ItemCache() {
        mCache = new LruCache<Object, Object>(CACHE_MAX_SIZE);
    }

    public static ItemCache getInstance() {
        if (mInstance == null) {
            mInstance = new ItemCache();
        }
        return mInstance;
    }

    public LruCache<Object, Object> getCache() {
        return mCache;
    }
}
