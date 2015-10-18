package com.kyleong.innometrics.api;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Created by SQL on 2015/10/18.
 */
public class IMResponse {

    int returnCode = -1;
    String returnValue = "";
    String message = "";

    public IMResponse(int returnCode, String returnValue, String message){
        this.returnCode = returnCode;
        this.returnValue = returnValue;
        this.message = message;
    }

    @Override
    public String toString(){
        try {
            return new JSONObject().put("returnCode", returnCode).put("returnValue", returnValue).put("message", message).toString();
        } catch (JSONException e) {
            return null;
        }
    }

}
