package org.kwick.view;

import com.jidesoft.plaf.basic.ThemePainter;
import com.jidesoft.swing.JideSplitButton;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.smartcardio.ATR;
import javax.smartcardio.CardException;
import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import org.kwick.Util.JavaCardUtil;
import org.kwick.model.*;


public class Shell extends javax.swing.JFrame 
{
    static JavaCard javaCard;
    static StyledDocument doc;
    String fromCard = "<-- ";
    String toCard = "--> ";
/*******************************************************************************/      
    static JideSplitButton createAuthenticateSplitButton(String name, Icon icon) 
    {
        final JideSplitButton button = new JideSplitButton(name);
        button.setForegroundOfState(ThemePainter.STATE_DEFAULT, Color.BLACK);
        button.setIcon(icon);
        button.add(new AbstractAction("Authenticate") 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {

            }
        });

        button.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                Authenticate auth = new Authenticate();
                boolean isAuth = auth.autenticate();
                if (isAuth)
                {
                    printMessage("Card is authenticated", false);
                }
            }
        });
        button.getPopupMenu().addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                System.out.println("menu is clicked");
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
        button.setFocusable(false);
        return button;
    }
/*******************************************************************************/    
    public Shell() 
    {
        initComponents();
        javaCard = new JavaCard();
        doc = (StyledDocument) textPane.getDocument();
        JideSplitButton authenSP = createAuthenticateSplitButton("", new javax.swing.ImageIcon(getClass().getResource("/images/enableIcons/auth_co.gif")));
        authenSP.setToolTipText("Authenticate with CM");
        toolBar.add(authenSP);
        authenSP.setEnabled(true); // disabled
        readers = new JideSplitButton();
        if ( javaCard.getTerminals() )
        {
            
        }
        readers.setIcon(getIcon());
        readers.getPopupMenu().addPopupMenuListener(new PopupMenuListener() 
        {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) 
            {
                javaCard.getTerminals();
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
        toolBar.add(readers);

        showWelcomeMessage();
        apdu_field.requestFocus();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        toolBar = new javax.swing.JToolBar();
        settings_Button = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        installPackage_Button = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        installApplet_Button = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        message_Label = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textPane = new javax.swing.JTextPane();
        cmd_Label = new javax.swing.JLabel();
        apdu_field = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        edit = new javax.swing.JMenu();
        clearTextPane = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JCShell v1.0");
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        toolBar.setBackground(new java.awt.Color(255, 255, 244));
        toolBar.setFloatable(false);
        toolBar.setRollover(true);

        settings_Button.setBackground(new java.awt.Color(255, 255, 244));
        settings_Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/enableIcons/prep_card.gif"))); // NOI18N
        settings_Button.setToolTipText("Diable PIN");
        settings_Button.setFocusable(false);
        settings_Button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        settings_Button.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        settings_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settings_ButtonActionPerformed(evt);
            }
        });
        toolBar.add(settings_Button);
        toolBar.add(jSeparator1);

        installPackage_Button.setBackground(new java.awt.Color(255, 255, 244));
        installPackage_Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/enableIcons/pkgupload_co.gif"))); // NOI18N
        installPackage_Button.setToolTipText("install package");
        installPackage_Button.setFocusable(false);
        installPackage_Button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        installPackage_Button.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        installPackage_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                installPackage_ButtonActionPerformed(evt);
            }
        });
        toolBar.add(installPackage_Button);
        toolBar.add(jSeparator2);

        installApplet_Button.setBackground(new java.awt.Color(255, 255, 244));
        installApplet_Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/enableIcons/appinstall_co.gif"))); // NOI18N
        installApplet_Button.setToolTipText("install applet");
        installApplet_Button.setFocusable(false);
        installApplet_Button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        installApplet_Button.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        installApplet_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                installApplet_ButtonActionPerformed(evt);
            }
        });
        toolBar.add(installApplet_Button);
        toolBar.add(jSeparator3);

        message_Label.setFont(new java.awt.Font("Tahoma", 0, 11));
        message_Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        message_Label.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        textPane.setEditable(false);
        textPane.setFont(new java.awt.Font("Cambria", 0, 48)); // NOI18N
        jScrollPane1.setViewportView(textPane);

        cmd_Label.setText("/cmd");

        apdu_field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                apdu_fieldActionPerformed(evt);
            }
        });

        jMenu1.setText("File");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Exit");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        edit.setText("Edit");

        clearTextPane.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_K, java.awt.event.InputEvent.CTRL_MASK));
        clearTextPane.setText("clear");
        clearTextPane.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearTextPaneActionPerformed(evt);
            }
        });
        edit.add(clearTextPane);

        jMenuBar1.add(edit);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(toolBar, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addComponent(cmd_Label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(message_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(apdu_field)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(toolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 616, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmd_Label)
                    .addComponent(apdu_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(message_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void settings_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settings_ButtonActionPerformed

        String command = "A02600010830303030FFFFFFFF";   
        displayCommand(JavaCardUtil.htos(JavaCardUtil.hexStringToByteArray(command)));
            byte[] apdu = JavaCardUtil.hexStringToByteArray(command);
            try 
            {
               if ( !JavaCard.sendApdu(apdu) )
               {
                    printMessage("Card Error", true);
               }
               else
               {
                    StatusHandler.checkStatus();                                
               }
            }
            catch (CardException ex)
            {
                printMessage("Card Error: \n"+ ex.getMessage(), true);
            }
            catch (IllegalArgumentException ex)
            {
                printMessage("APDU Error: \n"+ ex.getMessage(), true);
            }
    }//GEN-LAST:event_settings_ButtonActionPerformed

    private void installPackage_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_installPackage_ButtonActionPerformed
        JFileChooser capFileChooser = new JFileChooser(".");
        int option  = capFileChooser.showDialog(this, "Select Cap File");
        boolean isLoaded = true;
        switch(option)
        {          
            case JFileChooser.APPROVE_OPTION:
            {
                if (!Authenticate.isAuth())
                {
                    printMessage("Shell -->Card is not authenticated. Aborting", false);
                    return;
                }
                File file = capFileChooser.getSelectedFile();
                try 
                {
                    printMessage("Shell -->Loading Cap File ...", false);
                    
                    CapHandler handler = new CapHandler(file.getAbsolutePath());
 
                    try 
                    {
                        handler.start();
                    } catch (Exception ex) 
                    {
                        printMessage("Shell -->Unexpected Error."+ex.getMessage(), true);
                    }
                } catch (IOException ex) {
                    isLoaded = false;
                    handleErrorMessage("Invalid Cap File");
                    printMessage(""+ex.getMessage(), true);
                }

                    break;
            }
                default:
                    break;
        }
    }//GEN-LAST:event_installPackage_ButtonActionPerformed

    private void installApplet_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_installApplet_ButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_installApplet_ButtonActionPerformed

    private void apdu_fieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_apdu_fieldActionPerformed
        String command = apdu_field.getText().trim();
        apdu_field.setText("");
        apdu_field.requestFocus();

        if (command.equalsIgnoreCase("help") || command.equalsIgnoreCase("/help"))
        {
            displayCommand(command);
            printMessage(help(), false);
            return;
        }
        if (command.startsWith("/"))
        {
            displayCommand(command);
            System.out.println("command = "+command);
            String[] Do = command.split(" ");
            
            if (Do.length == 1)
            {
                if(Do[0].equalsIgnoreCase("/reset"))
                {
                    try {
                        ATR atr = javaCard.cardReset();
                        printMessage("Card Reseted.\nATR : "+JavaCardUtil.htos(atr.getBytes()), false);
                    } catch (CardException ex) {
                        printMessage("Could not Reset the card\n"+ex.getMessage(), true);
                    }
                }   
 
                else if(Do[0].equalsIgnoreCase("/atr"))
                {
                    try 
                    {
                        ATR atr = javaCard.getAtr();
                        printMessage("ATR : "+JavaCardUtil.htos(atr.getBytes()), false);
                    } catch (Exception ex) {
                        printMessage("Could not get ATR\n"+ex.getMessage(), true);
                    }
                }                 

                else if (Do[0].equalsIgnoreCase("/clear-card"))
                {
                    try 
                    {
                        if(javaCard.DoClear())
                        {
                            StatusHandler.checkStatus();
                            if(javaCard.DoClear())
                            {
                                StatusHandler.checkStatus();
                                if(javaCard.internalCommand())
                                {
                                    StatusHandler.checkStatus();
                                }
                            }
                        }
                    } 
                    catch ( IllegalArgumentException | CardException ex) 
                    {
                        printMessage("could not clear the card\n"+ex.getMessage(), true);
                    }
                }  
                
                else if (Do[0].equalsIgnoreCase("/load"))
                {
                    displayCommand(command);
                    String path = command.split(" ")[1];
                    TestSIM.parseFile(path);             
                }
                else
                {
                    printMessage("Command not found", false);
                }                
            }
           
           else if (Do.length == 2)
            {                                
                if (Do[0].equalsIgnoreCase("/get"))
                {
                    if(Do[1].equalsIgnoreCase("admin-code") || Do[1].equalsIgnoreCase("AC"))
                    {
                        try 
                        {
                            javaCard.DoGetAdminCode();
                        } catch (Exception ex) {
                            printMessage("Could not get Admin Code", true);
                        }
                    }
                    
                    else if(Do[1].equalsIgnoreCase("iccid"))
                    {
                        String iccid = null;
                        try {
                            iccid = javaCard.getICCID();
                        } catch (Exception ex) {
                        printMessage("Could not get ICCID", true);
                        }
                        String iccidFormated = "";
                        if (iccid != null)
                        {
                            iccidFormated = JavaCard.getFormatedICCID(iccid).toUpperCase().substring(0, iccid.length()-2);
                        }
                        Shell.printMessage("ICCID ="+iccidFormated, false);
                        StatusHandler.checkStatus();
                    } 
                    else
                    {
                        printMessage("Command not found", false);
                    }                
                    
                }
                else
                {
                    printMessage("Command not found", false);
                }                
                
            }
            
            else if (Do.length == 3)
            {
                if (Do[0].equalsIgnoreCase("/disable"))
                {
                    if (Do[1].equalsIgnoreCase("Pin1") || Do[1].equalsIgnoreCase("CHV1"))
                    {
                        String PIN1 = JavaCardUtil.byteArrayToHexString(Do[2].getBytes());
                        
                        if (PIN1.length() !=  8)
                        {
                            printMessage("Shell: Invalid PIN1 length", true);
                            return;
                        }
                        
                        String cmd = "A026000108" + PIN1 + "FFFFFFFF";
                        displayCommand(JavaCardUtil.htos(JavaCardUtil.hexStringToByteArray(cmd)));
                        byte[] apdu = JavaCardUtil.hexStringToByteArray(cmd);
                        try 
                        {
                            if ( !JavaCard.sendApdu(apdu) )
                            {
                                    printMessage("Card Error", true);
                            }
                            else
                            {
                                    StatusHandler.checkStatus();                                
                            }
                        }
                        catch (CardException ex)
                        {
                            printMessage("Card Error: \n"+ ex.getMessage(), true);
                        }
                        catch (IllegalArgumentException ex)
                        {
                            printMessage("APDU Error: \n"+ ex.getMessage(), true);
                        }                        
                    }
                    
                    else  if (Do[1].equalsIgnoreCase("Pin2") || Do[1].equalsIgnoreCase("CHV2"))
                    {
                        printMessage("Shell: Not Implemented yet", false);
                    }
                    
                    else
                    {
                        printMessage("Command not found", false);
                    }                
                    
                } 

                else if (Do[0].equalsIgnoreCase("/enable"))
                {
                    if (Do[1].equalsIgnoreCase("Pin1") || Do[1].equalsIgnoreCase("CHV1"))
                    {
                        String PIN1 = JavaCardUtil.byteArrayToHexString(Do[2].getBytes());
                        
                        if (PIN1.length() !=  8)
                        {
                            printMessage("Shell: Invalid PIN1 length", true);
                            return;
                        }
                        
                        String cmd = "A028000108" + PIN1 + "FFFFFFFF";
                        displayCommand(JavaCardUtil.htos(JavaCardUtil.hexStringToByteArray(cmd)));
                        byte[] apdu = JavaCardUtil.hexStringToByteArray(cmd);
                        try 
                        {
                            if ( !JavaCard.sendApdu(apdu) )
                            {
                                    printMessage("Card Error", true);
                            }
                            else
                            {
                                    StatusHandler.checkStatus();                                
                            }
                        }
                        catch (CardException ex)
                        {
                            printMessage("Card Error: \n"+ ex.getMessage(), true);
                        }
                        catch (IllegalArgumentException ex)
                        {
                            printMessage("APDU Error: \n"+ ex.getMessage(), true);
                        }                        
                    }
                    
                    else if (Do[1].equalsIgnoreCase("Pin2") || Do[1].equalsIgnoreCase("CHV2"))
                    {
                        printMessage("Shell: Not Implemented yet", false);
                    }
                    
                    else
                    {
                        printMessage("Command not found", false);
                    }                
                    
                }                                 
                
               else if (Do[0].equalsIgnoreCase("/verify"))
                {
                    if(Do[1].equalsIgnoreCase("admin-code") || Do[1].equalsIgnoreCase("AC"))
                    {
                        if (Do[2].length() != 8)
                        {
                            printMessage("Shell: Invalid Admin Code length", true);
                            return;
                        }
                        
                        String cmd = "A020000408"+Do[2];
                        displayCommand(JavaCardUtil.htos(JavaCardUtil.hexStringToByteArray(cmd)));
                        byte[] apdu = JavaCardUtil.hexStringToByteArray(cmd);
                        try 
                        {
                            if ( !JavaCard.sendApdu(apdu) )
                            {
                                    printMessage("Card Error", true);
                            }
                            else
                            {
                                    StatusHandler.checkStatus();                                
                            }
                        }
                        catch (CardException ex)
                        {
                            printMessage("Card Error: \n"+ ex.getMessage(), true);
                        }
                        catch (IllegalArgumentException ex)
                        {
                            printMessage("APDU Error: \n"+ ex.getMessage(), true);
                        }                         
                        
//                        try 
//                        {
//                            javaCard.DoGetAdminCode();
//                        } catch (Exception ex) {
//                            printMessage("Could not get Admin Code", true);
//                        }
//                       javaCard.verifyAdminCode();
//                        StatusHandler.checkStatus();
                    }
                    
                    else if(Do[1].equalsIgnoreCase("PIN1") || Do[1].equalsIgnoreCase("CHV1"))
                    {
                        if (Do[2].length() != 4)
                        {
                            printMessage("Shell: Invalid PIN1 length", true);
                            return;
                        }
                        
                        String cmd = "A020000108"+JavaCardUtil.byteArrayToHexString(Do[2].getBytes()) + "FFFFFFFF";
                        displayCommand(JavaCardUtil.htos(JavaCardUtil.hexStringToByteArray(cmd)));
                        byte[] apdu = JavaCardUtil.hexStringToByteArray(cmd);
                        try 
                        {
                            if ( !JavaCard.sendApdu(apdu) )
                            {
                                    printMessage("Card Error", true);
                            }
                            else
                            {
                                    StatusHandler.checkStatus();                                
                            }
                        }
                        catch (CardException ex)
                        {
                            printMessage("Card Error: \n"+ ex.getMessage(), true);
                        }
                        catch (IllegalArgumentException ex)
                        {
                            printMessage("APDU Error: \n"+ ex.getMessage(), true);
                        } 
                    }
                    
                    else if(Do[1].equalsIgnoreCase("PIN2") || Do[1].equalsIgnoreCase("CHV2"))
                    {
                        if (Do[2].length() != 4)
                        {
                            printMessage("Shell: Invalid PIN2 length", true);
                            return;
                        }
                        
                        String cmd = "A020000208"+JavaCardUtil.byteArrayToHexString(Do[2].getBytes()) + "FFFFFFFF";
                        displayCommand(JavaCardUtil.htos(JavaCardUtil.hexStringToByteArray(cmd)));
                        byte[] apdu = JavaCardUtil.hexStringToByteArray(cmd);
                        try 
                        {
                            if ( !JavaCard.sendApdu(apdu) )
                            {
                                    printMessage("Card Error", true);
                            }
                            else
                            {
                                    StatusHandler.checkStatus();                                
                            }
                        }
                        catch (CardException ex)
                        {
                            printMessage("Card Error: \n"+ ex.getMessage(), true);
                        }
                        catch (IllegalArgumentException ex)
                        {
                            printMessage("APDU Error: \n"+ ex.getMessage(), true);
                        } 
                    }
                    else
                    {
                        printMessage("Shell: Command not found", true);
                    }
                } 
            }
                
                else if (Do.length == 4)
                {
                    if (Do[0].equalsIgnoreCase("/change"))
                    {
                        if (Do[1].equalsIgnoreCase("Pin1") || Do[1].equalsIgnoreCase("CHV1"))
                        {
                            String oldPin = JavaCardUtil.byteArrayToHexString(Do[2].getBytes());
                            String newPin = JavaCardUtil.byteArrayToHexString(Do[3].getBytes());
                            if (oldPin.length() !=  8)
                            {
                                printMessage("Shell: Old PIN1 length is Invalid", true);
                                return;
                            }
                            if (newPin.length() !=  8)
                            {
                                printMessage("Shell: New PIN1 length is Invalid", true);
                                return;
                            }

                            String cmd = "A024000110" + oldPin + "FFFFFFFF" + newPin + "FFFFFFFF";
                            displayCommand(JavaCardUtil.htos(JavaCardUtil.hexStringToByteArray(cmd)));
                            byte[] apdu = JavaCardUtil.hexStringToByteArray(cmd);
                            try 
                            {
                                if ( !JavaCard.sendApdu(apdu) )
                                {
                                        printMessage("Card Error", true);
                                }
                                else
                                {
                                        StatusHandler.checkStatus();                                
                                }
                            }
                            catch (CardException ex)
                            {
                                printMessage("Card Error: \n"+ ex.getMessage(), true);
                            }
                            catch (IllegalArgumentException ex)
                            {
                                printMessage("APDU Error: \n"+ ex.getMessage(), true);
                            }                        
                        }
                    
                        else if (Do[1].equalsIgnoreCase("Pin2") || Do[1].equalsIgnoreCase("CHV2"))
                        {
                            String oldPin = JavaCardUtil.byteArrayToHexString(Do[2].getBytes());
                            String newPin = JavaCardUtil.byteArrayToHexString(Do[3].getBytes());

                            if (oldPin.length() !=  8)
                            {
                                printMessage("Shell: Old PIN2 length is Invalid", true);
                                return;
                            }
                            if (newPin.length() !=  8)
                            {
                                printMessage("Shell: New PIN2 length is Invalid", true);
                                return;
                            }

                            String cmd = "A024000210" + oldPin + "FFFFFFFF" + newPin + "FFFFFFFF";

                            displayCommand(JavaCardUtil.htos(JavaCardUtil.hexStringToByteArray(cmd)));
                        
                            byte[] apdu = JavaCardUtil.hexStringToByteArray(cmd);
                            try 
                            {
                                if ( !JavaCard.sendApdu(apdu) )
                                {
                                        printMessage("Card Error", true);
                                }
                                else
                                {
                                        StatusHandler.checkStatus();                                
                                }
                            }
                            catch (CardException ex)
                            {
                                printMessage("Card Error: \n"+ ex.getMessage(), true);
                            }
                            catch (IllegalArgumentException ex)
                            {
                                printMessage("APDU Error: \n"+ ex.getMessage(), true);
                            }                        
                        } 
                        
                        else
                        {
                            printMessage("Command not found", false);
                        }                
                        
                    }
                    else if (Do[0].equalsIgnoreCase("/unblock"))
                    {
                        if (Do[1].equalsIgnoreCase("Pin1") || Do[1].equalsIgnoreCase("CHV1"))
                        {
                            String puk1 = JavaCardUtil.byteArrayToHexString(Do[2].getBytes());
                            String newPin = JavaCardUtil.byteArrayToHexString(Do[3].getBytes());
                            if (puk1.length() !=  16)
                            {
                                printMessage("Shell: Unblock PIN1 code length is Invalid", true);
                                return;
                            }
                            if (newPin.length() !=  8)
                            {
                                printMessage("Shell: New PIN1 length is Invalid", true);
                                return;
                            }

                            String cmd = "A02C000010" + puk1 + newPin + "FFFFFFFF";
                            displayCommand(JavaCardUtil.htos(JavaCardUtil.hexStringToByteArray(cmd)));
                            byte[] apdu = JavaCardUtil.hexStringToByteArray(cmd);
                            try 
                            {
                                if ( !JavaCard.sendApdu(apdu) )
                                {
                                        printMessage("Card Error", true);
                                }
                                else
                                {
                                        StatusHandler.checkStatus();                                
                                }
                            }
                            catch (CardException ex)
                            {
                                printMessage("Card Error: \n"+ ex.getMessage(), true);
                            }
                            catch (IllegalArgumentException ex)
                            {
                                printMessage("APDU Error: \n"+ ex.getMessage(), true);
                            }                        
                        } 
                        
                        else if (Do[1].equalsIgnoreCase("Pin2") || Do[1].equalsIgnoreCase("CHV2"))
                        {
                            String puk2 = JavaCardUtil.byteArrayToHexString(Do[2].getBytes());
                            String newPin = JavaCardUtil.byteArrayToHexString(Do[3].getBytes());
                            if (puk2.length() !=  16)
                            {
                                printMessage("Shell: Disable PIN2 code length is Invalid", true);
                                return;
                            }
                            if (newPin.length() !=  8)
                            {
                                printMessage("Shell: New PIN2 length is Invalid", true);
                                return;
                            }

                            String cmd = "A02C000210" + puk2 + newPin + "FFFFFFFF";
                            displayCommand(JavaCardUtil.htos(JavaCardUtil.hexStringToByteArray(cmd)));
                            byte[] apdu = JavaCardUtil.hexStringToByteArray(cmd);
                            try 
                            {
                                if ( !JavaCard.sendApdu(apdu) )
                                {
                                        printMessage("Card Error", true);
                                }
                                else
                                {
                                        StatusHandler.checkStatus();                                
                                }
                            }
                            catch (CardException ex)
                            {
                                printMessage("Card Error: \n"+ ex.getMessage(), true);
                            }
                            catch (IllegalArgumentException ex)
                            {
                                printMessage("APDU Error: \n"+ ex.getMessage(), true);
                            }                        
                        } 
                        
                        else
                        {
                            printMessage("Command not found", false);
                        }                

                    }
                    else
                    {
                        printMessage("Command not found", false);
                    }                     
                }
            else
            {
                printMessage("Command not found", false);
            }
        }
        else
        {
            if (!JavaCardUtil.validateCmd(command))
            {
                printMessage( JavaCardUtil.getMessage(), true );
            }
            else
            {
                displayCommand(JavaCardUtil.htos(JavaCardUtil.hexStringToByteArray(command)));
                byte[] apdu = JavaCardUtil.hexStringToByteArray(command);
                try 
                {
                    if ( !JavaCard.sendApdu(apdu) )
                    {
                            printMessage("Card Error", true);
                    }
                    else
                    {
                            StatusHandler.checkStatus();
                    }
                }
                catch (CardException ex)
                {
                    printMessage("Card Error: \n"+ ex.getMessage(), true);
                }
                catch (IllegalArgumentException ex)
                {
                    printMessage("APDU Error: \n"+ ex.getMessage(), true);
                }
            }
        }
    }//GEN-LAST:event_apdu_fieldActionPerformed

    private void clearTextPaneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearTextPaneActionPerformed
       textPane.setText("");
       apdu_field.requestFocus();
    }//GEN-LAST:event_clearTextPaneActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } 
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
           JOptionPane.showMessageDialog(null, "LnF not supported ..");
        }
        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new Shell().setVisible(true);
            }
        });
    }
    JComboBox comboBox = new JComboBox();
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField apdu_field;
    private javax.swing.JMenuItem clearTextPane;
    private javax.swing.JLabel cmd_Label;
    private javax.swing.JMenu edit;
    private javax.swing.JButton installApplet_Button;
    private javax.swing.JButton installPackage_Button;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    public static javax.swing.JLabel message_Label;
    private javax.swing.JButton settings_Button;
    private javax.swing.JTextPane textPane;
    protected static javax.swing.JToolBar toolBar;
    // End of variables declaration//GEN-END:variables
    public static JideSplitButton readers ;
