/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Responces;

/**
 *
 * @author th
 */
public class GetTimeStampKeyResponce extends Responce
{
   
    public GetTimeStampKeyResponce(String responce)
    {
        super(responce);
    }
    
    public byte[] getKeyBytes()
    {
        return java.util.Base64.getDecoder().decode(responce);
    }
    
}
