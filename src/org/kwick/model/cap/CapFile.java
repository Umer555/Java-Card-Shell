package org.kwick.model.cap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.kwick.Util.JavaCardUtil;

/**
 *
 * @author Umer
 */
public class CapFile
{

    JarFile capFile;
    long unwanted =0;
    long size = 0;
    HeaderComp header;
    AppletComp applet;
    ByteArrayOutputStream binaryData;
    byte[] headerData;
    byte[] capBuffer;
    private static CapFile  cap = null;
    private static final String capFiles[] = 
    {
            "Header.cap", "Directory.cap", "Import.cap", "Applet.cap", "Class.cap", "Method.cap", "StaticField.cap", "Export.cap", "ExportDescription.cap", "ConstantPool.cap", 
            "RefLocation.cap", "Descriptor.cap"
    };
    
/*******************************************************************************/        
    private CapFile()
    {

    }
/*******************************************************************************/
    public static CapFile getCapFileInstance()
    {
        if (cap == null) {
            cap = new CapFile();
        }
        return cap;
    }
/*******************************************************************************/    
    public void setCapFile( JarFile file) throws IOException
    {
        capFile = file;
        size = 0;
        unwanted =0;
    }
/*******************************************************************************/    
    public long getTotalSize()
    {
        return size;
    }
/*******************************************************************************/    
    public long getUnwantedSize()
    {
        return unwanted;
    }
/*******************************************************************************/
    public byte[] getPackageAID()
    {
        return header.getAID();
    }
/*******************************************************************************/
    public byte[] getAppletAID()
    {
        return applet.getAID();
    }
/*******************************************************************************/   
    private void getAIDs() throws IOException
    {
        Enumeration<JarEntry> capComponents = capFile.entries();
        
        while (capComponents.hasMoreElements())
        {
            
            JarEntry capEntry = (JarEntry)capComponents.nextElement();
            //System.out.println(" "+capEntry.getName() +" "+capEntry.getSize());
            
            if ( capEntry.getName().contains("Descriptor.cap") || capEntry.getName().contains("Debug.cap") )
            {
                unwanted += capEntry.getSize();
            }
            
            size += capEntry.getSize();
                       
            if (capEntry.getName().contains("Header.cap"))
            {
                InputStream stream = null;
                stream = capFile.getInputStream(capEntry);
                header = new HeaderComp(stream);                    
            }            
            if (capEntry.getName().contains("Applet.cap"))
            {
                InputStream stream = null;
                    stream = capFile.getInputStream(capEntry);
                    applet = new AppletComp(stream);                  
            }
        }
            System.out.println("Unwanted size:"+unwanted+" Actual size:"+size);
            System.out.println("Total - mandatory = " +(size-unwanted));
    }
/*******************************************************************************/
    public byte[] readCap() throws IOException
    {
        getAIDs();
        binaryData = new ByteArrayOutputStream();
        String path = "";        
        Enumeration<JarEntry> capComponents = capFile.entries();
        if (capComponents.hasMoreElements())
        {            
            String name = ((JarEntry)capComponents.nextElement()).getName();
            path = getPath(name);
        }
        for (int i=0; i<capFiles.length; i++)
        {
               byte [] capData = getData(path+capFiles[i]);
                if (capData != null) {
                binaryData.write(capData);
            }                    
        }
            capBuffer = binaryData.toByteArray();
          //  System.out.println("-----------------------------------------\nBinaryData\n"+JavaCardUtil.htos(capBuffer));
            binaryData.reset();
            prepareHeader();
            
            return capBuffer;
    }
/*******************************************************************************/
    private String getPath( String fullPath) throws IOException
    {
        int i = fullPath.lastIndexOf("/");
        if(i > 0) {
            return fullPath.substring(0, i + 1);
        }
        else {
            throw new RuntimeException("Invalid Cap file");
        }
    }
/*******************************************************************************/    
    private byte[] getData(String capComponent) throws IOException 
    {
        ByteArrayOutputStream temp = new ByteArrayOutputStream();
        temp.reset();
        JarEntry capEntry = capFile.getJarEntry(capComponent);
        if (capEntry ==  null)
        {   
            return null;
        }
        InputStream stream = capFile.getInputStream(capEntry);
        int nextByte = 0;
        while( (nextByte = stream.read()) != -1)
        {
            temp.write(nextByte);
        }
        return temp.toByteArray();
    }
/*******************************************************************************/
    private void prepareHeader( ) throws IOException
    {
        binaryData.reset();
        binaryData.write(-60);
        binaryData.write(JavaCardUtil.toLengthOctets((int)(getTotalSize()-getUnwantedSize())));
        headerData = binaryData.toByteArray();
        System.out.println("size of header: "+headerData.length);
        System.out.println("Header is: "+JavaCardUtil.htos(headerData));
    }
/*******************************************************************************/
    public byte[] getHeader()
    {
        return headerData;
    }
/*******************************************************************************/
    public int getLength()
    {
        return capBuffer.length+headerData.length; 
    }
/*******************************************************************************/    
//    public static void main(String[] args)
//    {
//        CapFile cf = null;
//        try 
//        {
//            cf = new CapFile( "E:/calculator.cap");
//            cf.read();
//            cf.Load();
//        } catch (IOException ex) 
//        {
//            System.out.println("could not read cap file");
//        }
//    }
}

