/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kwick.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.jar.JarFile;
import javax.smartcardio.CardException;
import org.kwick.Util.JavaCardUtil;
import org.kwick.config.Config;
import org.kwick.model.cap.CapFile;
import org.kwick.view.Shell;

/**
 *
 * @author Dell
 */
public class CapHandler extends Thread{

    JarFile capJar;
    Authenticate auth;
    CapFile capFile;// = CapFile.getCapFileInstance();
    
    byte[] capBuffer;
    byte[] headerData;
    byte[] InstallParam;
    byte[] privileges;
    
    public CapHandler(String absoPath) throws IOException {
        capFile = CapFile.getCapFileInstance();
        capJar = new JarFile(absoPath);
        auth = new Authenticate();
        InstallParam = JavaCardUtil.hexStringToByteArray(Config.IP);
        privileges = JavaCardUtil.hexStringToByteArray(Config.privileges);
    }

    /**
     * ****************************************************************************
     */
    public boolean install() throws IOException, CardException, Exception {
        capFile.setCapFile(capJar);
        capBuffer = capFile.readCap();
        headerData = capFile.getHeader();
        byte[] installForLoad;
        try {
            installForLoad = Authenticate.getSCP02APDU(getInstallForLoadCmd(capFile.getPackageAID()));
            Shell.displayCommand(JavaCardUtil.htos(installForLoad));
        } catch (Exception ex) 
        {
            Shell.printMessage("Shell -->Internal Error: 1009 "+ ex.getMessage(), true);
            return false;
        }
        if ( !JavaCard.sendApdu(installForLoad))
        {
            return false;
        }
        
        else
        {
            if ( !StatusHandler.checkStatus())
            {
                return false;
            }
            String statusWords = Integer.toHexString(JavaCard.getStatusWords()); 
            if (!statusWords.equalsIgnoreCase(StatusHandler._NO_ERROR))
            {
                Shell.printMessage("Shell -->Unexpected error. Aborting...", true);
                return false;
            }
            
        }
        
        if ( !Load() )
        {
            return false;
        }
        Shell.printMessage("Shell -->Cap file loaded", false);
       byte[] comAPDU = Authenticate.getSCP02APDU(getInstallForInstallCmd(capFile.getPackageAID(), capFile.getAppletAID()));
               Shell.displayCommand(JavaCardUtil.htos(comAPDU));
        
        if ( !JavaCard.sendApdu(comAPDU) )
        {
            return false;
        }
        
        else
        {
            if ( !StatusHandler.checkStatus())
            {
                return false;
            }
        }
        
        return true;
    }

    /**
     * ****************************************************************************
     */
    public boolean Load() throws RuntimeException {
        try {
            int loadBlockSize = 150;
            int capBufferOffset = 0;
            int lastBlockSize = 0;
            boolean isLast = false;
            int k = 0;
            int totalSize = capFile.getLength();
            byte[] buffer = new byte[totalSize];
            System.arraycopy(headerData, 0, buffer, 0, headerData.length);
            System.arraycopy(capBuffer, 0, buffer, headerData.length, capBuffer.length);
            int blocks = totalSize / loadBlockSize;
            System.out.println("totla size" + totalSize);
            if ((totalSize % loadBlockSize) >= 1) {
                blocks = blocks + 1;
                lastBlockSize = totalSize % loadBlockSize;
                System.out.println("Last block size: " + lastBlockSize);
            }
            
            System.out.println("total blocks" + blocks);

            byte[] chunk = new byte[loadBlockSize];
            for (int i = 0; i < blocks; i++) {
                if (i == 0) {
                    k = 0;
                } else {
                    k = 1;
                }
                if (i == blocks - 1) {
                    isLast = true;
                    if (lastBlockSize > 0) {
                        chunk = new byte[lastBlockSize];
                        loadBlockSize = lastBlockSize;
                    }
                }
                System.arraycopy(buffer, capBufferOffset, chunk, 0, loadBlockSize);
                if (!loadBlock(isLast, i, chunk)) {
                    return false;
                }
                capBufferOffset += loadBlockSize;
            }
        } catch (Exception ex) {
            return false;
        }
        System.out.println("Install for Install Command\n" + JavaCardUtil.htos(getInstallForInstallCmd(capFile.getPackageAID(), capFile.getAppletAID())));
        return true;
    }

