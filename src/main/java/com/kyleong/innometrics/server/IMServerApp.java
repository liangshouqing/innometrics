package com.kyleong.innometrics.server;

/**
 * Created by SQL on 2015/10/16.
 */
import com.kyleong.innometrics.service.CounterService;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.*;
import com.sun.jersey.spi.container.servlet.ServletContainer;

public class IMServerApp {
    public static void main( String[] args ) {
        Server server = new Server(8080);
        SelectChannelConnector connector = new SelectChannelConnector();

        server.addConnector(connector);

        ServletHolder holder = new ServletHolder(ServletContainer.class);

        holder.setInitParameter("com.sun.jersey.config.property.resourceConfigClass",
                "com.sun.jersey.api.core.PackagesResourceConfig");
        holder.setInitParameter("com.sun.jersey.config.property.packages",
                "com.kyleong.innometrics.api");
        holder.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature",
                "true");

        ServletContextHandler context = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        context.addServlet(holder, "/*");
        try{
            Thread csThread = new Thread(CounterService.getInstance());
            csThread.start();
            server.start();
            server.join();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
