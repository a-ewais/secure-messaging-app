/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Requestor;
import Responces.ServerHandShakeResponce;
/**
 *
 * @author ewais
 */
public class ServerHandShakeRequest extends Request {
    private static final String DATA_LABEL = "handShake";
    String data;
    
   
        
    public ServerHandShakeRequest(String _data)
    {
        
        super("WebApplication1/handshake");
        this.data = _data;
        addParameter(DATA_LABEL, data);
    }
    
    public ServerHandShakeResponce send() throws Exception
    {
        return new ServerHandShakeResponce(getHTML(url));
    }
}
