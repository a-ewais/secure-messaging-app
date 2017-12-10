/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Decryptor.EncryptionAssistant;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import org.json.simple.JSONObject;

/**
 *
 * @author moera
 */
public class Server {
    private static final String ID = "omar";
    
    private static String makeFingerPrint(
            String encryptedTimeStamp) 
            throws NoSuchAlgorithmException, Exception
    {
        JSONObject json = new JSONObject();
        json.put("timestamp",encryptedTimeStamp);
        json.put("ID", ID);
        String j = json.toJSONString();
        return Base64.getEncoder().encodeToString(j.getBytes());
    }
    private static String makeFingerPrintEncryptedHashed(
            String encryptedTimeStamp) 
            throws NoSuchAlgorithmException, Exception
    {
        EncryptionAssistant ea = new EncryptionAssistant();
        JSONObject json = new JSONObject();
        json.put("timestamp", encryptedTimeStamp);
        json.put("ID", ID);
        String j = json.toJSONString();
        return Base64.getEncoder().
                encodeToString(ea.encryptWithMyPrivate(ea.hash(json.toJSONString()).getBytes()));
    }
}
