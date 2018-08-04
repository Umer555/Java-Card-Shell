/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kwick.config;

import java.io.FileReader;
import java.io.IOException;
import org.kwick.view.Shell;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author Dell
 */
public class XMLControler 
{
    public XMLControler( )
    {
        //loyaltyCard = card;
    }   
    public boolean loadXML()
    {
        boolean isLoaded = true ;
            FileReader fr = null;
            
            try 
            {
                XMLReader XMLreader = XMLReaderFactory.createXMLReader();
                Config main = new Config();
                XMLreader.setContentHandler(main);
                XMLreader.setErrorHandler(main);
                fr = new FileReader("@config.xml");
                XMLreader.parse(new InputSource(fr));
            } 
            catch (IOException ex) 
            {
                Shell.printMessage("Invalid Config file "+ex.getMessage(), true);
                Shell.handleErrorMessage("Invalid state");
            }            
            catch (SAXException ex) 
            {
                isLoaded = false;
                ex.printStackTrace();
                Shell.printMessage("Invalid Config file "+ex.getMessage(), true);
                Shell.handleErrorMessage("Invalid state");
            } 
            finally 
            {
                try
                {
                    fr.close();
                }
                catch (IOException ex)
                {
                    
                }
            }
            
            return isLoaded ;
    }
}
