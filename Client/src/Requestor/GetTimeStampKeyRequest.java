/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Requestor;

import Responces.GetTimeStampKeyResponce;

/**
 *
 * @author th
 */
public class GetTimeStampKeyRequest extends Request
{
    public GetTimeStampKeyRequest()
    {
        super("WebApplication1/gettimekey");
    }
    
    public GetTimeStampKeyResponce send() throws Exception
    {
        return new GetTimeStampKeyResponce(getHTML(url));
    }
            
            
}
