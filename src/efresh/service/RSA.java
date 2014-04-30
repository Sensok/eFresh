package efresh.service;

import java.math.BigInteger;

/**
 * This is an encryption class for RSA
 * @author Adam Harper
 */
public class RSA
{

   /**
    * Big Int for RSA
    */
   private final static BigInteger one = new BigInteger("1");

   /**
    * Big int encrypted message
    */
   private BigInteger encryptedMessage;

   /**
    * Decrypted message
    */
   private BigInteger decryptedMessage;

   /**
    * Private Key
    */
   private BigInteger privateKey;

   /**
    * Key for RSA
    */
   private BigInteger publicKey;

   /**
    * Modulus number
    */
   private BigInteger modulus;

   /**
    * RSAQ Key for RSA
    */
   private String mRsaQ;

   /**
    * RSA Public Key to use
    */
   private String mPublicKey;
   /**
    * Creates a new RSA object.
    *
    * @param N BigInteger number
    */
   public RSA(BigInteger N)
   {
      mRsaQ = "9223";
      mPublicKey = "65537";
      BigInteger p = N;
      BigInteger q = new BigInteger(mRsaQ);
      BigInteger phi = (p.subtract(one)).multiply(q.subtract(one));

      modulus = p.multiply(q);
      publicKey = new BigInteger(mPublicKey);
      privateKey = publicKey.modInverse(phi);
   }

   /**
    * Encrypts message
    *
    * @param message to encrypt
    *
    * @return Number of encrypted message
    */
   public BigInteger encrypt(BigInteger message)
   {
      return message.modPow(publicKey, modulus);
   }

   /**
    * Decrypts big Int
    *
    * @param encrypted The number to decrypt
    *
    * @return BigInt - decrypted number
    */
   public BigInteger decrypt(BigInteger encrypted)
   {
      return encrypted.modPow(privateKey, modulus);
   }

   /**
    * puts the keys and modulus to strings
    *
    * @return string of keys
    */
   @Override
   public String toString()
   {
      String s = "";
      s += ("public  = " + publicKey + "\n");
      s += ("private = " + privateKey + "\n");
      s += ("modulus = " + modulus);

      return s;
   }

   /**
    * prepares the string to encrypt
    *
    * @param pEncrypt to encrypt
    *
    * @return Big Int of prep encrypt
    */
   public BigInteger prepare(String pEncrypt)
   {
      int stringVal = 0;

      for (int i = 0; i < pEncrypt.length(); i++)
      {
         stringVal += ((pEncrypt.charAt(i) - 'A') * i);
      }

      String stringValue = Integer.toString(stringVal);

      return new BigInteger(stringValue);
   }
}
