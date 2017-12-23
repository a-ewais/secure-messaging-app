/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Responces;

import Decryptor.EncryptionAssistant;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author th
 */
public class StampResponce extends Responce
{

    String hash;
    String message;
    String originalData;
    String data;
    String res;

    public StampResponce(String responce, String originalData) throws ParseException, Exception
    {
        super(responce);
        res = responce;
        this.originalData = originalData;
        proccesJson();
    }

    public boolean checkValidity() throws NoSuchAlgorithmException
    {
        return new EncryptionAssistant().checkhash(message, hash) && originalData.equals(data);
    }

    private void proccesJson() throws ParseException, Exception
    {
        String x = new String(Base64.getDecoder().decode(responce));
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(x);
        hash = new EncryptionAssistant().decryptWithTimeStampKey(Base64.getDecoder().decode((String) json.get("hash")));
        message = new String(Base64.getDecoder().decode(((String) json.get("data")).getBytes()));
        data = getDataFromMessage(message);
    }

    private String getDataFromMessage(String message) throws ParseException
    {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(message);
        return (String) json.get("data");
    }

    public String getHash()
    {
        return hash;
    }

    public String getMessage()
    {
        return message;
    }

    public String getOriginalData()
    {
        return originalData;
    }

    public String getData()
    {
        return data;
    }
    public String getResponce(){
        return res;
    }

}
