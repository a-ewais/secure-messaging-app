/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Requestor;

import Responces.GetTimeStampResponce;
/**
 *
 * @author th
 */
public class GetTimeStampRequest extends Request
{

    public GetTimeStampRequest()
    {
        super("timestamp/gettime");
    }
    
        public GetTimeStampResponce send() throws Exception
    {
        return new GetTimeStampResponce(getHTML(url));
    }
    
}
