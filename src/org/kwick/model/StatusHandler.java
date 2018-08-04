package org.kwick.model;

import org.kwick.Util.JavaCardUtil;
import org.kwick.view.Shell;

/**
 *
 * @author Dell
 */
public class StatusHandler 
{
    public static String message = "";
    public static final String _NO_ERROR =  "9000";
    
    public static final String _WRITE_PROBLEM =  "9202";
    
    public static final String _KEY_FILE_SELECTION_ERROR =  "9408";
    public static final String _ALGO_NOT_SUPPORTED =  "9484";
    public static final String _INVALID_KCV =  "9485";
    
    public static final String _PIN_DISABLED =  "9808";
    public static final String _CHV_NOT_INITIALIZED =  "9802";
    public static final String _WRONG_KEY =  "9804";
    public static final String _CHV_BLOCKED =  "9840";
    
    public static final String _WRONG_LENGTH_IN_SECURE_MESSIGING = "6C40";
    
    public static final String _CHANNEL_CLOSED_WARNING = "6200";
    public static final String _CARD_LOCKED = "6283";
    public static final String _FILE_DISCRIPTER_ERROR = "6284";
    public static final String _IADF_ERROR = "6281";
    
    public static final String _MORE_DATA_AVAIABLE = "6310";
    public static final String _AUTHENTICATION_FAILED = "6300";
    
    public static final String _WRONG_PK_CRYPTO_COMMAND = "6400";
    
    public static final String _INS_NOTSUPPORTED = "6D00";
    public static final String _CLA_NOTSUPPORTED = "6E00";
    public static final String _NO_DIAGNOS = "6F00";
    
    public static final String _MEMORY_FAILURE = "6581";
    
    public static final String _UNCAUGHT_EXCEPTION = "6700";    

    public static final String _SECURE_MSG_NOTSUPPORTED = "6882";
    public static final String _LOGICAL_CHANNEL_NOT_SUPPORTED = "6881";
    
    public static final String _APPLET_SELECTION_FAILED = "6999";       
    public static final String _NOT_ENOUGH_SPACE_IN_FILE = "6984";
    public static final String _SECURITY_CONDITION_NOT_SATISFIED = "6982";
    public static final String _CONDITION_NOT_SATISFIED = "6985";
    
    public static final String _INCORRECT_VALUES = "6A80";
    public static final String _FUNCTION_NOT_SUPPORTED = "6A81";
    public static final String _APP_NOTFOUND = "6A82";
    public static final String _RECORD_NOTFOUND = "6A83";
    public static final String _NOT_ENOUGH_MEMORY = "6A84";
    public static final String _REF_DATA_NOTFOUND = "6A88";
    
    public static final String _WRONG_PAREAMETERS = "6B00";
    
    public static String getMessage()
    {        
        return message;
    }
    
