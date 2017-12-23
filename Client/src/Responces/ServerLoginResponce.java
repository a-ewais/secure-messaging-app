/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Responces;

import org.json.simple.parser.ParseException;

/**
 *
 * @author ewais
 */
public class ServerLoginResponce extends Responce {
    public ServerLoginResponce(String responce) throws ParseException, Exception
    {
        super(responce);
        processJson();
    }
    public boolean success(){
        return true;
    }
    private void processJson() throws ParseException, Exception
    {
    
    }
    
}
