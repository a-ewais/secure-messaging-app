/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Decryptor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author th
 */
public class EncryptionAssistant
{

    public static final int KEY_SIZE = 2048;
    private static final String TIME_STAMP_KEY_NAME = "TimeKey";
    private static final String MY_PRIVATE_KEY_NAME = "MyPrivateKey";
    private static final String MY_PUBLIC_KEY_NAME = "MyPublicKey";
    private static final String SERVER_PUBLIC_KEY_NAME = "ServerPublicKey";
    private static final String SERVER_DIFFIE_HELLMAN_KEY="ServerDiffieHellman";
    private static final String KEYSTORE_ADDRESS = "/home/ewais/Downloads/Security/keytrial/";

    public EncryptionAssistant()
    {
        Security.addProvider(new BouncyCastleProvider());
    }

    public void saveTimeStampKey(byte[] keyBytes) throws IOException
    {
        RSAPublicKeyCreator key = new RSAPublicKeyCreator(keyBytes, TIME_STAMP_KEY_NAME);
    }
    public void saveSymmetricServerKey(byte[] keyBytes) throws IOException
    {
        //TODO: save the DH with server as a local file
    }
    public byte[] encrypWithServerSymmetric(String s)throws Exception{
        //TODO: implement.
        return new byte[0];
    }
    public String decryptWithTimeStampKey(byte[] s) throws Exception
    {
        
        KeyFactory factory = KeyFactory.getInstance("RSA", "BC");
        PublicKey key = Loadkey.generatePublicKey(factory, KEYSTORE_ADDRESS + TIME_STAMP_KEY_NAME + ".pub");
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] plainText = cipher.doFinal(s);
        return new String(plainText);
    }

    public boolean checkhash(String s, String hashed) throws NoSuchAlgorithmException
    {
        return hashed.equals(hash(s));
    }

    public String hash(String s) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(s.getBytes(StandardCharsets.UTF_8));
        byte[] digest = md.digest();
        String hex = String.format("%064x", new BigInteger(1, digest));
        return hex;
    }

    public void generateRSAKeyPair() throws IOException, NoSuchAlgorithmException, NoSuchProviderException
    {
        KeyPair keyPair = getRSAKeyPair();
        RSAPrivateKey priv = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey pub = (RSAPublicKey) keyPair.getPublic();
        writePemFile(priv, "RSA PRIVATE KEY", KEYSTORE_ADDRESS + MY_PRIVATE_KEY_NAME);
        writePemFile(pub, "RSA PUBLIC KEY", KEYSTORE_ADDRESS + MY_PUBLIC_KEY_NAME + ".pub");
    }

    private KeyPair getRSAKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException
    {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
        generator.initialize(KEY_SIZE);
        KeyPair keyPair = generator.generateKeyPair();
        return keyPair;
    }

    private void writePemFile(Key key, String description, String filename)
            throws FileNotFoundException, IOException
    {
        PemFile pemFile = new PemFile(key, description);
        pemFile.write(filename);
    }

    public DiffieHellmanPackage generateDiffieHellman() throws Exception
    {
        DiffieHellmanPackage p = new DiffieHellmanPackage();
        final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
        keyPairGenerator.initialize(1024);

        final KeyPair keyPair = keyPairGenerator.generateKeyPair();

        p.privateKey = keyPair.getPrivate();
        p.setPublicKey(keyPair.getPublic());
        return p;
    }

    public String createDiffieHellmanKey(DiffieHellmanPackage df) throws Exception
    {
        return new String(Base64.getEncoder().encode(generateCommonSecretKey(df)));
    }

    private byte[] generateCommonSecretKey(DiffieHellmanPackage df) throws Exception
    {
        final KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
        keyAgreement.init(df.privateKey);
        keyAgreement.doPhase(df.getReceivedPublicKey(), true);
        byte[] key = shortenSecretKey(keyAgreement.generateSecret());
        return key;
    }

    private byte[] shortenSecretKey(final byte[] longKey) throws Exception
    {
        final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        final DESKeySpec desSpec = new DESKeySpec(longKey);
        return keyFactory.generateSecret(desSpec).getEncoded();
    }

    public byte[] encryptWithMyPrivate(byte[] s) throws Exception
    {
        Security.addProvider(new BouncyCastleProvider());
        KeyFactory factory = KeyFactory.getInstance("RSA", "BC");
        PrivateKey priv = Loadkey.generatePrivateKey(factory, KEYSTORE_ADDRESS + MY_PRIVATE_KEY_NAME);
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, priv);
        byte[] ciphetText = cipher.doFinal(s);
        return ciphetText;
    }
    
    public byte[] decryptWithMyPrivate(byte[] s) throws Exception
    {
        Security.addProvider(new BouncyCastleProvider());
        KeyFactory factory = KeyFactory.getInstance("RSA", "BC");
        PrivateKey priv = Loadkey.generatePrivateKey(factory,
                KEYSTORE_ADDRESS + MY_PRIVATE_KEY_NAME);
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE, priv);
        byte[] ciphetText = cipher.doFinal(s);
        return ciphetText;
    }

    public byte[] getEncodedPublicKey() throws IOException, Exception
    {
//        byte[] encoded = Files.readAllBytes(Paths.get(KEYSTORE_ADDRESS + MY_PUBLIC_KEY_NAME + ".pub"));
        KeyFactory factory = KeyFactory.getInstance("RSA", "BC");
        PublicKey pub = Loadkey.generatePublicKey(factory, KEYSTORE_ADDRESS + MY_PUBLIC_KEY_NAME+ ".pub");
        return pub.getEncoded();
    }
    
    public byte[] encryptWithServerPublic(String s) throws Exception
    {
        KeyFactory factory = KeyFactory.getInstance("RSA", "BC");
        PublicKey key = Loadkey.generatePublicKey(factory, KEYSTORE_ADDRESS + SERVER_PUBLIC_KEY_NAME + ".pub");
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(s.getBytes());
    }
    
    public byte[] decryptWithServerPublic(byte[] s) throws Exception
    {
        KeyFactory factory = KeyFactory.getInstance("RSA", "BC");
        PublicKey key = Loadkey.generatePublicKey(factory, KEYSTORE_ADDRESS + SERVER_PUBLIC_KEY_NAME + ".pub");
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(s);
    }
    
}
