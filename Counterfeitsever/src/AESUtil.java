

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.*;

public class AESUtil {

     private static final String ALGO = "AES";
   
     
     public static String getAKey()
     {
            StringBuffer pairkey = new StringBuffer();
            for (int i=0;i<16;i++)
            {
                 //int num = did.charAt(i)-'0';
                int num = (int)(Math.random()*100);
                 num = (num)%9;
                 char nn = (char )('0' + num);
                 pairkey.append(nn);
                
            }
            String keyi=pairkey.toString();
            return keyi;
         
     }

   public static String encrypt(String Data,String keyv) throws Exception {
        Key key = generateKey(keyv);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedValue = new BASE64Encoder().encode(encVal);
        return encryptedValue;
    }

    public static String decrypt(String encryptedData,String keyv) throws Exception {
        Key key = generateKey(keyv);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
        //return decValue;
    }
    private static Key generateKey(String keyv) throws Exception {
        
        //byte [] keyValue = keys.getBytes();
        Key key = new SecretKeySpec(keyv.getBytes(), ALGO);
        return key;
    }

    public static void main(String [] arg)
    {
        
          
            
            String keyi =getAKey();
            try
            {
              String en=encrypt("101",keyi);
            
              System.out.println("Encrypted value is " + en);
              
              String de = decrypt(en,keyi);
              
              System.out.println("Decrypted value is " + de);
        
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        
        
    }
    
}

