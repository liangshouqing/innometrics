package com.kyleong.innometrics.service;

import com.kyleong.innometrics.dal.CounterStore;
import java.util.concurrent.ExecutorService;

import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by SQL on 2015/10/18.
 * For processing add request async way;
 */
public class CounterService implements Runnable{

    private static CounterService self = null;

    public static synchronized CounterService getInstance() {
        if (self == null) {
            self = new CounterService();
        }
        return self;
    }

    private boolean runFlag = false;

    class CounterAddThread implements Runnable{

        Queue<String> queue = null;

        public CounterAddThread(Queue<String> asyncQueue){
            queue = asyncQueue;
        }

        public void run() {
            String key = queue.poll();
            CounterStore.getInstance().counterAdd(key);
            System.out.println("Thread: " + Thread.currentThread().getName()+", add count to " + key);
            //System.out.println("current queue size : " + queue.size());
        }
    }

    Queue<String> addRequestQueue = null;
    ExecutorService executor = null;

    public CounterService(){
        addRequestQueue = new LinkedBlockingDeque<String>();
        executor = Executors.newFixedThreadPool(10);
    }

    public void counterAdd(String name){
        addRequestQueue.add(name);
    }

    public void run(){
        runFlag = true;
        while(runFlag){
            if (addRequestQueue.size() > 0) {
                executor.execute(new CounterAddThread(addRequestQueue));
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop(){
        runFlag = false;
        System.out.println("Queue size : " + addRequestQueue.size());
        try {
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }

    public boolean isFinished(){
        return addRequestQueue.size() == 0;
    }
}
