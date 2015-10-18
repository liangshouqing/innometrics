package com.kyleong.innometrics.dal;

import com.kyleong.innometrics.model.Counter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by SQL on 2015/10/16.
 */
public class CounterStore {

    //private Map<String, Integer> counterMap = new HashMap<String, Integer>();
    private ConcurrentHashMap<String, AtomicInteger> counterMap = new ConcurrentHashMap<String, AtomicInteger>();
    private static CounterStore self = null;

    protected CounterStore() {
        //counterMap.put("defaulttest", new AtomicInteger(38));
    }

    public static synchronized CounterStore getInstance() {
        if (self == null) {
            self = new CounterStore();
        }
        return self;
    }

    public void counterAdd(String name) {
        //TODO : using request queue to avoid the sync lock, handel add in async way, just put request in queue
        synchronized (counterMap) {
            if (counterMap.containsKey(name)) {
                //counterMap.put(name, counterMap.get(name)+1);
                counterMap.get(name).incrementAndGet();
            } else {
                //counterMap.put(name, new AtomicInteger(1));
                counterMap.putIfAbsent(name, new AtomicInteger(1));
            }
        }
    }

    public int getCounter(String name) {
        if (counterMap.containsKey(name)) {
            return counterMap.get(name).intValue();
        }
        return -1;
    }

    public List<Counter> getAllCounter() {
        List<Counter> counterList = new ArrayList<Counter>();
        for (Map.Entry<String, AtomicInteger> entry : counterMap.entrySet()) {
            counterList.add(new Counter(entry.getKey(), entry.getValue().intValue()));
        }
        return counterList;
    }
}
