/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Decryptor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author th
 */
public class RSAPublicKeyCreator
{
    private final byte[] keyBytes;
    private PublicKey key;
    
    public RSAPublicKeyCreator(byte[] keyBytes, String fileName) throws IOException
    {
        this.keyBytes = keyBytes;
        saveAndGenerateKey(fileName);
    }
    
    private void saveAndGenerateKey(String fileName) throws FileNotFoundException, IOException
    {
        Security.addProvider(new BouncyCastleProvider());
        try
        {
        KeyFactory factory = KeyFactory.getInstance("RSA", "BC");FileOutputStream fileOuputStream;
        //C:\\Users\\th\\Desktop\\keytrial
        fileOuputStream = new FileOutputStream("D:\\Moe\\Studies\\Workspace\\Github\\YASMS\\secure-messaging-app\\keytrial\\"+ fileName+".pub");
        fileOuputStream.write(keyBytes);
        fileOuputStream.close();
        //C:\\Users\\th\\Desktop\\keytrial
        key = Loadkey.generatePublicKey(factory, "D:\\Moe\\Studies\\Workspace\\Github\\YASMS\\secure-messaging-app\\keytrial\\"+ fileName+".pub");
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException ex)
        {
            Logger.getLogger(RSAPublicKeyCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public PublicKey getKey()
    {
        return key;
    }
}
