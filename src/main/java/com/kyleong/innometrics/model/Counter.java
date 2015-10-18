package com.kyleong.innometrics.model;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Created by SQL on 2015/10/16.
 */
public class Counter {
    String name;
    int counter;

    public Counter(String name, int counter){
        this.name = name;
        this.counter = counter;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        try {
            return new JSONObject().put("name", name).put("value", counter).toString();
        } catch (JSONException e) {
            return null;
        }
    }
}
