package com.R07r35cInc.util;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.*;
import java.time.LocalDateTime;
import java.util.Base64;


/**
 * ECDSA Digital Signature Verification
 *
 * @author Cleber Lecheta
 * https://encontreiJesus.com
 */
public class ECDSA {

    private final ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");

    private KeyPairGenerator generator;
    private KeyPair keyPair;
    private Signature ecdsaSign;

    public ECDSA() {
        try {
            generator = KeyPairGenerator.getInstance("EC");
            ecdsaSign = Signature.getInstance("SHA256withECDSA");
        } catch (NoSuchAlgorithmException e) {
            System.out.println(LocalDateTime.now() + " ERROR [ECDSA] " + e.getMessage());
        }
    }

    /**
     * Every call return a new KeyPair object where you can
     * access getPublic() and getPrivate() methods to obtain keys
     * Also set/configure the utility class with the new generated class
     */
    public KeyPair genKeyPair() {
        try {
            generator.initialize(ecSpec, new SecureRandom());
            this.keyPair = generator.genKeyPair();
        } catch (InvalidAlgorithmParameterException e) {
            System.out.println(LocalDateTime.now() + " ERROR [ECDSA] " + e.getMessage());
        }
        return this.keyPair;
    }

    /**
     * You can use the generated Key or set an external key to use within this utility class methods
     */
    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    /**
     * Returns a single value for the EC publicKey that we can use as wallet address
     */
    public String getPublic() {
        return Base64.getEncoder().encodeToString(this.keyPair.getPublic().getEncoded());
    }

    /**
     * Signs data with the private key
     */
    public String sign(String data) {
        byte[] signature = null;
        try {
            ecdsaSign.initSign(this.keyPair.getPrivate()); //publicKey
            ecdsaSign.update(data.getBytes(StandardCharsets.UTF_8)); //data
            signature = ecdsaSign.sign(); //signature
        } catch (InvalidKeyException | SignatureException e) {
            System.out.println(LocalDateTime.now() + " ERROR [ECDSA] " + e.getMessage());
        }
        return Base64.getEncoder().encodeToString(signature);
    }

    /**
     * If the signature, publicKey or the underling data is modified in anyway,
     * the decription will be off and the drivin data isn't gonna match the proposed data
     */
    public Boolean verifySignature(String publicKey, String data, String signature) {
        boolean isValid = false;
        try {
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            PublicKey publicKeyObject = keyFactory.generatePublic(publicKeySpec);
            ecdsaSign.initVerify(publicKeyObject);
            ecdsaSign.update(data.getBytes(StandardCharsets.UTF_8));
            isValid = ecdsaSign.verify(Base64.getDecoder().decode(signature));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException
                | SignatureException e) {
            System.out.println(LocalDateTime.now() + " ERROR [ECDSA] " + e.getMessage());
        }
        return isValid;
    }

    public static ECPublicKey loadPublicKey(String pubKey) {
        try {
            byte[] encoded = Base64.getDecoder().decode(pubKey);
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            return (ECPublicKey)keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            System.out.println(LocalDateTime.now() + " ERROR [ECDSA] Failed to load public key " + e.getMessage());
        }
        return null;
    }

    public static ECPrivateKey loadPrivateKey(byte[] privKey) {
        try {
            //byte[] encoded = Base64.getDecoder().decode(privKey);
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privKey);
            return  (ECPrivateKey)keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            System.out.println(LocalDateTime.now() + " ERROR [ECDSA] Failed to load private key " + e.getMessage());
        }
        return null;
    }

}