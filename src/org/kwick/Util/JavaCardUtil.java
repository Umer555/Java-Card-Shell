/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kwick.Util;

import sun.security.util.BigInt;

/**
 *
 * @author Dell
 */
public class JavaCardUtil {

    static String message = "";
    public static BigInt SCUCCESS = new BigInt(0x9000);
    public static BigInt INS_NOTSUPPORTED = new BigInt(0x6D00);
    public static BigInt CLA_NOTSUPPORTED = new BigInt(0x6E00);
    public static BigInt UNCAUGHT_EXCEPTION = new BigInt(0x6700);

    public static String getMessage() {
        return message;
    }

    /**
     * ***************************************************************************
     */
//    public static String byteArrayToHexString(byte[] byteArray) {
//        final byte[] HEXBYTES = {(byte) '0', (byte) '1', (byte) '2',
//            (byte) '3', (byte) '4', (byte) '5',
//            (byte) '6', (byte) '7', (byte) '8',
//            (byte) '9', (byte) 'a', (byte) 'b',
//            (byte) 'c', (byte) 'd', (byte) 'e',
//            (byte) 'f'};
//        int len = byteArray.length;
//        char[] hexDecimal = new char[len * 2];
//
//        for (int i = 0, j = 0; i < len; i++) {
//            int c = ((int) byteArray[i]) & 0xff;
//
//            hexDecimal[j++] = (char) HEXBYTES[c >> 4 & 0xf];
//            hexDecimal[j++] = (char) HEXBYTES[c & 0xf];
//        }
//        return new String(hexDecimal);
//    }
/*******************************************************************************/    
        public static String byteArrayToHexString(byte[] b){
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result +=
                    Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }
/*******************************************************************************/            
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
    /**
     * ***************************************************************************
     */
    public static byte[] getStringToByteArray(String textCommand) {
        String command = textCommand;
        String[] stringArray = command.split("\\.");
        byte[] byteArray = new byte[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            int hex = Integer.parseInt(stringArray[i], 16);
            byteArray[i] = (byte) hex;
        }

        return byteArray;
    }

    /**
     * ***************************************************************************
     */
    public static boolean validateCmd(String cmd) {
        boolean isValidated = true;        
        String command = cmd;
        byte[] byteCommand = JavaCardUtil.hexStringToByteArray(command);
        if (byteCommand.length < 5) {
            isValidated = false;
            message = "Invalid APDU: Lengh too short";
        } else if (byteCommand.length > 261) 
        {
            message = "Invalid APDU: Lengh too High";
        } else if (!checkFormat(command)) 
        {
            isValidated = false;
            message = "command format not OK";
        }
        return isValidated;
    }

    /**
     * ***************************************************************************
     */
    private static boolean checkFormat(String cmd) {
        boolean isGood = true;
        if((cmd.length()%2) != 0) 
        {            
            isGood = false;
        }
        return isGood;
    }

    /**
     * ***************************************************************************
     */
    public static String getFormatedData(String data) {
        char[] outPut = data.toCharArray();
        String newData = "";
        for (int i = 0; i < outPut.length; i++) {
            newData += "" + outPut[i];
            if ((i + 1) % 2 == 0) {
                if (i != outPut.length - 1) {
                    newData += ".";
                }
            }
            if ((i + 1) % 15 == 0) {
                newData += "\n";
            }

        }
        return newData;
    }

    /**
     * ***************************************************************************
     */
    public static String getFormatedCmd(String cmd) {
        String newData = "";
        char[] outPut = cmd.toCharArray();
        for (int i = 0; i < outPut.length; i++) {
            newData += "" + outPut[i];
            if ((i + 1) % 51 == 0) {
                newData = newData.substring(0, newData.length() - 1);
                newData += "\n        ";
            }

        }
        return newData;
    }

    /**
     * ***************************************************************************
     */
    public static int byteToInt(byte[] b) {
        int val = 0;
        for (int i = b.length - 1, j = 0; i >= 0; i--, j++) {
            val += (b[i] & 0xff) << (8 * j);
        }
        return val;
    }

    /**
     * ***************************************************************************
     */
    public static byte[] toLengthOctets(int i) {
        byte abyte0[] = null;
        if (i < 128) {
            abyte0 = new byte[1];
            abyte0[0] = (byte) i;
        } else {
            byte abyte1[] = intToBytes(i);
            abyte0 = new byte[1 + abyte1.length];
            System.arraycopy(abyte1, 0, abyte0, 1, abyte1.length);
            abyte0[0] = (byte) (abyte1.length | 0x80);
        }
        return abyte0;
    }

    /**
     * ***************************************************************************
     */
    public static byte[] intToBytes(int i) {
        int j = (Integer.toHexString(i).length() + 1) / 2;
        byte abyte0[] = new byte[j];
        for (int k = 0; k < j; k++) {
            abyte0[k] = (byte) (i >>> 8 * (j - 1 - k) & 0xff);
        }

        return abyte0;
    }

    /**
     * ***************************************************************************
     */
    public static String htos(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String tmp = Integer.toHexString(((int) bytes[i]) & 0xFF);
            while (tmp.length() < 2) {
                tmp = "0" + tmp;
            }
            if (i != bytes.length - 1) {
                sb.append(tmp).append(" ");
            } else {
                sb.append(tmp);
            }
            if(((i+1)%17) == 0)
            {
                sb.append("\n");
                sb.append("        ");
            }

        }
        return sb.toString().toUpperCase();
    }
}
/******************************************************************************/