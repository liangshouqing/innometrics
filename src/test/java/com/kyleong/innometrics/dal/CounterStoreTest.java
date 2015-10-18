package com.kyleong.innometrics.dal;

/**
 * Created by SQL on 2015/10/16.
 */

import com.kyleong.innometrics.api.IMResponse;
import com.kyleong.innometrics.model.Counter;
import org.junit.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

//TODO : add some more test cases, include jersey test client to test rest api
public class CounterStoreTest {

    @Test
    public void addCounterTest() {
        CounterStore cs = CounterStore.getInstance();
        cs.counterAdd("a");
        assertEquals(cs.getCounter("a"), 1);
        cs.counterAdd("a");
        assertEquals(cs.getCounter("a"), 2);
        cs.counterAdd("a");
        assertEquals(cs.getCounter("a"), 3);
        cs.counterAdd("b");
        cs.counterAdd("b");
        assertEquals(cs.getCounter("b"), 2);
    }

    @Test
    public void getAllCounter() {
        CounterStore cs = CounterStore.getInstance();
        cs.counterAdd("a");
        cs.counterAdd("a");
        cs.counterAdd("a");
        cs.counterAdd("b");
        cs.counterAdd("b");
        for (Iterator<Counter> it = cs.getAllCounter().iterator(); it.hasNext(); ) {
            System.out.println(it.next());
        }

        List<Counter> lstExpect = new ArrayList<Counter>();
        lstExpect.add(new Counter("b", 2));
        lstExpect.add(new Counter("a", 3));
        //assertThat(lstExpect,is(cs.getAllCounter()));
    }

    @Test
    public void multiThreadAdd2() {

        class AddCounterThread implements Runnable {

            private CounterStore cs = null;
            private String name = "";
            private int nTimes = 0;

            public AddCounterThread(String name, int nTimes) {
                this.cs = CounterStore.getInstance();
                this.name = name;
                this.nTimes = nTimes;
            }

            public void run() {
                for (int i = 0; i < this.nTimes; i++) {
                    cs.counterAdd(name);
                    //System.out.println("Thread : " + Thread.currentThread().getName() + ", Counter " + name + " add 1, current value : " + cs.getCounter(name));
                }
            }
        }

        CounterStore csi = CounterStore.getInstance();

        List<Thread> lstThreads = new ArrayList<Thread>();

        int n = 10;
        int m = 5;
        try {
            for (int i = 0; i < n; i++) {
                //lstThreads.add(new AddCounterThread("a", 500000));
                Thread t = new Thread(new AddCounterThread("a", 500000));
                lstThreads.add(t);
                t.start();

            }
            for(Thread t : lstThreads){
                t.join();
            }
            for (int i = 0; i < m; i++) {
                //lstThreads.add(new AddCounterThread("a", 500000));
                Thread t = new Thread(new AddCounterThread("b", 900000));
                t.start();
                t.join();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(csi.getCounter("a"), 500000 * n);
        assertEquals(csi.getCounter("b"), 900000 * m);
        //assertEquals(csi.getCounter("b"), 600000);
    }

    @Test
    public void multiThreadAdd() {

        class AddCounterThread implements Runnable {

            private CounterStore cs = null;
            private String name = "";
            private int nTimes = 0;

            public AddCounterThread(CounterStore cs, String name, int nTimes) {
                this.cs = cs;
                this.name = name;
                this.nTimes = nTimes;
            }

            public void run() {
                for (int i = 0; i < this.nTimes; i++) {
                    cs.counterAdd(name);
                }
            }
        }

        CounterStore csi = CounterStore.getInstance();

        Thread t1 = new Thread(new AddCounterThread(csi, "a", 500000));
        Thread t2 = new Thread(new AddCounterThread(csi, "a", 500000));
        Thread t3 = new Thread(new AddCounterThread(csi, "b", 600000));

        t1.start();
        t2.start();
        t3.start();
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(csi.getCounter("a"), 1000000);
        assertEquals(csi.getCounter("b"), 600000);
    }

    @Test public void testIMResponse(){
        IMResponse imr = new IMResponse(200, "testvalue", "message");
        System.out.println(imr.toString());
    }
}
