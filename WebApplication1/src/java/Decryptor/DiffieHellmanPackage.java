/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Decryptor;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 *
 * @author th
 */
public class DiffieHellmanPackage
{
    protected PrivateKey privateKey;
    private PublicKey  publicKey;
    private PublicKey  receivedPublicKey;


    public PublicKey getPublicKey()
    {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey)
    {
        this.publicKey = publicKey;
    }

    public PublicKey getReceivedPublicKey()
    {
        return receivedPublicKey;
    }

    public void setReceivedPublicKey(PublicKey receivedPublicKey)
    {
        this.receivedPublicKey = receivedPublicKey;
    }
    
    
}
