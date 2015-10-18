package com.kyleong.innometrics.api;

/**
 * Created by SQL on 2015/10/16.
 */

import com.kyleong.innometrics.dal.CounterStore;
import com.kyleong.innometrics.model.Counter;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.kyleong.innometrics.service.CounterService;
import com.kyleong.innometrics.utils.RestReturnCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/counter")
public class CounterResource {

    final static Logger logger = LoggerFactory.getLogger(CounterResource.class);

    @GET
    @Path("/getjson")
    @Produces(MediaType.APPLICATION_JSON)
    public Counter getCounterJSONValue(
            @QueryParam("name") String name
    ) {
        Counter counter = new Counter(name, CounterStore.getInstance().getCounter(name));
        logger.info("get counter " + counter.toString());
        return counter; //if only need json of Counter
    }

    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCounterValue(
            @QueryParam("name") String name
    ) {
        Counter counter = new Counter(name, CounterStore.getInstance().getCounter(name));
        logger.info("get counter " + counter.toString());
        IMResponse imResponse = null;
        if (counter.getCounter() == -1) {
            imResponse = new IMResponse(RestReturnCode.COUNTER_NOT_FOUND.getCode(), "", RestReturnCode.COUNTER_NOT_FOUND.getDescription());
        }else{
            imResponse = new IMResponse( RestReturnCode.SUCCESS.getCode(), counter.toString(), RestReturnCode.SUCCESS.getDescription());
        }
        return Response.status(200).entity(imResponse.toString()).build();  // return with formated response
    }

    @GET
    @Path("/getalljson")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllCounterJsonValue(){
        String ret = CounterStore.getInstance().getAllCounter().toString();
        logger.info("get all counter : " + ret);
        return ret;
    }

    @GET
    @Path("/getall")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCounterValue(){
        String ret = CounterStore.getInstance().getAllCounter().toString();
        logger.info("get all counter : " + ret);
        IMResponse imResponse = new IMResponse( RestReturnCode.SUCCESS.getCode(), ret, RestReturnCode.SUCCESS.getDescription());
        return Response.status(200).entity(imResponse.toString()).build();
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response add1ToCounter(
            @FormParam("name") String name
    ) {
        logger.info("Add 1 to counter" + name);
        CounterStore.getInstance().counterAdd(name);
        String result = "Add 1 to counter '" + name + "' success!";
        IMResponse imResponse = new IMResponse(0, "", result);
        return Response.status(200).entity(imResponse.toString()).build();
    }

    @POST
    @Path("/addasync")
    @Produces(MediaType.APPLICATION_JSON)
    public Response add1ToCounterBySvc(
            @FormParam("name") String name
    ) {
        logger.info("Add 1 to counter" + name);
        CounterService.getInstance().counterAdd(name);
        String result = "Add 1 to counter '" + name + "' success!";
        IMResponse imResponse = new IMResponse(0, "", result);
        return Response.status(200).entity(imResponse.toString()).build();
    }
}