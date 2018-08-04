
package org.kwick.model;

import com.jidesoft.plaf.basic.ThemePainter;
import com.jidesoft.swing.JideSplitButton;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import javax.smartcardio.*;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import org.kwick.Util.JavaCardUtil;
import org.kwick.config.Config;
import org.kwick.config.XMLControler;
import org.kwick.view.Shell;

/**
 *
 * @author Dell
 */
public class JavaCard {

    protected List<CardTerminal> terminals;
    protected static CardTerminal terminal;
    protected static TerminalFactory factory;
    protected static Card card;
    public static ResponseAPDU rAPDU;
    protected static CardChannel channel;
    static boolean IsConnectedToCard = false;

    String adminCode = "";
    public static byte[] securityDomain ;
    ATR atr;

    
    public JavaCard()
    {
        XMLControler config = new XMLControler();
        config.loadXML();
        securityDomain = JavaCardUtil.hexStringToByteArray(Config.cardM);
    }
    /**
     * ****************************************************************************
     */
    public static byte[] getSecurityDomain() {
        return securityDomain;
    }

    /**
     * ****************************************************************************
     */
    public ATR getAtr() {
        return atr;
    }

    /**
     * ****************************************************************************
     */
    public static int getStatusWords() {
        return rAPDU.getSW();
    }

    /**
     * ****************************************************************************
     */
    protected CardTerminal getCardReader(String name) {
        CardTerminal reader = null;
        for (int i = 0; i < terminals.size(); i++) 
        {
            if (terminals.get(i).getName().equals(name)) 
            {
                reader = terminals.get(i);
            }
        }

        return reader;
    }

    /**
     * ****************************************************************************
     */
    //function to connect to the card
    protected boolean connectToCard() {
        boolean isConnectedToCard = true;

        try {
            card = terminal.connect("*");
        } catch (CardException | NullPointerException exception) {
            Shell.printMessage("cannot acces smart card" + "\n" + exception.getMessage(), true);
            isConnectedToCard = false;
            //JOptionPane.showMessageDialog(null,"Card is not present \n.", "Error",JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException exception) {
            isConnectedToCard = false;
            Shell.printMessage("cannot connect to smart card" + "\n" + exception.getMessage(), true);
        } catch (SecurityException exception) {
            isConnectedToCard = false;
            Shell.printMessage("Security Error:" + "\n" + exception.getMessage(), true);
        }
        return isConnectedToCard;
    }

    /**
     * ****************************************************************************
     */
    //function to connect to the terminal 
    protected boolean connectToTerminal(CardTerminal terninalSource) {
        boolean isConnectedToTerminal = true;
        try {
            terminal = terninalSource;
        } catch (Exception exception) {
            Shell.handleErrorMessage("Could not connect to terminal"+exception.getMessage());
            isConnectedToTerminal = false;
            exception.printStackTrace();
        }

        return isConnectedToTerminal;
    }

    /**
     * ****************************************************************************
     */
    //function to get the terminals list
    public boolean getTerminals() {
        boolean isTerminalPresent = true;
        try {
            // Display the list of terminals
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();
            if (terminals.isEmpty()) {
                Shell.handleErrorMessage("Smart Card reader(s) not found");
                isTerminalPresent = false;
                return isTerminalPresent;
            } else {
                Shell.readers.removeAll();
                for (int i = 0; i < terminals.size(); i++) {
                    createTerminalsSplitButton(terminals.get(i).getName());
                   // System.out.println("" + terminals.get(i).getName());
                }
                Shell.message_Label.setForeground(Color.blue);
                Shell.handleErrorMessage("" + terminals.size() + ": Terminal(s) detected");
            }
        } catch (Exception exception) {
            isTerminalPresent = false;
            Shell.message_Label.setForeground(Color.red);
            Shell.handleErrorMessage("Smart Card reader(s) not detected");
            exception.printStackTrace();
        }

        return isTerminalPresent;
    }

