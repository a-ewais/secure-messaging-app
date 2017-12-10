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
    
    public ServerHandShakeRequest()
    {
        super("WebApplication1/handshake");
    }
    
    public ServerHandShakeResponce send() throws Exception
    {
        return new ServerHandShakeResponce(getHTML(url));
    }
}