    public static boolean analyseStatus( String statusWord)
    {
        boolean isFound = true;

        
            if (statusWord.equalsIgnoreCase(_NO_ERROR))
            {
               message = "No Error"; 
               return isFound;
            }
        
            if (statusWord.equalsIgnoreCase(_PIN_DISABLED))
            {
               message = "PIN is already disabled"; 
               return isFound;
            }
            if (statusWord.equalsIgnoreCase(_CHV_BLOCKED))
            {
               message = "CHV verification was unsuccessful; no attempts remains and the CHV is blocked."; 
               return isFound;
            }            
            if (statusWord.equalsIgnoreCase(_CHV_NOT_INITIALIZED))
            {
               message = "CHV/PIN is not initialized"; 
               return isFound;
            }
            if (statusWord.equalsIgnoreCase(_RECORD_NOTFOUND))
            {
               message = "Record Not Found"; 
               return isFound;
            }            
            if (statusWord.equalsIgnoreCase(_WRONG_PK_CRYPTO_COMMAND))
            {
               message = "ISO error (64 00): Wrong context in the PK Crypto command"; 
               return isFound;
            }               
            if (statusWord.equalsIgnoreCase(_NOT_ENOUGH_SPACE_IN_FILE))
            {
               message = "ISO error (69 84): Not enough memory space in file"; 
               return isFound;
            }   
            if (statusWord.equalsIgnoreCase(_SECURITY_CONDITION_NOT_SATISFIED))
            {
               message = "Security condition not satisfied"; 
               return isFound;
            }              
            if (statusWord.equalsIgnoreCase(_WRONG_LENGTH_IN_SECURE_MESSIGING))
            {
               message = "ISO error (6C 40): Wrong length in secure messaging mode"; 
               return isFound;
            }            
            if (statusWord.equalsIgnoreCase(_WRITE_PROBLEM))
            {
               message = "Application error (92 02): Write problem / Memory failure (non-iso class int)"; 
               return isFound;
            }            
            if (statusWord.equalsIgnoreCase(_KEY_FILE_SELECTION_ERROR))
            {
               message = "Application error (94 08): Key file selection error"; 
               return isFound;
            }            
            if (statusWord.equalsIgnoreCase(_WRONG_KEY))
            {
               message = "Access authorisation not fulfilled, wrong certificate, key file, or TTC.\nAtleast one attempt left"; 
               return isFound;
            }            
            if (statusWord.equalsIgnoreCase(_INS_NOTSUPPORTED))
            {
               message = "Instruction not supported"; 
               return isFound;
            }
            if (statusWord.equalsIgnoreCase(_IADF_ERROR))
            {
               message = "ISO error (62 81): a problem occured during IADF; interpretation and the relevant data may be corrupted." ;
               return isFound;
            }            
            if (statusWord.equalsIgnoreCase(_FILE_DISCRIPTER_ERROR))
            {
               message = "ISO error (62 84): File descriptor error"; 
               return isFound;
            }            
            if (statusWord.equalsIgnoreCase(_NO_DIAGNOS))
            {
               message = "No Precise Diagnostics/Unhandled Exception"; 
               return isFound;
            }            
            if (statusWord.equalsIgnoreCase(_CLA_NOTSUPPORTED))
            {
               message = "CLA value not supported"; 
               return isFound;
               
            }
            if (statusWord.equalsIgnoreCase(_UNCAUGHT_EXCEPTION))
            {
               message = "Uncaught exception thrown"; 
               return isFound;
               
            }
            if (statusWord.equalsIgnoreCase(_WRONG_PAREAMETERS))
            {
               message = "Wrong Parameters P1 and P2"; 
               return isFound;
               
            }
            if (statusWord.equalsIgnoreCase(_APPLET_SELECTION_FAILED))
            {
               message = "Applet Selection Failed"; 
               return isFound;
               
            }
            if (statusWord.equalsIgnoreCase(_CONDITION_NOT_SATISFIED))
            {
               message = "Condition of use not satisfied"; 
               return isFound;
               
            }    
            if (statusWord.equalsIgnoreCase(_SECURE_MSG_NOTSUPPORTED))
            {
               message = "Secure messaging not supported"; 
               return isFound;
               
            }
            if (statusWord.equalsIgnoreCase(_CHANNEL_CLOSED_WARNING))
            {
               message = "Logical Channel already closed"; 
               return isFound;
               
            }
            if (statusWord.equalsIgnoreCase(_INCORRECT_VALUES))
            {
               message = "Incorrect Values in the command"; 
               return isFound;
               
            }
            if (statusWord.equalsIgnoreCase(_APP_NOTFOUND))
            {
               message = "Application not found"; 
               return isFound;
               
            }
            if (statusWord.equalsIgnoreCase(_LOGICAL_CHANNEL_NOT_SUPPORTED))
            {
               message = "Logical Channels Not Supported"; 
               return isFound;
               
            }
            if (statusWord.equalsIgnoreCase(_NOT_ENOUGH_MEMORY))
            {
               message = "Memory overflow"; 
               return isFound;
               
            }
            if (statusWord.equalsIgnoreCase(_AUTHENTICATION_FAILED))
            {
               message = "ISO error (63 00): Authentication failed"; 
               return isFound;
               
            }            
            if (statusWord.equalsIgnoreCase(_REF_DATA_NOTFOUND))
            {
               message = "Reference data not found"; 
               return isFound;
               
            }
            if (statusWord.equalsIgnoreCase(_MORE_DATA_AVAIABLE))
            {
               message = "More data available"; 
               return isFound;
               
            }
            if (statusWord.equalsIgnoreCase(_CHANNEL_CLOSED_WARNING))
            {
               message = "Channel is closed"; 
               return isFound;
               
            }
            if (statusWord.equalsIgnoreCase(_FUNCTION_NOT_SUPPORTED))
            {
               message = "Function not supported e.g. card Life Cycle State is CARD_LOCKED"; 
               return isFound;
               
            }
            if (statusWord.equalsIgnoreCase(_ALGO_NOT_SUPPORTED))
            {
               message = "Algorithm not supported"; 
               return isFound;
               
            }
            if (statusWord.equalsIgnoreCase(_INVALID_KCV))
            {
               message = "Invalid key check value"; 
               return isFound;
               
            }
            if (statusWord.equalsIgnoreCase(_CARD_LOCKED))
            {
               message = "Card is locked"; 
               return isFound;
               
            }            
            else
            {
                isFound = false;
                message = "0x"+statusWord.toUpperCase();
            }
        return isFound;
    }
    
    public static boolean checkStatus()
    {
        Shell.printMessage( (JavaCardUtil.getFormatedData( JavaCardUtil.byteArrayToHexString(JavaCard.getData()) )== null || JavaCardUtil.getFormatedData( JavaCardUtil.byteArrayToHexString(JavaCard.getData()) ).length() == 0 )? "-->"+"No data" : "-->   "+JavaCardUtil.htos( JavaCard.getData()).toUpperCase() , false);
        String statusWords = Integer.toHexString(JavaCard.getStatusWords());
        Shell.printMessage("-->   "+JavaCardUtil.htos(JavaCardUtil.intToBytes(JavaCard.getStatusWords())).toUpperCase(), false);                    
        boolean status = statusWords.equalsIgnoreCase(_NO_ERROR) ;        
//        System.out.println("<<<<<<<<<<<<<<\nstatus word "+ statusWords+" : "+status);
        analyseStatus(statusWords);
        Shell.printMessage("Status: "+getMessage(), false);
        return status;
    }
}