    /**
     * *********************************************************************************
     */
    JideSplitButton createTerminalsSplitButton(String name) {

        Shell.readers.setForegroundOfState(ThemePainter.STATE_DEFAULT, Color.BLACK);

        Shell.readers.add(new AbstractAction(name) {

            @Override
            public void actionPerformed(ActionEvent e) {
                JMenuItem terminalSource = (JMenuItem) e.getSource();
                CardTerminal reader = getCardReader(terminalSource.getText());

                if (connectToTerminal(reader)) {
                    try {
                        if (reader != null) {
                            if (!reader.isCardPresent()) {
                                Shell.handleErrorMessage("Card not present");
                                Shell.printMessage("Card Not Present", true);
                            } else if (connectToCard()) {
                                Shell.handleErrorMessage("Card is OK and connected");
                                IsConnectedToCard = true;
                                atr = card.getATR();
                                Shell.printMessage("\nCard Info:", false);                                
                                Shell.printMessage("Protocol:" + card.getProtocol(), false);                                
                                Shell.printMessage("ATR:" + JavaCardUtil.htos(atr.getBytes()), false);
                                try 
                                {
                                    Shell.printMessage("Historical Bytes: " + JavaCardUtil.byteArrayToHexString(atr.getHistoricalBytes()).toUpperCase() + "\n", false);
                                } catch (Exception ex) {
                                    Shell.printMessage("System Internal Error, Code:104", true);
                                }
                                
                            } else {
                                IsConnectedToCard = false;
                                Shell.printMessage("Error:\nBad Card, Connection fail", true);
                                Shell.handleErrorMessage("Could not connect to card");
                            }
                        } else {
                            Shell.handleErrorMessage("Terminal error");
                        }
                    } catch (CardException ex) {
                        Shell.printMessage("Error:\nBad Card, Connection fail"+"\n"+ex.getMessage(), true);
                    }
                } else {
                    Shell.handleErrorMessage("Terminal Error");
                }
            }
        });
        Shell.readers.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("button is clicked");
            }
        });

        Shell.readers.setFocusable(false);
        return Shell.readers;
    }

    /**
     * ****************************************************************************
     */
    public static boolean sendApdu(byte[] apdu) throws CardException, IllegalArgumentException{
        boolean isSent = true;
        if (IsConnectedToCard) {
            try
            {
                channel = card.getBasicChannel();
                rAPDU = channel.transmit(new CommandAPDU(apdu));
            }
            catch(java.lang.IllegalStateException ex)
            {
                isSent = false;
                Shell.printMessage(""+ex.getMessage(), true);
            }
            
        } else {
            isSent = false;
        }
        return isSent;
    }

    /**
     * ****************************************************************************
     */
    public static byte[] getData() {
        if (rAPDU!=null)
            return rAPDU.getData();
        else
            return null;
    }
/*******************************************************************************/
    public boolean DoClear() throws IllegalArgumentException, CardException
    {
        //F0EC010000
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.reset();
        baos.write((byte) 0xF0);
        baos.write((byte) 0xEC);
        baos.write((byte) 0x01);
        baos.write((byte) 0x00);
        baos.write((byte) 0x00);
        Shell.displayCommand(JavaCardUtil.htos(baos.toByteArray()));
        try
        {
            if (!sendApdu(baos.toByteArray()))
            {
                return false;
            }
        }
        catch(java.lang.IllegalStateException ex)
        {
            Shell.printMessage(ex.getMessage(), true);
        }
        return true;
    }
/*******************************************************************************/    
    public  boolean internalCommand() throws CardException, IllegalArgumentException
    {
        //F0F0020000
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.reset();
        baos.write((byte) 0xF0);
        baos.write((byte) 0xF0);
        baos.write((byte) 0x02);
        baos.write((byte) 0x00);
        baos.write((byte) 0x00);
        Shell.displayCommand(JavaCardUtil.htos(baos.toByteArray()));
        if (!sendApdu(baos.toByteArray()))   
        {
           return false;
        }
        return true;
    }
/*******************************************************************************/
    public void DoGetAdminCode() throws Exception
    {
        String iccid = getICCID();
        if (iccid != null)
        {
        
        }
    }
/*******************************************************************************/
    public String getICCID() throws Exception
    {
        if (!selectMF())
        {
            Shell.printMessage("Could not select MF", true);
            return null;
        }
        StatusHandler.checkStatus();
        if(!selectEFiccid())
        {
            Shell.printMessage("Could not select EFiccid", true);
            return null;            
        }
        StatusHandler.checkStatus();
        if(!ReadBinary_iccid())
        {
            Shell.printMessage("Could not read EFiccid", true);
            return null;            
        }
        StatusHandler.checkStatus();
        
        return JavaCardUtil.byteArrayToHexString(JavaCard.getData());
    }
/*******************************************************************************/    
    private boolean selectMF()
    {
        //send A0A40000023F00
        boolean sendAPDU = true;        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try 
        {
            baos.reset();
            baos.write((byte) 0xA0);
            baos.write((byte) 0xA4);
            baos.write((byte) 0x00);
            baos.write((byte) 0x00);
            baos.write((byte) 0x02);
            baos.write((byte) 0x3F);
            baos.write((byte) 0x00);
            Shell.displayCommand(JavaCardUtil.htos(baos.toByteArray()));
            sendAPDU = sendApdu(baos.toByteArray());
        }
        catch (CardException | IllegalArgumentException | IllegalStateException ex) 
        {
            sendAPDU = false;
            Shell.printMessage(""+ex.getMessage(), true);
        }        
        return sendAPDU;
    }
