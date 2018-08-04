
package org.kwick.model.cap;

import java.io.IOException;
import java.io.InputStream;
import org.kwick.Util.JavaCardUtil;

public class HeaderComp 
{
    
    InputStream inputStream;
    
    byte[] pack_MinorVersion = new byte[1];
    byte[] pack_MajorVersion = new byte[1];
    byte[] AIDlength = new byte[1];

    byte Tag[] = new byte[1];
    byte Size[] = new byte[2];
    byte Magic[] = new byte[4];
    byte minor_version[] = new byte[1];
    byte major_version[] = new byte[1];
    byte Flags[] = new byte[1];
    byte[] AID;  
    
/*******************************************************************************/    
    public HeaderComp( InputStream headerStream)
    {
        this.inputStream = headerStream;
        readEntry();
    }
/*******************************************************************************/
    public byte[] getAID() {
        return AID;
    }
/*******************************************************************************/
    public byte[] getAIDlength() {
        return AIDlength;
    }
/*******************************************************************************/
    public byte[] getFlags() {
        return Flags;
    }
/*******************************************************************************/
    public byte[] getMagic() {
        return Magic;
    }
/*******************************************************************************/
    public byte[] getSize() {
        return Size;
    }
/*******************************************************************************/
    public byte[] getTag() {
        return Tag;
    }
/*******************************************************************************/
    public InputStream getInputStream() {
        return inputStream;
    }
/*******************************************************************************/
    public byte[] getMajor_version() {
        return major_version;
    }
/*******************************************************************************/
    public byte[] getMinor_version() {
        return minor_version;
    }
/*******************************************************************************/
    public byte[] getPack_MajorVersion() {
        return pack_MajorVersion;
    }
/*******************************************************************************/
    public byte[] getPack_MinorVersion() {
        return pack_MinorVersion;
    }
/*******************************************************************************/    
    private void readEntry()
    {
        try 
        {           
            inputStream.read(Tag,0, 1);
            System.out.println("Tag is: "+ JavaCardUtil.byteToInt(Tag));
            inputStream.read(Size,0,2);
            System.out.println("Size is:"+JavaCardUtil.byteToInt(Size)); 
            inputStream.read(Magic,0, 4);
            System.out.println("Magic is: "+ JavaCardUtil.byteToInt(Tag));
            inputStream.read(minor_version,0,1);
            System.out.println("Minor Version is:"+JavaCardUtil.byteToInt(minor_version));
            inputStream.read(major_version,0,1);
            System.out.println("Mjor Version is:"+JavaCardUtil.byteToInt(major_version));
            inputStream.read(Flags,0,1);
            System.out.println("Flags is:"+JavaCardUtil.byteToInt(Flags));
         
            packageInfo(inputStream);
           
        } catch (IOException ex) {
            System.out.println("could not read cap entry: "+"Header.cap"+"\n"+ex.getMessage());
        }
    }
/*******************************************************************************/   
    private void packageInfo(InputStream stream)
    {
        try 
        {          
            stream.read(pack_MinorVersion, 0, 1);
            stream.read(pack_MajorVersion, 0, 1);
            stream.read(AIDlength, 0, 1);
           
            AID = new byte[JavaCardUtil.byteToInt(AIDlength)];
            System.out.println("AId length: "+JavaCardUtil.byteToInt(AIDlength));
            stream.read(AID, 0, JavaCardUtil.byteToInt(AIDlength));
            System.out.println("AID: "+JavaCardUtil.byteArrayToHexString(AID));
        }
        catch (IOException ex) 
        {
            System.out.println("could not read pack info");
        }      
    }   
/*******************************************************************************/      
}
