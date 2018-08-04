
package org.kwick.model;

import java.io.ByteArrayOutputStream;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.security.sasl.AuthenticationException;
import javax.smartcardio.CardException;
import org.kwick.Util.JavaCardUtil;
import org.kwick.config.Config;
import org.kwick.view.Shell;

/**
 *
 * @author Dell
 */
public class Authenticate 
{
    byte[] securityDomain;

    private static byte[] keyDiversification = new byte[10];
    private static byte[] keyInformation = new byte[2];
    private static byte[] sequenceCounter = new byte[2];
    private static byte[] cardChallenge = new byte[6];
    private static byte[] cardCryptogram = new byte[8];
    private static byte[] hostChallenge = new byte[8];
    private static byte[] hostCryptogram = new byte[8];
    private static byte[] icvNextCommand;
    
    private static byte[] sessionCMAC;
    private static byte[] sessionDEK;
    private static byte[] sessionENC;
    private static byte[] sessionRMAC;

    private static byte[] baseKey ; //hexStringToByteArray("404142434445464748494a4b4c4d4e4f4041424344454647");
    private static byte[] newKey ; //hexStringToByteArray("505152535455565758595a5b5c5d5e5f");
    private static byte[] CMAC;
    private static boolean isAu = false;
 
/*******************************************************************************/    
    public Authenticate()
    {
        securityDomain = JavaCardUtil.hexStringToByteArray(Config.cardM);
    }
/*******************************************************************************/
    public static boolean isAuth()
    {
        return isAu;
    }
/*******************************************************************************/    
    public boolean autenticate()
    {
        boolean isAuth = false;
        try 
        {
             String statusWords;
            if (JavaCard.sendApdu(selectCM()))
            {
                 StatusHandler.checkStatus();
                 statusWords = Integer.toHexString(JavaCard.getStatusWords());
                 if (!statusWords.equalsIgnoreCase(StatusHandler._NO_ERROR))
                 {                     
                     Shell.printMessage("Coud not select Card Manager...", true);
                     if (JavaCard.sendApdu(selectWithoutAID()))
                     {
                        StatusHandler.checkStatus();
                        statusWords = Integer.toHexString(JavaCard.getStatusWords()); 
                        if (!statusWords.equalsIgnoreCase(StatusHandler._NO_ERROR))
                        {
                            isAuth = false;
                            Shell.printMessage("Coud not select Card Manager...", true);
                            return isAuth;
                        }
                        else
                        {
                            isAuth = true;
                        }
                     } //end-if
                     else
                     {
                         return false;
                     }
                 } //end-if
                 else                      
                 {
                    try
                    {
                        if (JavaCard.sendApdu(getInitUpdateAPDU()))
                        {
                            StatusHandler.checkStatus();
                            statusWords = Integer.toHexString(JavaCard.getStatusWords());
                            if (!statusWords.equalsIgnoreCase(StatusHandler._NO_ERROR))
                            {
                                Shell.printMessage("Aborting...", true);
                                return false;
                            }
                            else if (statusWords.equalsIgnoreCase(StatusHandler._NO_ERROR))
                            {
                                if(!filterInitUpdateResponse(JavaCard.getData()))
                                {
                                    Shell.printMessage("System Internal Error. Could not parse Init-Update\n Aborting...", true);
                                    return false;
                                }
                                
                                if (generateSessionKeys())
                                {
                                   // System.out.println("Session keys are generated");
                                    byte[] cardSig = cbcMACSignature(getCardCryptogram(), sessionENC);
                                    String signatureHexString = JavaCardUtil.byteArrayToHexString(cardSig);
                                    if (signatureHexString.equalsIgnoreCase((JavaCardUtil.byteArrayToHexString(cardCryptogram)))) 
                                    {
                                       // System.out.println("signature is :" + signatureHexString + "\ncardCryptogram is :" + (JavaCardUtil.byteArrayToHexString(cardCryptogram)) + " \nCard cryptogram authenticated");                             
                                        if (JavaCard.sendApdu(gettExtAuthAPDU()))
                                        {
                                            StatusHandler.checkStatus();
                                            statusWords = Integer.toHexString(JavaCard.getStatusWords());
                                            if (!statusWords.equalsIgnoreCase(StatusHandler._NO_ERROR))
                                            {
                                                isAu = false;
                                                Shell.printMessage("Authentication Failed.Aborting...", true);
                                                return false;
                                            }
                                            
                                            isAu = true;
                                        }
                                        else
                                        {
                                            isAu = false;
                                            Shell.printMessage("Could not send Ext-Auth APDU. Aborting...", true);
                                            return false;
                                        }
                                    }
                                    else
                                    {
                                       // System.out.println("signature is :" + signatureHexString + "\ncardCryptogram is :" + (JavaCardUtil.byteArrayToHexString(cardCryptogram)) + " \nCard cryptogram not authenticated");                                 
                                        Shell.printMessage("Authentication failed. Aborting...", true);
                                        return false;
                                    }
                                }
                                else
                                {
                                  isAu = false;
                                  Shell.printMessage("System Internal Error. System could not generate keys./n Aborting...", true);
                                  return false;  
                                }
                            }
                        } 
                    }
                    
                    catch (Exception ex) 
                    {
                        isAu = false;
                        Shell.printMessage(ex.getMessage(), true);
                    }                 
                 }//end-else
            }
            else
            {
                isAu = false;
                Shell.printMessage("Could not select Card Manager", true);
                return false;
            }
        } //end try
        catch (CardException | IllegalArgumentException ex) 
        {
            isAu = false;
            Shell.printMessage("Card Error: \n"+ex.getMessage(), false);
            isAuth = false;
        }
        
        return isAuth;
    }
/*******************************************************************************/
     public byte[] selectCM() {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
        baos.reset();
        baos.write((byte) 0x00);
        baos.write((byte) 0xA4);
        baos.write((byte) 0x04);
        baos.write((byte) 0x00);
        baos.write((byte) securityDomain.length);
        baos.write(securityDomain);
        baos.write((byte)0x00);
    } catch (Exception ex) {
        System.out.println(""+ex.toString());
    }
    Shell.displayCommand(JavaCardUtil.htos(baos.toByteArray()));
    return baos.toByteArray();
    }
