/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import Requestor.*;
import Responces.*;
import Decryptor.*;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Base64;
import org.json.simple.JSONObject;

public class Client
{

    private static final String ID = "omar";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception
    {
        getTimeStampKey();
        GetTimeStampResponce r = getTimeStamp();
        stampMessage("heylidfjwoefhw;ourgwierhlqefh;ewuhw'oeifhw;efuh");
        //makeInitialMessage(r);
//        DatabaseHandler d = new DatabaseHandler();
    }

    public static void getTimeStampKey() throws Exception
    {
        GetTimeStampKeyRequest g = new GetTimeStampKeyRequest();
        GetTimeStampKeyResponce r = g.send();
        new EncryptionAssistant().saveTimeStampKey(r.getKeyBytes());
    }

    public static GetTimeStampResponce getTimeStamp() throws Exception
    {
        GetTimeStampRequest g = new GetTimeStampRequest();
        GetTimeStampResponce r = g.send();
        System.out.println(r.getTimeStamp());
        System.out.println(r.checkValidity());
        return r;
    }

    public static void stampMessage(String s) throws Exception
    {
        StampRequest g = new StampRequest(s);
        StampResponce r = g.send();
        System.out.println(r.checkValidity());
        System.out.println(r.getData());
        System.out.println(r.getMessage());
    }

    private static String makeInitialMessage(GetTimeStampResponce r) throws Exception
    {
        EncryptionAssistant ea = new EncryptionAssistant();
        JSONObject rawMessage = new JSONObject();
        DiffieHellmanPackage df = ea.generateDiffieHellman();
        String fingerprint = makeFingerPrint(r.getEncryptedTimeStamp());
        String encyptedHashedFingerPrint = makeFingerPrintEncryptedHashed(
        r.getEncryptedTimeStamp());
        rawMessage.put("fingerPrintHashedEncrypted", encyptedHashedFingerPrint);
        rawMessage.put("fingerPrint", fingerprint);
        rawMessage.put("keyPartDH", df.getPublicKey());
        rawMessage.put("pubKey", ea.getEncodedPublicKey());
        JSONObject json = new JSONObject();
        json.put("hashedEncryptedMessage", Arrays.toString(
                ea.encryptWithServerPublic(ea.hash(rawMessage.toJSONString()))));
        json.put("rawMessage", rawMessage.toJSONString());
        
        return Base64.getEncoder().encodeToString(json.toJSONString().getBytes());
    }
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
    private static void openChannelWith(String peerUserName, double validForInHours)throws Exception{
//        peerPublicKey = getPeerPublicKey(String peerID);
        EncryptionAssistant ea = new EncryptionAssistant();
        GetTimeStampResponce timeStamp = getTimeStamp();
        DiffieHellmanPackage df = ea.generateDiffieHellman();
        JSONObject json = new JSONObject();
        json.put("fingerPrintEncryptedHashed", makeFingerPrintEncryptedHashed(
                timeStamp.getEncryptedTimeStamp()));
        json.put("fingerPrint", makeFingerPrint(
                timeStamp.getEncryptedTimeStamp()));
        json.put("peerUserName", peerUserName);
        json.put("validFor", validForInHours);
        json.put("keyPartDH", df.getPublicKey());     
        String j = json.toJSONString();       
    }
    private static void sendMessageTo(String otherUserID, String Message){
        
    }
    private static String readConvWith(String otherUserID){
        String conv = new String ("");
        return conv;
    }
    
}
