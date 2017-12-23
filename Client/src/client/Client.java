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
//        stampMessage("heylidfjwoefhw;ourgwierhlqefh;ewuhw'oeifhw;efuh");
        makeInitialMessage();
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

    public static String stampMessage(String s) throws Exception
    {
        StampRequest g = new StampRequest(s);
        StampResponce r = g.send();
        System.out.println(r.checkValidity());
        System.out.println(r.getHash());
        System.out.println(r.getMessage());
        System.out.println(new String(Base64.getDecoder().decode(r.getOriginalData())));
        return r.getResponce();
    }

    private static void makeInitialMessage() throws Exception
    {
        getTimeStampKey();
        GetTimeStampResponce _r = getTimeStamp();
        EncryptionAssistant ea = new EncryptionAssistant();
        JSONObject rawMessage = new JSONObject();
        DiffieHellmanPackage df = ea.generateDiffieHellman();
        String fingerprint = makeFingerPrint(_r.getEncryptedTimeStamp());
        String encyptedHashedFingerPrint = makeFingerPrintHashedEncrypted(
        _r.getEncryptedTimeStamp());
        rawMessage.put("fingerPrintHashedEncrypted", encyptedHashedFingerPrint);
        rawMessage.put("fingerPrint", fingerprint);
        rawMessage.put("keyPartDH", Base64.getEncoder()
                .encodeToString(df.getPublicKey().getEncoded()));
        rawMessage.put("pubKey", Base64.getEncoder()
                .encodeToString(ea.getEncodedPublicKey()));
        JSONObject json = new JSONObject();
        json.put("hashedEncryptedMessage", new String(Base64.getEncoder().encodeToString(
                ea.encryptWithServerPublic(ea.hash(rawMessage.toJSONString()))).getBytes()));
        json.put("rawMessage", Base64.getEncoder().
                encodeToString(rawMessage.toJSONString().getBytes()));
        String stamped = stampMessage(Base64.getEncoder().encodeToString(json.toJSONString().
                getBytes()));
        ServerHandShakeRequest g = new ServerHandShakeRequest(stamped);
        ServerHandShakeResponce r = g.send();
        if(!r.checkValidity())
            throw new Exception("The handShake message has been tampered with");
        df.setReceivedPublicKey(r.getKeyPartDH());
        ea.saveSymmetricServerKey(ea.createDiffieHellmanKey(df).getBytes());
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
    private static String makeFingerPrintHashedEncrypted(
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
    
    private static boolean login(String username, String password)throws Exception{
        getTimeStampKey();
        GetTimeStampResponce r = getTimeStamp();
        EncryptionAssistant ea = new EncryptionAssistant();
        JSONObject rawMessage = new JSONObject();
        DiffieHellmanPackage df = ea.generateDiffieHellman();
        String fingerprint = makeFingerPrint(r.getEncryptedTimeStamp());
        String encyptedHashedFingerPrint = makeFingerPrintHashedEncrypted(
        r.getEncryptedTimeStamp());
        rawMessage.put("fingerPrintHashedEncrypted", encyptedHashedFingerPrint);
        rawMessage.put("fingerPrint", fingerprint);
        JSONObject loginData = new JSONObject();
        loginData.put("username", Base64.getEncoder().encodeToString(username.getBytes()));
        loginData.put("password", Base64.getEncoder().encodeToString(
        ea.hash(password).getBytes()));
        rawMessage.put("loginData", Base64.getEncoder()
                .encodeToString(loginData.toJSONString().getBytes()));
        String stamped = stampMessage(Base64.getEncoder().encodeToString(ea.encrypWithServerSymmetric(
                rawMessage.toJSONString())));
        ServerLoginRequest temp = new ServerLoginRequest(stamped);
        ServerLoginResponce rtemp= temp.send();
        if(!rtemp.success())
            System.out.print("login failed!");
        return rtemp.success();        
    }
    private static void openChannelWith(String peerUserName, double validForInHours)throws Exception{
//        peerPublicKey = getPeerPublicKey(String peerID);
        EncryptionAssistant ea = new EncryptionAssistant();
        GetTimeStampResponce timeStamp = getTimeStamp();
        DiffieHellmanPackage df = ea.generateDiffieHellman();
        JSONObject json = new JSONObject();
        json.put("fingerPrintEncryptedHashed", makeFingerPrintHashedEncrypted(
                timeStamp.getEncryptedTimeStamp()));
        json.put("fingerPrint", makeFingerPrint(
                timeStamp.getEncryptedTimeStamp()));
        json.put("peerUserName", peerUserName);
        json.put("validFor", validForInHours);
        json.put("keyPartDH", df.getPublicKey());     
        String j = json.toJSONString();       
    }
    private static void sendMessageTo(String otherUserID, String Message){
        //TODO:
    }
    private static String readConvWith(String otherUserID){
        //TODO:
        return new String("todo");
    }
    
}