/*******************************************************************************/
     public static byte[] getInitUpdateAPDU() throws Exception {
        SecureRandom chal = new SecureRandom();
        hostChallenge = new byte[8];
        chal.nextBytes(hostChallenge);
        Shell.displayCommand(JavaCardUtil.htos(JavaCardUtil.hexStringToByteArray( "8050000008" + JavaCardUtil.byteArrayToHexString(hostChallenge) + "00" )));
        return JavaCardUtil.hexStringToByteArray( "8050000008" + JavaCardUtil.byteArrayToHexString(hostChallenge) + "00" );
 
    }
/*******************************************************************************/
     public static byte[] gettExtAuthAPDU() throws Exception 
        {
           // System.out.println("Calculating and Verifying Host Cryptogram...");
            hostCryptogram = getHostCryptogram();
            String APDUhex = ("8482030008"+JavaCardUtil.byteArrayToHexString(hostCryptogram));
            CMAC = generateCMac2(JavaCardUtil.hexStringToByteArray(APDUhex), sessionCMAC, 
                                                      new byte[]{0, 0, 0, 0, 0, 0, 0, 0});
            //System.out.println("hostCryptogram is :" + JavaCardUtil.byteArrayToHexString(hostCryptogram));
            Shell.displayCommand(JavaCardUtil.htos(JavaCardUtil.hexStringToByteArray("8482030010" + JavaCardUtil.byteArrayToHexString(hostCryptogram) + JavaCardUtil.byteArrayToHexString(CMAC))));
            return JavaCardUtil.hexStringToByteArray("8482030010" + JavaCardUtil.byteArrayToHexString(hostCryptogram) + JavaCardUtil.byteArrayToHexString(CMAC));
        }
