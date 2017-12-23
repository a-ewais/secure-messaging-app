/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Decryptor.DiffieHellmanPackage;
import Decryptor.EncryptionAssistant;
import Requestor.GetTimeStampRequest;
import Requestor.StampRequest;
import Responces.GetTimeStampResponce;
import Responces.StampResponce;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.apache.xml.security.utils.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author moera
 */
public class handshake extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Base64DecodingException, ParseException, Exception {
        EncryptionAssistant ea = new EncryptionAssistant();
        
        String msg = request.getParameter("handShake");
        System.out.println(msg);
        JSONParser parser = new JSONParser();
        String jsonDecoded = new String(Base64.decode(msg.getBytes()));
        JSONObject json = (JSONObject) parser.parse(jsonDecoded);
        System.out.println((String) json.toJSONString());
        
        String hashedEncrypted = new String(Base64.decode((String) json.get("hash")));
        String data = new String(Base64.decode((String) json.get("data")));
        System.out.println(data);
        String hash = ea.decryptWithTimeStampKey(Base64.decode((String) json.get("hash")));
        if(!hash.equals(ea.hash(data)))
            throw new Exception("Hash not matching stamped data.");
        
        json = (JSONObject) parser.parse(data);
        data = (String) json.get("data");
        json = (JSONObject) parser.parse(new String(Base64.decode(data)));
        System.out.println(data);
        
        String hashedEncryptedMessage = new String(Base64.decode((String) json.get("hashedEncryptedMessage")));
        String rawMessage = new String(Base64.decode((String) json.get("rawMessage")));
        String hashedMessage = new String(ea.decryptWithMyPrivate(Base64.decode((String) json.get("hashedEncryptedMessage"))));
        if(!hashedMessage.equals(ea.hash(rawMessage)))
            throw new Exception("Hash not matching raw message.");
        
        JSONObject msgObject = (JSONObject) parser.parse(rawMessage);
        System.out.println(msgObject);
        
//        String fingerPrintHashedEncrypted = new String(Base64.decode((String) msgObject.get("fingerPrintHashedEncrypted")));
//        String fingerPrint = new String(Base64.decode((String) msgObject.get("fingerPrint")));
//        String fingerPrintHashed = new String(ea.(Base64.decode((String) msgObject.get("fingerPrintHashedEncrypted"))));
//        if(!fingerPrintHashed.equals(ea.hash(fingerPrint)))
//            throw new Exception("Hash not matching finger print.");
        
        byte[] keyPartDHString =  Base64.decode((String) msgObject.get("keyPartDH"));
        KeyFactory kf = KeyFactory.getInstance("DH");
        PublicKey keyPartDH = kf.generatePublic(new X509EncodedKeySpec(keyPartDHString));
        System.out.println(keyPartDH);
        
        byte[] pubKeyString = Base64.decode((String) msgObject.get("pubKey"));
        System.out.println(new String(pubKeyString));
        kf = KeyFactory.getInstance("RSA");
        PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(Base64.decode((String) msgObject.get("pubKey"))));
        System.out.println(publicKey);
        
        // TODO: Store PK : DH in file.
    }
    
    private static String prepareResponse() throws Exception {
        EncryptionAssistant ea = new EncryptionAssistant();
        JSONObject rawMessage = new JSONObject();
        DiffieHellmanPackage df = ea.generateDiffieHellman();
        GetTimeStampResponce _r = getTimeStamp();
        String fingerprint = makeFingerPrint(_r.getEncryptedTimeStamp());
        String encyptedHashedFingerPrint = makeFingerPrintHashedEncrypted(
        _r.getEncryptedTimeStamp());
        rawMessage.put("fingerPrintHashedEncrypted", encyptedHashedFingerPrint);
        rawMessage.put("fingerPrint", fingerprint);
        rawMessage.put("keyPartDH", Base64.encode(df.getPublicKey().getEncoded()));
        rawMessage.put("validFor", Base64.encode(new String("5")));
        JSONObject json = new JSONObject();
        json.put("hashedEncryptedMessage", new String(Base64.encode(
                ea.encryptWithServerPublic(ea.hash(rawMessage.toJSONString()))).getBytes()));
        json.put("rawMessage", Base64.encode(rawMessage.toJSONString().getBytes()));
        String stamped = stampMessage(Base64.encode(json.toJSONString().
                getBytes()));
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
        System.out.println(new String(Base64.decode(r.getOriginalData())));
        return r.getResponce();
    }
    
    private static String makeFingerPrint(
            String encryptedTimeStamp) 
            throws NoSuchAlgorithmException, Exception
    {
        JSONObject json = new JSONObject();
        json.put("timestamp",encryptedTimeStamp);
        json.put("ID", ID);
        String j = json.toJSONString();
        return Base64.encode(j.getBytes());
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
        return Base64.encode(ea.encryptWithMyPrivate(ea.hash(json.toJSONString()).getBytes()));
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(handshake.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(handshake.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(handshake.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(handshake.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