/************************************************************************************/	 
    private Icon getIcon()
    {
        return new javax.swing.ImageIcon(getClass().getResource("/images/enableIcons/connect_co.gif"));
    }
/*******************************************************************************/
    public static void handleErrorMessage(String message)
    {
       message_Label.setText(message+" "); 
    }
/*******************************************************************************/
    private void showWelcomeMessage()
    {
        try 
        {
            Style style = doc.addStyle("copyrighted", null);
            Style productName = doc.addStyle("ProductName", null);
            StyleConstants.setForeground(productName, Color.blue);
            StyleConstants.setFontSize(productName, 17);
            StyleConstants.setFontSize(style, 11);
            StyleConstants.setForeground(style, Color.black);
            doc.insertString(doc.getLength(), "\n\t\tJCShell version 1.0\n", productName);
            doc.insertString(doc.getLength(), "   \t\t        Copyrighted © 2012\n", style);
            doc.insertString(doc.getLength(), "    ----------------------------"
                    + "------------------------------------------"
                    + "-\n", productName);
        } 
        catch (BadLocationException ex) {
            
        }
    }
/*******************************************************************************/    
    public static void displayCommand(String command)
    {
        try 
        {
            doc.insertString(doc.getLength(), "cmd>"+command+"\n", getCmdDisStyle());
        }
        catch (BadLocationException ex) 
        {
//            ex.printStackTrace();
        }
    }