/*******************************************************************************/ 
     public byte[] selectWithoutAID() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try 
        {
            baos.reset();
            baos.write((byte) 0x00);
            baos.write((byte) 0xA4);
            baos.write((byte) 0x04);
            baos.write((byte) 0x00);
            baos.write((byte)0x00);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Shell.displayCommand(JavaCardUtil.htos(baos.toByteArray()));
        return baos.toByteArray();
    }
/*******************************************************************************/
     public boolean filterInitUpdateResponse(byte[] response)
      {
          try
          {
            System.out.println("Init Response length:"+ response.length);
            keyDiversification = new byte[10];
            keyInformation = new byte[2];
            sequenceCounter = new byte[2];
            cardChallenge = new byte[6];
            cardCryptogram = new byte[8];
            
            System.arraycopy(response, 0, keyDiversification, 0, 10);
            System.arraycopy(response, 10, keyInformation, 0, 2);
            System.arraycopy(response, 12, sequenceCounter, 0, 2);
            System.arraycopy(response, 14, cardChallenge, 0, 6);
            System.arraycopy(response, 20, cardCryptogram, 0, 8);
          
//            System.out.println("keyDiversification: " + JavaCardUtil.byteArrayToHexString(keyDiversification));
//            System.out.println("keyInformation: " + JavaCardUtil.byteArrayToHexString(keyInformation));
//            System.out.println("sequenceCounter: " + JavaCardUtil.byteArrayToHexString(sequenceCounter));
//            System.out.println("cardChallenge: " + JavaCardUtil.byteArrayToHexString(cardChallenge));
//            System.out.println("cardCryptogram: " + JavaCardUtil.byteArrayToHexString(cardCryptogram));
          }
          catch( Exception ex)
          {
              ex.printStackTrace();
              return false;
          }
          return true;
      }
/*******************************************************************************/
      public byte[] getBaseKey()
      {
          baseKey = new byte[24];
        // System.out.println("Current Key[0] = "+Config.currentKeys[0] +": Length = "+JavaCardUtil.hexStringToByteArray(Config.currentKeys[0]).length);
         int length = JavaCardUtil.hexStringToByteArray(Config.currentKeys[0]).length;
         System.arraycopy(JavaCardUtil.hexStringToByteArray(Config.currentKeys[0]), 0, baseKey, 0, length);
         System.arraycopy(JavaCardUtil.hexStringToByteArray(Config.currentKeys[0]), 0, baseKey, length, length/2);
        //  System.out.println("Base Key:"+ JavaCardUtil.htos(baseKey));
          //baseKey = JavaCardUtil.hexStringToByteArray("404142434445464748494a4b4c4d4e4f4041424344454647");
         return baseKey;
      }
/*******************************************************************************/
      public static byte[] deriveEncryptionCBC(byte[] keyData, byte[] data) throws GeneralSecurityException 
      {
         // System.out.println("In Derivate Encruption");
        //Key key = getSecretKey(keyData);
        if (keyData.length == 16) 
        {
            byte[] temp = (byte[]) keyData.clone();
            keyData = new byte[24];
            System.arraycopy(temp, 0, keyData, 0, temp.length);
            System.arraycopy(temp, 0, keyData, 16, 8);
        }
        SecretKey secretKey = new SecretKeySpec(keyData, "DESede");
        IvParameterSpec dps =
                new IvParameterSpec(new byte[]{0, 0, 0, 0, 0, 0, 0, 0});

        String algorithm = "DESede/CBC/NoPadding";
        Cipher desedeCBCCipher = Cipher.getInstance(algorithm);
        desedeCBCCipher.init(Cipher.ENCRYPT_MODE, secretKey, dps);

        byte[] result = desedeCBCCipher.doFinal(data);
        //adjustParity(result);

        return result;
    }
