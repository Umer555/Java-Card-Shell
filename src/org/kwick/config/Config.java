/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kwick.config;

import org.kwick.view.Shell;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Dell
 */

public class Config extends DefaultHandler
{
    private static boolean isConfig = false;
    private static boolean isApp = false;
    private static boolean isCard = false;
    private static boolean isCardM = false;
    private static boolean isKeys = false;
    private static boolean isCurrent = false;
    private static boolean isNew = false;
    private static boolean isKeyset = false;
    private static boolean isShell = false;
    private static boolean isKey = false;
    private static boolean isIP = false;
    private static boolean isPri = false;
    
    public static boolean isConfigPresent = false;
    public static boolean isAppPresent = false;
    public static boolean isCardPresent = false;
    public static boolean isCardMPresent = false;
    public static boolean isKeysPresent = false;
    public static boolean isCurrentPresent = false;
    public static boolean isNewPresent = false;
    public static boolean isKeysetPresent = false;
    public static boolean isShellPresent = false;
    public static boolean isKeyPresent = false;
    public static boolean isIPPresent = false;
    public static boolean isPriPresnt = false;
    
    public static String[] currentKeys = new String[3];
    public static String[] newKeys = new String[3];
    public static String cardM;
    public static String keySet;
    public static String IP;
    public static String privileges;
    
    int cuIndex = 0;
    int neIndex = 0;
    
    public Config()
    {
        super();
        cuIndex = 0;
        neIndex = 0;
    }
/*******************************************************************************/
    @Override
    public void startElement (String uri, String name, String qName, Attributes atts)
    {
        //System.out.println("Start Element: "+qName);
        if (qName.equalsIgnoreCase("Shell"))
        {
            isShell = true;   
            isShellPresent =true;
        }
        
        else if (qName.equalsIgnoreCase("Config"))
        {
            isConfig = true;   
            isConfigPresent = true;
        }
        
        else if (qName.equalsIgnoreCase("app"))
        {
            isApp = true;
            isAppPresent = true;
        }
        
        else if (qName.equalsIgnoreCase("Card"))
        {
            isCard = true;
            isCardPresent = true;
        }
        
        else if (qName.equalsIgnoreCase("cardManager"))
        {
            isCardM = true;
            isCardMPresent = true;
        }        
        
        else if (qName.equalsIgnoreCase("Keys"))
        {
            isKeys = true;
            isKeysPresent = true;
        }

        else if (qName.equalsIgnoreCase("current"))
        {
            isCurrent = true;
            isCurrentPresent = true;
            isNew = false;
        }

        else if (qName.equalsIgnoreCase("New"))
        {
            isCurrent = false;
            isNew = true;
            isNewPresent = true;
        }
        
        else if (qName.equalsIgnoreCase("key"))
        {
            isKey = true;
            isKeyPresent = true;
        }
                        
        else if (qName.equalsIgnoreCase("keySet"))
        {
            isKeyset = true;
            isKeysetPresent = true;
        }
                        
        else if (qName.equalsIgnoreCase("Parameters"))
        {
            isIP = true;
            isIPPresent = true;
        }
                        
        else if (qName.equalsIgnoreCase("privileges"))
        {
            isPri = true;
            isPriPresnt = true;
        }                
    }
/*******************************************************************************/
    @Override
    public void endElement (String uri, String name, String qName)
    {

    }
/******************************************************************************/    
    @Override
    public void characters (char ch[], int start, int length)
    {
        if (isCardM)
        {
            cardM = new String (ch, start, length);
            System.out.println("Card Manager:" + cardM);
            if(cardM.length() == 0)
               Shell.abort("Invalid Config file.\nPlease check Card Manager Parameters field and try again", "Configuration Error");
            isCardM = false;
        }
        
        if ( isKey )
        {

            if (isCurrent)
            {
                currentKeys[cuIndex] = new String (ch, start, length);
                System.out.println("Current Key["+cuIndex+"]:" + currentKeys[cuIndex]);
                cuIndex++;
            }
            else if (isNew)
            {
                newKeys[neIndex] = new String (ch, start, length);
                System.out.println("New Keys["+neIndex+"]:"+ newKeys[neIndex]);
                neIndex++;
            }
                
            isKey = false;
        }
        if (isKeyset)
        {
            keySet = new String (ch, start, length);
            System.out.println("KeySet:" + keySet);
           // isSystem.out.println("Size of list: in characers: "+ list.size());
            isKeyset = false; 
        }
        if (isIP)
        {
            IP = new String (ch, start, length);
            System.out.println("Installation Parameters:" + IP);
            if (IP.length() == 0)
            {
                IP = "00";
            }
            else
            {
                if ((IP.length()%2 != 0) || IP.length() > 4)
                {
                    Shell.abort("Invalid Config file.\nPlease check Install Parameters field and try again", "Configuration Error");
                }
            }
           // isSystem.out.println("Size of list: in characers: "+ list.size());
            isIP = false; 
        }
        
        if (isPri)
        {
            privileges = new String (ch, start, length);
            System.out.println("Privileges:" + privileges);
            if((privileges.length() == 0) || (privileges.length() > 6))
               Shell.abort("Invalid Config file.\nPlease check Privileges Parameters field and try again", "Configuration Error");
            isPri = false;
        }        
    }
/******************************************************************************/    
//    public static void main(String[] argv)
//    {        
//            FileReader fr = null;
//            try 
//            {
//                XMLReader XMLreader = XMLReaderFactory.createXMLReader();
//                Config main = new Config();
//                XMLreader.setContentHandler(main);
//                XMLreader.setErrorHandler(main);
//                fr = new FileReader("@config.xml");
//                XMLreader.parse(new InputSource(fr));
//            } 
//            catch (IOException ex) 
//            {
//                System.out.println("File reading error");
//            }            
//            catch (SAXException ex) 
//            {
//                ex.printStackTrace();
//                 System.out.println("could not create reader object");
//            } 
//            finally 
//            {
//                try
//                {
//                    fr.close();
//                }
//                catch (IOException ex)
//                {
//                    
//                }
//            }
//     }        
}
/******************************************************************************/