/*******************************************************************************/    
    private static Style getCmdDisStyle()
    {
         Style cmdDis = doc.addStyle("command", null);
         StyleConstants.setForeground(cmdDis, Color.black);
         StyleConstants.setFontSize(cmdDis, 16);
         return cmdDis;
    }
/*******************************************************************************/    
    private Style getStatusDisStyle()
    {
         Style cmdDis = doc.addStyle("command", null);
         StyleConstants.setForeground(cmdDis, Color.blue);
         StyleConstants.setFontSize(cmdDis, 13);
         return cmdDis;
    }
/*******************************************************************************/ 
     private static Style getErrorDisStyle()
    {
         Style errDis = doc.addStyle("Error", null);
         StyleConstants.setForeground(errDis, Color.red);
         StyleConstants.setFontSize(errDis, 16);
         return errDis;
    }
/*******************************************************************************/ 
     private static Style getSuccessDisStyle()
    {
         Style errDis = doc.addStyle("Error", null);
         StyleConstants.setForeground(errDis, Color.blue);
         StyleConstants.setFontSize(errDis, 16);
         return errDis;
    }
/*******************************************************************************/     
     private static Style getMessageDisStyle()
    {
         Style errDis = doc.addStyle("Error", null);
         StyleConstants.setForeground(errDis, Color.blue);
         StyleConstants.setFontSize(errDis, 16);
         return errDis;
    }    