/*******************************************************************************/    
      public boolean generateSessionKeys()
      {
        try 
        {
            byte[] derData = {(byte)0x01,(byte)0x00, sequenceCounter[0],sequenceCounter[1],
                              (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
                              (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
                              (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
            
            derData [1] = (byte)0x01;
           // System.out.println("in generate session keys");
            //Shell.printMessage("Base Key: \n"+JavaCardUtil.htos(getBaseKey()), false);
            sessionCMAC = deriveEncryptionCBC(getBaseKey(), derData);
           // System.out.println("Sesstion MAC: "+JavaCardUtil.htos(sessionCMAC));
            //Shell.printMessage("Sesstion MAC: \n"+JavaCardUtil.htos(sessionCMAC), false);
            
            derData[1] = (byte)0x02;
            
            sessionRMAC = deriveEncryptionCBC(getBaseKey(), derData);
          //  System.out.println("Sesstion RMAC:"+JavaCardUtil.htos(sessionRMAC));
            //Shell.printMessage("Sesstion RMAC: \n"+JavaCardUtil.htos(sessionRMAC), false);
            
            derData[1] = (byte)0x82;
            sessionENC = deriveEncryptionCBC(getBaseKey(), derData);
            //System.out.println("Sesstion ENC: "+JavaCardUtil.htos(sessionENC));
            //Shell.printMessage("Sesstion ENC: \n"+JavaCardUtil.htos(sessionENC), false);
            
            derData[1] = (byte)0x81;
            sessionDEK = deriveEncryptionCBC(getBaseKey(), derData);
            //System.out.println("Sesstion DEK: "+JavaCardUtil.htos(sessionDEK));
            //Shell.printMessage("Sesstion DEK: \n"+JavaCardUtil.htos(sessionDEK), false);
        
        } catch (GeneralSecurityException ex) {
            ex.printStackTrace();
            Shell.printMessage("Could not generate session keys\nAborting...", true);
            return false;
        }
        return true;
      }
/*******************************************************************************/    
        public static byte[] cbcMACSignature(byte[] data, byte[] sessionSENC) throws AuthenticationException {
        IvParameterSpec params =
                new IvParameterSpec(new byte[]{(byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0});
 
        if (sessionSENC.length == 16) {
            byte[] temp = (byte[]) sessionSENC.clone();
            sessionSENC = new byte[24];
            System.arraycopy(temp, 0, sessionSENC, 0, temp.length);
            System.arraycopy(temp, 0, sessionSENC, 16, 8);
        }
 
        byte[] temp = null;
        SecretKey secretKey = new SecretKeySpec(sessionSENC, "DESede");
        try {
            Cipher cbcDES = Cipher.getInstance("DESede/CBC/NoPadding");
            cbcDES.init(Cipher.ENCRYPT_MODE, secretKey, params);
 
            temp = cbcDES.doFinal(data);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
 
        byte[] signature = new byte[8];
        System.arraycopy(temp, temp.length - 8, signature, 0, signature.length);
        return signature;
    }
/*******************************************************************************/
        public static byte[] getCardCryptogram() throws Exception
        {
            byte[] cardCrypto = new byte[24];
            String desPadding = "8000000000000000";
            System.arraycopy(hostChallenge, 0, cardCrypto, 0, hostChallenge.length);
            System.arraycopy(sequenceCounter, 0, cardCrypto, hostChallenge.length, sequenceCounter.length);
            System.arraycopy(cardChallenge, 0, cardCrypto, hostChallenge.length+sequenceCounter.length, cardChallenge.length);
            System.arraycopy(JavaCardUtil.hexStringToByteArray(desPadding), 0, cardCrypto, (hostChallenge.length+sequenceCounter.length+cardChallenge.length), desPadding.length()/2); 
           // System.out.println("\nCard Crypto: "+JavaCardUtil.byteArrayToHexString(cardCrypto));
            return cardCrypto;
        }
/*******************************************************************************/
        public static byte[] getHostCryptogram() throws AuthenticationException
        {
            byte[] hostCrypto = new byte[24];
            String desPadding = "8000000000000000";
            System.arraycopy(sequenceCounter, 0, hostCrypto, 0, sequenceCounter.length);
            System.arraycopy(cardChallenge, 0, hostCrypto, sequenceCounter.length, cardChallenge.length);
            System.arraycopy(hostChallenge, 0, hostCrypto, (sequenceCounter.length+cardChallenge.length), hostChallenge.length);
            System.arraycopy(JavaCardUtil.hexStringToByteArray(desPadding), 0, hostCrypto, (hostChallenge.length+sequenceCounter.length+cardChallenge.length), desPadding.length()/2); 
            hostCryptogram = new byte[8];
            hostCryptogram = cbcMACSignature( hostCrypto, sessionENC);
            return hostCryptogram;
        }
/*******************************************************************************/  
        public static byte[] generateCMac2(byte[] APDU, byte[] SMacSessionKey, byte[] icv) throws GeneralSecurityException, Exception 
        {
            if (SMacSessionKey.length == 16) {
                byte[] temp = (byte[]) SMacSessionKey.clone();
                SMacSessionKey = new byte[24];
                System.arraycopy(temp, 0, SMacSessionKey, 0, temp.length);
                System.arraycopy(temp, 0, SMacSessionKey, 16, 8);
            }
            byte[] cMac = new byte[8];
            int length =  APDU[4] & 0xFF;
            
            byte[] dataField = new byte[length];
            System.arraycopy(APDU, 5, dataField, 0, length);
            
            byte[] padding = {(byte) 0x80, 0, 0, 0, 0, 0, 0, 0};
            int paddingRequired = 8 - (5 + dataField.length) % 8;
            byte[] data = new byte[APDU.length + paddingRequired];

            //Build APDU
            System.arraycopy(APDU, 0, data, 0, 4);
            data[4] = (byte) ((byte) length + (byte) 0x08);
            System.arraycopy(dataField, 0, data, 5, dataField.length);
            System.arraycopy(padding, 0, data, 5 + dataField.length, paddingRequired);

            //System.out.println("data to calculate mac :" + JavaCardUtil.byteArrayToHexString(data));

            //System.out.println("icv to calculate mac :" + JavaCardUtil.byteArrayToHexString(icv));

            Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding");

            Cipher singleDesCipher = Cipher.getInstance("DES/CBC/NoPadding", "SunJCE");
            SecretKeySpec desSingleKey = new SecretKeySpec(SMacSessionKey, 0, 8, "DES");
            SecretKey secretKey = new SecretKeySpec(SMacSessionKey, "DESede");
            //Calculate the first n - 1 block. For this case, n = 1
            IvParameterSpec ivSpec = new IvParameterSpec(icv);
            singleDesCipher.init(Cipher.ENCRYPT_MODE, desSingleKey, ivSpec);
            byte ivForLastBlock[] = singleDesCipher.doFinal(data, 0, 8);

            int blocks = data.length / 8;

            for (int i = 0; i < blocks - 1; i++) {
                singleDesCipher.init(Cipher.ENCRYPT_MODE, desSingleKey, ivSpec);
                byte[] block = singleDesCipher.doFinal(data, i * 8, 8);
                ivSpec = new IvParameterSpec(block);
            }

            int offset = (blocks - 1) * 8;

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            cMac = cipher.doFinal(data, offset, 8);


            ivSpec = new IvParameterSpec(new byte[8]);

            singleDesCipher.init(Cipher.ENCRYPT_MODE, desSingleKey, ivSpec);
            icvNextCommand = singleDesCipher.doFinal(cMac);

            return cMac;
    }
/******************************************************************************/
        public static byte[] getSCP02APDU(byte[] plainAPDU) throws Exception 
        {
            byte[] data;
            byte[] enAPDU;
            int Lc = 0;
            
            Lc = plainAPDU[4] & 0xFF;
            data = new byte[Lc];
            System.arraycopy(plainAPDU, 5, data, 0, Lc);
            
            //testing newKey
            String dataField = JavaCardUtil.byteArrayToHexString(data);
            // String dataField2 = "01" + "8010" + byteArrayToHexString(newKey) + "03" + keyCheckValue + "8010" + byteArrayToHexString(newKey) + "03" + keyCheckValue + "8010" + byteArrayToHexString(newKey) + "03" + keyCheckValue;
           // System.out.println("datafield to calculate cmac :" + dataField);
           // System.out.println("icv to calculate cmac is previous mac first 8 byte sessionCMAC in CBC single des :" + JavaCardUtil.byteArrayToHexString(icvNextCommand));

            CMAC = generateCMac2(plainAPDU, sessionCMAC, icvNextCommand);

            //System.out.println("data field with des padding for encryption (encryption in CBC sessionENC) :" + desPadding(dataField));
            byte[] encrytedData = deriveEncryptionCBC(sessionENC, JavaCardUtil.hexStringToByteArray(desPadding(dataField)));
            String dataField3 = JavaCardUtil.byteArrayToHexString(encrytedData);
            //System.out.println("data field after encryption :" + dataField3);

            int CMACLen = JavaCardUtil.byteArrayToHexString(CMAC).length() / 2;
            //System.out.println("CMACLen :" + CMACLen);
            int dataFieldLen = dataField3.length() / 2;
            //System.out.println("dataFieldLen :" + dataFieldLen);
            int intLc = dataFieldLen + CMACLen;
            //System.out.println("intLc :" + intLc);
            String hexLc = Integer.toString(intLc, 16);
           // System.out.println("hexLc :" + hexLc);
            enAPDU = new byte[5 + intLc + 1];
            System.arraycopy(plainAPDU, 0, enAPDU, 0, 4);
            enAPDU[4] = (byte)intLc;
            System.arraycopy(encrytedData, 0, enAPDU, 5, encrytedData.length);
            System.arraycopy(CMAC, 0, enAPDU, 5+encrytedData.length, CMAC.length);
            enAPDU[enAPDU.length-1] = (byte)0x00;
            
            //System.out.println("Complete APDU: "+ JavaCardUtil.byteArrayToHexString(enAPDU));
            //System.out.println("APDU Length in SCP02: "+JavaCardUtil.byteArrayToHexString(enAPDU).length()/2+"---LC: "+(enAPDU[4] & 0xFF));
            return enAPDU;
    }
/******************************************************************************/
     public static byte[] deriveEncryptionECB(byte[] keyData, byte[] data) throws GeneralSecurityException 
     {
        //Key key = getSecretKey(keyData);
        if (keyData.length == 16) {
            byte[] temp = (byte[]) keyData.clone();
            keyData = new byte[24];
            System.arraycopy(temp, 0, keyData, 0, temp.length);
            System.arraycopy(temp, 0, keyData, 16, 8);
        }
        SecretKey secretKey = new SecretKeySpec(keyData, "DESede");
        String algorithm = "DESede/ECB/NoPadding";
        Cipher desedeCBCCipher = Cipher.getInstance(algorithm);
        desedeCBCCipher.init(Cipher.ENCRYPT_MODE, secretKey);
 
        byte[] result = desedeCBCCipher.doFinal(data);
        //adjustParity(result);
 
        return result;
    }
/******************************************************************************/
        public static String desPadding(String hexString) {
 
 
        //System.out.println("String to pad before:" + hexString);
        hexString = hexString + "80";
 
        int hexStringLen = hexString.length() / 2;
 
        int padding = 8 - (hexStringLen % 8);
 
        for (int i = 0; i < padding; i++) {
            hexString = hexString + "00";
        }
        //System.out.println("String to pad after :" + hexString);
        return hexString;
    }
}