/*******************************************************************************/    
    private boolean selectEFiccid()
    {
        //send A0A40000022FE2
        boolean sendAPDU = true;        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try 
        {
            baos.reset();
            baos.write((byte) 0xA0);
            baos.write((byte) 0xA4);
            baos.write((byte) 0x00);
            baos.write((byte) 0x00);
            baos.write((byte) 0x02);
            baos.write((byte) 0x2F);
            baos.write((byte) 0xE2);
            Shell.displayCommand(JavaCardUtil.htos(baos.toByteArray()));
            sendAPDU = sendApdu(baos.toByteArray());
        }
        catch (CardException | IllegalArgumentException | IllegalStateException  ex) 
        {
            sendAPDU = false;
            Shell.printMessage(""+ex.getMessage(), true);
        }        
        return sendAPDU;
    }
/*******************************************************************************/    
    private boolean ReadBinary_iccid()
    {
        //send A0B000000a
        boolean sendAPDU = true;        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try 
        {
            baos.reset();
            baos.write((byte) 0xA0);
            baos.write((byte) 0xB0);
            baos.write((byte) 0x00);
            baos.write((byte) 0x00);
            baos.write((byte) 0x0A);
            Shell.displayCommand(JavaCardUtil.htos(baos.toByteArray()));
            sendAPDU = sendApdu(baos.toByteArray());
        }
        catch (CardException | IllegalArgumentException | IllegalStateException  ex) 
        {
            sendAPDU = false;
            Shell.printMessage(""+ex.getMessage(), true);
        }        
        return sendAPDU;
    }
/*******************************************************************************/    
   public static String getFormatedICCID( String nibbledICCID)
   {
       char[] ICCID_Array = nibbledICCID.toCharArray();
       String iccid = "";
       String digit1 = "";
       String digit2 = "";
       String temp = "";
        for (int i = 0; i < ICCID_Array.length; i = i + 2) 
        {
            digit1 = "" + ICCID_Array[i];
            digit2 = "" + ICCID_Array[i + 1];
            temp = digit2 + digit1;
            iccid += temp;
        }
        return iccid;
   }
/*******************************************************************************/
    public void getAdminCode(String fileName, String iccid)
    {
        BufferedReader in = null;
        String line = null;
        try 
        {
            in = new BufferedReader(new FileReader(fileName));
            while ((line = in.readLine()) != null) 
            {
                if (parseRecord(line, iccid))
                {
                    break;
                }
            }
        }
        catch (IOException ex) 
        {
            Shell.printMessage("could not get Admin Code \n"+ex.getMessage(), true);
        }
    }
/*******************************************************************************/    
    public boolean parseRecord(String line, String iccid)
    {
        boolean isFound = false;
        int i = 0;
        Scanner scanner = new Scanner(line);
        scanner.useDelimiter(",");
        while(scanner.hasNext())
        {
            i = i + 1;
            scanner.next();
            if (i == 2)
            {
                if (scanner.next().equalsIgnoreCase(iccid))
                {
                    isFound = true;
                }
                else  break;
            } 
            if (i == 6)
            {
                adminCode = scanner.next();
                Shell.printMessage("Admin Code == "+adminCode, false);
            }
        }
        return isFound;
    }
/*******************************************************************************/
    public boolean verifyAdminCode()
    {   //send A020000408
        boolean sendAPDU = true;        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try 
        {
            baos.reset();
            baos.write((byte) 0xA0);
            baos.write((byte) 0x20);
            baos.write((byte) 0x00);
            baos.write((byte) 0x04);
            baos.write((byte) 0x08);           
            baos.write(adminCode.getBytes());
            Shell.displayCommand(JavaCardUtil.htos(baos.toByteArray()));
            sendAPDU = sendApdu(baos.toByteArray());
        } 
        catch (CardException | IOException | IllegalArgumentException | IllegalStateException  ex) 
        {
            sendAPDU = false;
            Shell.printMessage(""+ex.getMessage(), true);
        }        
        return sendAPDU;
    }
/*******************************************************************************/
    public ATR cardReset() throws CardException
    {
       card.disconnect(false); 
       connectToCard();
       atr = card.getATR();
       return atr;
    }
/*******************************************************************************/   
}