/*******************************************************************************/ 
    public static void printMessage( String error, boolean IsErr)
    {
        try
        {
            if (IsErr) {
                doc.insertString(doc.getLength(), ""+error+"\n", getErrorDisStyle());
            }
            else {
                doc.insertString(doc.getLength(), ""+error+"\n", getMessageDisStyle());
            }    
        } 
        catch (BadLocationException ex) 
        {
            //ex.printStackTrace();
        }
    }
/*******************************************************************************/ 
    public static void printStatus( String status, boolean IsErr)
    {
        try
        {
            if (IsErr) {
                doc.insertString(doc.getLength(), " "+status+"\n", getErrorDisStyle());
            }
            else {
                doc.insertString(doc.getLength(), " "+status+"\n", getSuccessDisStyle());
            } 
        } 
        catch (BadLocationException ex) 
        {
           // ex.printStackTrace();
        }
    }
/*******************************************************************************/ 
    public static void abort(String reason, String title)
    {
        JOptionPane.showMessageDialog(null, reason, title, JOptionPane.ERROR_MESSAGE);
        System.exit(-1);
    }
/*******************************************************************************/     
    public static String help()
    {
        String help = "Command: ' /atr'\n\tTo check the ATR of a card"
                + " (it will not reset the card)\nResponse: ATR of the card";
               help += "\nCommand: '/reset'\n\tTo Reset a card\nResponse: Card will be Reseted";
        return help;
    }
    
    public static boolean sendCommands_withData(String command, String data, String resp)
    {
        displayCommand(JavaCardUtil.htos(JavaCardUtil.hexStringToByteArray(command)));
        byte[] apdu = JavaCardUtil.hexStringToByteArray(command);
        try 
        {
            if ( !JavaCard.sendApdu(apdu) )
            {
                printMessage("Card Error", true);
                System.out.println("Card Hardware error");
            }
            else
            {
                String statusWords = Integer.toHexString(JavaCard.getStatusWords());
                System.out.println(" Card Response:"+statusWords+" | Expected: "+ resp);
                if (!statusWords.equalsIgnoreCase(resp))
                {
                    printMessage("Response not match", true);
                    System.out.println("Resp not match");
                }
                else
                {
                    System.out.println("Matched: Card Response:"+statusWords+"| Expected: "+ resp);
                }
                if (!JavaCardUtil.htos( JavaCard.getData()).equalsIgnoreCase(data))
                {
                    System.out.println("Data not match");
                    printMessage("Data not match", true);
                }
            }
        }
        catch (CardException ex)
        {
            printMessage("Card Error: \n"+ ex.getMessage(), true);
        }
        catch (IllegalArgumentException ex)
        {
            printMessage("APDU Error: \n"+ ex.getMessage(), true);
        }
        return true;
    }
    public static boolean sendCommands(String command, String resp)
    {
        displayCommand(JavaCardUtil.htos(JavaCardUtil.hexStringToByteArray(command)));
        byte[] apdu = JavaCardUtil.hexStringToByteArray(command);
        try 
        {
            if ( !JavaCard.sendApdu(apdu) )
            {
                printMessage("Card Error", true);
                System.out.println("Card Hardware error");
            }
            else
            {
                String statusWords = Integer.toHexString(JavaCard.getStatusWords());
                System.out.println(" Card Response:"+statusWords+"| Expected: "+ resp);
//                if (!statusWords.equalsIgnoreCase(resp))
//                {
//                    System.out.println("Resp not match");
//                    
//                    printMessage("Response not match", true);
//                }
            }
        }
        catch (CardException ex)
        {
            printMessage("Card Error: \n"+ ex.getMessage(), true);
        }
        catch (IllegalArgumentException ex)
        {
            printMessage("APDU Error: \n"+ ex.getMessage(), true);
        }
        return true;
    }
    
    public static boolean RST()
    {
        try 
        {
            javaCard.cardReset();
        } catch (CardException ex) {
            printMessage("Could not reset", true);
        }
        return true;
    }
}
