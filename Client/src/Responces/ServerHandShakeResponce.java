/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Responces;

import Decryptor.EncryptionAssistant;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author ewais
 */
public class ServerHandShakeResponce extends Responce {
    private String fingerprint;
    private PublicKey keyPartDH;
    private String hash;

    public String getEncryptedTimeStamp()
    {
        return encryptedTimeStamp;
    }
    
    public ServerHandShakeResponce(String responce) throws ParseException, Exception
    {
        super(responce);
        processJson();
    }

    public String getTimeStamp()
    {
        return timeStamp;
    }

    public String getHash()
    {
        return hash;
    }
   

    public boolean checkValidity() throws NoSuchAlgorithmException
    {
        return new EncryptionAssistant().checkhash(timeStamp, hash);
    }

    private void processJson() throws ParseException, Exception
    {
        JSONParser parser = new JSONParser(); 
        JSONObject json = (JSONObject) parser.parse(responce);
        timeStamp = new EncryptionAssistant().decryptWithTimeStampKey(Base64.getDecoder().decode((String) json.get("time")));
        encryptedTimeStamp = new String(Base64.getDecoder().decode((String) json.get("time")));
        hash = new String(Base64.getDecoder().decode((String)json.get("hash")));
    }
    
}
