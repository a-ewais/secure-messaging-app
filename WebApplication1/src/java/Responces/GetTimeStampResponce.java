/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Responces;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import Decryptor.EncryptionAssistant;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
/**
 *
 * @author th
 */
public class GetTimeStampResponce extends Responce
{
    private String timeStamp;
    private String hash;
    private String encryptedTimeStamp;

    public String getHash()
    {
        return hash;
    }
    
    public GetTimeStampResponce(String responce) throws ParseException, Exception
    {
        super(responce);
        processJson();
    }
    
    public String getTimeStamp()
    {
        return timeStamp;
    }
    public String getEncryptedTimeStamp()
    {
        return encryptedTimeStamp;
    }
    public boolean checkValidity() throws NoSuchAlgorithmException
    {
        return new EncryptionAssistant().checkhash(timeStamp, hash);
    }

    private void processJson() throws ParseException, Exception
    {
        JSONParser parser = new JSONParser(); 
        JSONObject json = (JSONObject) parser.parse(
                Arrays.toString(Base64.getDecoder()
                .decode(responce)));
        encryptedTimeStamp = (String)json.get("time");
        timeStamp = new EncryptionAssistant().
                decryptWithTimeStampKey(encryptedTimeStamp.getBytes());
        hash = (String) json.get("hash");
    }
    
}
