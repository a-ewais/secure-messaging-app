/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Requestor;

import Responces.StampResponce;

/**
 *
 * @author th
 */
public class StampRequest extends Request
{
    
    private static final String DATA_LABEL = "data";
    String data;
    
    public StampRequest(String data)
    {
        super("timestamp/stamp");
        this.data = data;
        addParameter(DATA_LABEL, data);  
    }
    
    public StampResponce send() throws Exception
    {
        return new StampResponce(getHTML(url),data);
    }
    
}
