/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Requestor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 *
 * @author th
 */
public abstract class Request
{
    private int paramters = 0;
    protected String url = "http://10.40.36.83:8080/";

    public Request(String url)
    {
        this.url += url;
    }

    protected String getHTML(String url) throws Exception
    {
        StringBuilder result = new StringBuilder();
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestMethod("GET");
        try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream())))
        {
            String line;
            while ((line = rd.readLine()) != null)
            {
                result.append(line);
            }
        }
        //return java.util.Base64.getDecoder().decode(result.toString());
        return result.toString();
    }

    protected void addParameter(String label, String value)
    {
        if(paramters== 0)
        {
            url += "?";
        }
        url+= label + "=" + value;
        paramters++;
    }
    
}
