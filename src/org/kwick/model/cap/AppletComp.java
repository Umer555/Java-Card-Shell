/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kwick.model.cap;

import java.io.IOException;
import java.io.InputStream;
import org.kwick.Util.JavaCardUtil;

/**
 *
 * @author Dell
 */
public class AppletComp 
{
    
    InputStream inputStream;

    int Tag; 
    int Size;
    int Count;
    
    int AIDlength;
    byte[] AID; 
    int install_method_offset;
    
/*******************************************************************************/        
    public int getCount() {
        return Count;
    }
/*******************************************************************************/    
    public int getInstall_method_offset() {
        return install_method_offset;
    }
/*******************************************************************************/    
    public AppletComp( InputStream headerStream)
    {
        this.inputStream = headerStream;
        readEntry();
    }
/*******************************************************************************/
    public byte[] getAID() {
        return AID;
    }
/*******************************************************************************/
    public int getAIDlength() {
        return AIDlength;
    }
/*******************************************************************************/
    public int getSize() {
        return Size;
    }
/*******************************************************************************/
    public int getTag() {
        return Tag;
    }
/*******************************************************************************/
    public InputStream getInputStream() {
        return inputStream;
    }
/*******************************************************************************/
    private void readEntry()
    {
        try 
        {   
            byte[] u2 = new byte[2];
            byte[] u1 = new byte[1];
            inputStream.read(u1,0, 1);
            if ( JavaCardUtil.byteToInt(u1)!= 3)
            {
                throw new IOException("Invalid Applet.cap component"+JavaCardUtil.byteToInt(u1) );
                
            }
            Tag = JavaCardUtil.byteToInt(u1);
            System.out.println("Tag is: "+ Tag);
            
            inputStream.read(u2,0,2);
            if ( JavaCardUtil.byteToInt(u2)<= 0)
            {
                throw new IOException("Invalid size of Applet.cap component");
                
            }            
            Size = JavaCardUtil.byteToInt(u2);
             System.out.println("Size is:"+ Size);
            
            inputStream.read(u1,0, 1);
            if ( JavaCardUtil.byteToInt(u1)<= 0)
            {
                throw new IOException("Invalid applet count of Applet.cap component");
                
            }            
            Count = JavaCardUtil.byteToInt(u1);
             System.out.println("Count is: "+ Count);
         
            Applets(inputStream);
           
        } catch (IOException ex) {
             System.out.println("could not read cap entry: "+"Header.cap"+"\n"+ex.getMessage());
        }
    }
/*******************************************************************************/   
    private void Applets(InputStream stream)
    {
        try 
        {   byte[] u2 = new byte[2];  
            byte[] u1 = new byte[1];
            
            stream.read(u1, 0, 1);
            AIDlength = JavaCardUtil.byteToInt(u1); 
            if ( AIDlength< 5 || AIDlength> 16)
            {
                throw new IOException("Invalid AID length of Applet.cap component");
            }                         
            System.out.println("AId length: "+AIDlength);
            
            AID = new byte[AIDlength];
            stream.read(AID, 0, AIDlength);
            System.out.println("AID: "+JavaCardUtil.byteArrayToHexString(AID));
                        
            stream.read(u2, 0, 2);
            install_method_offset = JavaCardUtil.byteToInt(u2);
            
            System.out.println("Insall Method offset : "+install_method_offset);
        }
        catch (IOException ex) 
        {
            System.out.println("could not read Applet info");
        }      
    }   
/*******************************************************************************/      
}
