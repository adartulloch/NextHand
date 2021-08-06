package com.example.nexthand.feed.util;

import android.util.LruCache;

import com.example.nexthand.models.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Lightweight cache structure to handle Items. 
 */

public class ItemCache {
    private static final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    public static final int CACHE_MAX_SIZE = maxMemory / 8;     // Use 1/8th of the available memory for this memory cache.
    private static ItemCache mInstance;
    private LruCache<String, Item> mCache;

    private ItemCache() {
        mCache = new LruCache<>(CACHE_MAX_SIZE);
    }

    public static ItemCache getInstance() {
        if (mInstance == null) {
            mInstance = new ItemCache();
        }
        return mInstance;
    }

    /**
     * Takes a list of items and adds them to the Cache
     */
    public List<Item> loadItemsFromCache() {
        List<Item> cachedItems = new ArrayList<>();
        Collection<String> entrySet =  mCache.snapshot().keySet();
        for (String key : entrySet) {
            cachedItems.add(mCache.get(key));
        }
        return cachedItems;
    }

    /**
     * Saves un-cached items to underlying Cache
     * @param items
     * @return
     */
    public void saveItemsToCache(List<Item> items) {
        for (Item item : items) {
            String key = item.getObjectId();
            if (mCache.get(key) == null) {
                mCache.put(key, item);
            }
        }
    }

    public void evictItem(Item item) {
        if (item != null) {
            mCache.remove(item.getObjectId());
        }
    }
    public void clearCache() {
        mCache.evictAll();
    }
}