    /**
     * ****************************************************************************
     */
    private boolean loadBlock(boolean isLast, int p2, byte[] data) throws Exception {
        boolean isLoaded = true;
        byte[] apdu = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            baos.reset();
            byte p1 = (byte) 0x00;
            if (isLast) {
                p1 = (byte) 0x80;
            }
            baos.write((byte) 0x80);
            baos.write((byte) 0xE8);
            baos.write((byte) p1);
            baos.write((byte) p2);
            baos.write((byte) (data.length));
            baos.write(data);
            baos.write((byte) 0x00);
            apdu = Authenticate.getSCP02APDU(baos.toByteArray());
            Shell.displayCommand(JavaCardUtil.htos(apdu));            
            if ( !JavaCard.sendApdu( apdu ))
            {
               return false;
            }
            else
            {
                if ( !StatusHandler.checkStatus())
                {
                    return false;
                }
            }
        } catch (IOException | CardException | IllegalArgumentException ex) {
            isLoaded = false;
            ex.printStackTrace();
        }
        System.out.println("------------------------\nLoad Commands\n" + JavaCardUtil.byteArrayToHexString(apdu).toUpperCase());
        return isLoaded;
    }

    /**
     * ****************************************************************************
     */
    public byte[] getInstallForLoadCmd(byte[] packID) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            baos.reset();
            baos.write((byte) 0x80);
            baos.write((byte) 0xE6);
            baos.write((byte) 0x02);
            baos.write((byte) 0x00);
            baos.write((byte) (packID.length + JavaCard.getSecurityDomain().length + 5));
            baos.write(packID.length);
            baos.write(packID);
            baos.write((byte) JavaCard.getSecurityDomain().length);
            baos.write(JavaCard.getSecurityDomain());
            baos.write((byte) 0x00);
            baos.write((byte) 0x00);
            baos.write((byte) 0x00);
            baos.write((byte) 0x00);
            
            System.out.println("Install for load: "+ JavaCardUtil.byteArrayToHexString(baos.toByteArray()));
        } catch (Exception ex) {
            baos = null;
            ex.printStackTrace();
        }
        return baos.toByteArray();
    }

    /**
     * ****************************************************************************
     */
    public byte[] getInstallForInstallCmd(byte[] packAID, byte[] appAID) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            baos.reset();
            baos.write((byte) 0x80);
            baos.write((byte) 0xE6);
            baos.write((byte) 0x0C);
            baos.write((byte) 0x00);     
            
            baos.write((byte) (packAID.length + 1 + (appAID.length + 1) * 2 + 5 + privileges.length + InstallParam.length));
            
            baos.write(packAID.length);
            baos.write(packAID);
            baos.write((byte) appAID.length);
            baos.write(appAID);
            baos.write((byte) appAID.length);
            baos.write(appAID);
            //privileges
            baos.write((byte) privileges.length);            
            baos.write(privileges);
            //install parameters
            baos.write((byte) 0x02 + (byte) InstallParam.length);
            baos.write((byte) 0xC9);
            baos.write((byte) InstallParam.length);
            baos.write(InstallParam);
            baos.write((byte) 0x00);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return baos.toByteArray();
    }
/******************************************************************************/    
    @Override
    public void run()
    {
        boolean isLoaded = false;
        try {
           isLoaded = install();
        } catch (IOException | CardException ex) {
            isLoaded = false;
            Shell.printMessage(""+ex.getMessage(), false);
        } catch (Exception ex) {
            isLoaded = false;
            isLoaded = false;Shell.printMessage(""+ex.getMessage(), false);
        }        
        if (isLoaded)
        {
            Shell.printMessage("Shell -->Cap File Loaded and Installed", false);
        }
        if(!isLoaded)
            Shell.printMessage("Shell -->Error: cap file not installed", true);
    }
}
