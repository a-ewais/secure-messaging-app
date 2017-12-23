/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Requestor;

import Responces.ServerHandShakeResponce;
import Responces.ServerLoginResponce;

/**
 *
 * @author ewais
 */
public class ServerLoginRequest extends Request {
    private static final String DATA_LABEL = "login";
    String data;
    
    public ServerLoginRequest(String _data){
        super("WebApplication1/login");
        this.data = _data;
        addParameter(DATA_LABEL, data);
    }
    
    public ServerLoginResponce send() throws Exception
    {
        return new ServerLoginResponce(getHTML(url));
    }
}
