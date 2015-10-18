package com.kyleong.innometircs.service;

/**
 * Created by SQL on 2015/10/18.
 */
import com.kyleong.innometrics.dal.CounterStore;
import com.kyleong.innometrics.service.CounterService;
import org.junit.*;

import static org.junit.Assert.*;

public class CounterServiceTest {

    @Test
    public void addCounterTest() {

        CounterService cs = CounterService.getInstance();
        Thread t = new Thread(CounterService.getInstance());
        t.start();
        for (int i = 0; i < 10; i++) {
            cs.counterAdd("a");
        }

        for (int i = 0; i < 100; i++) {
            cs.counterAdd("b");
        }

        for (int i = 0; i < 500; i++) {
            cs.counterAdd("c");
        }

        try {
            while(!cs.isFinished()){
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //cs.stop();
        assertEquals(CounterStore.getInstance().getCounter("a"), 10);
        cs.counterAdd("b");
        assertEquals(CounterStore.getInstance().getCounter("c"), 500);
        cs.stop();
        assertEquals(CounterStore.getInstance().getCounter("b"), 101);

    }

}
