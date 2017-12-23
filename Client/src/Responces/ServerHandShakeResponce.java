/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Responces;

import Decryptor.EncryptionAssistant;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author ewais
 */
public class ServerHandShakeResponce extends Responce {
    private String hashedEncryptedMessage;
    private String hash;
    private String rawMessage;
    private String fingerPrintHashedEncrypted;
    private String fingerPrintHashed;
    private String fingerPrint;
    private PublicKey keyPartDH;
    private String validFor;

    public ServerHandShakeResponce(String responce) throws ParseException, Exception
    {
        super(responce);
        processJson();
    }
    
    public PublicKey getKeyPartDH(){
        return this.keyPartDH;
    }
    
    public String getValidFor(){
        return this.validFor;
    }
    
    public boolean checkValidity() throws NoSuchAlgorithmException
    {
        EncryptionAssistant ea = new EncryptionAssistant();
        return ea.checkhash(rawMessage, hash)&&
                ea.checkhash(this.fingerPrint, this.fingerPrintHashed);
    }

    private void processJson() throws ParseException, Exception
    {
        JSONParser parser = new JSONParser(); 
        JSONObject json = (JSONObject) parser.parse(new String(Base64.getDecoder()
                .decode(responce)));
        EncryptionAssistant ea = new EncryptionAssistant();
        
        this.hashedEncryptedMessage = (String) json.get("hashedEncryptedMessage");
        this.hash = new String(
                ea.decryptWithMyPrivate(this.hashedEncryptedMessage.getBytes()));
        this.rawMessage = (String) json.get("rawMessge");
        json = (JSONObject) parser.parse(this.rawMessage);
        this.fingerPrintHashedEncrypted = (String) 
                json.get("fingerPrintHashedEncrypted");
        this.fingerPrintHashed = new String(ea.decryptWithServerPublic
                (this.fingerPrintHashedEncrypted.getBytes()));
        this.fingerPrint = (String) json.get("fingerPrint");
        //TODO: add the DHkey        
        this.validFor = (String) json.get("validFor");
    }
    
}
