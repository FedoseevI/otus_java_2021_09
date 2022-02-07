package ru.otus.cachehw;

import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

@Log4j2
public class MyCache<K, V> implements HwCache<K, V> {

    private final Map<K,V> myCache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listenerList = new ArrayList<>();

    public static final String actionPut = "putting new value";
    public static final String actionGet = "getting value";
    public static final String actionRemove = "removing value";

    private void notifyAll(K key, V value, String action) {
        for (HwListener<K, V> listener : listenerList) {
            try {
                listener.notify(key, value, action);
            }
            catch (Exception ex) {
                log.error("notifyAll runtime error", ex);
            }
        }
    }

    @Override
    public void put(K key, V value) {
        notifyAll(key, value, actionPut);
        myCache.put(key, value);
    }

    @Override
    public void remove(K key) {
        V removedValue = myCache.remove(key);
        notifyAll(key, removedValue, actionRemove);
    }

    @Override
    public V get(K key) {
        V resultValue = myCache.get(key);
        notifyAll(key, resultValue, actionGet);
        return resultValue;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listenerList.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listenerList.remove(listener);
    }
}
