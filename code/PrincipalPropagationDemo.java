package com.sap.connectivity.demo;

import java.io.IOException;
import java.io.PrintWriter;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import com.sap.core.connectivity.api.DestinationFactory;
import com.sap.core.connectivity.api.http.HttpDestination;

/**
 * Servlet implementation class PrincipalPropagationDemo
 */
public class PrincipalPropagationDemo extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public PrincipalPropagationDemo() 
    {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException 
    {

        PrintWriter responseWriter = response.getWriter();
        String targetDest=null;
        try 
        {
            targetDest=request.getParameter("destination")
            if (targetDest==null)
                targetDest="ABAP_SYSTEM";

            InitialContext ctx=new InitialContext();
            DestinationFactory destinationFactory=(DestinationFactory)ctx.lookup(DestinationFactory.JNDI_NAME);
            HttpDestination destination=(HttpDestination)destinationFactory.getDestination(targetDest);
            HttpClient httpClient4BC = destination.createHttpClient();

            StringBuilder command=new StringBuilder(256);
            command.append("/sap/bc/user_info");

            HttpGet getOperation = new HttpGet(command.toString());
            HttpResponse backendResponse = httpClient4BC.execute(getOperation);
            HttpEntity entity = backendResponse.getEntity();
            String respToString = EntityUtils.toString(entity);

            int statusCode = backendResponse.getStatusLine().getStatusCode();
            response.addHeader("Content-type", backendResponse.getFirstHeader("content-type").getValue());
            switch (statusCode)
            {
                case 401:
                    responseWriter.println("Destination access to "+targetDest+" failed with 401");
                    Header[] headers=backendResponse.getAllHeaders();
                    responseWriter.println("Response from  server: ");
                    for (int i = 0; i<headers.length; i++) 
                    {
                        responseWriter.println(headers[i].toString());
                    }
                    break;
                default:
                    response.setStatus(statusCode, backendResponse.getStatusLine().getReasonPhrase());
            }
            responseWriter.println(respToString);
        } 
        catch (Exception e)
        {
            e.printStackTrace(responseWriter);
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        response.getWriter().println("In this example we restrict ourselves to GET operations - I'll redirect to get now ");
        doGet(request, response);
    }
}
