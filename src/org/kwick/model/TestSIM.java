
package org.kwick.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.kwick.view.Shell;

/**
 *
 * @author Dell
 */
public class TestSIM 
{
    static boolean isCommand = false;
    static boolean isRST = false;
    static boolean isData = false;
    
    static String command = "";
    static String response = "";
    static String data = "";
    
    public static void parseFile(String filePath)
    {
        try {
            BufferedReader in = null;
            String line = "";
            in = new BufferedReader(new FileReader(filePath));
            try 
            {
                while ((line = in.readLine()) != null) 
                {
                    line =  line.trim();
                    if (line.startsWith("cmd") || line.trim().startsWith("CMD"))
                    {
                        isCommand = true;
                        command = line.substring(3).replaceAll(" ", "").trim();
                        System.out.println("/send "+command);
                    }
                    else if(line.trim().startsWith("("))
                    {
                        response = line.substring(1, line.length()-1).replaceAll(" ", "").trim();
                        System.out.println("Response is: "+response);
                    }
                    else if(line.trim().startsWith("["))
                    {
                        System.out.println("Actual Data is: "+line);   
                        data = line.trim().substring(1, line.length()-1).replaceAll(" ", "").trim();
                        //System.out.println("Data is: "+data);   
                        isData = true;
                    }
                    else if(line.trim().startsWith("RST") || line.trim().startsWith("rst"))
                    {
                        isRST = true;
                        isCommand = false;
                        isData = false;
                        System.out.println("RST");
                    }
                    if (isRST)
                    {
                        Shell.RST();
                    }
//                    if (isCommand && isData)
//                    {
//                        Shell.sendCommands_withData(command, data, response);
//                    }
//                    else if(isCommand && !isData)
//                    {
//                        Shell.sendCommands(command, response);
//                    }
                    if (isCommand)
                    {    
                        //Shell.sendCommands(command, "");
                    }
                    isCommand = false;
                    isRST = false;
                    isData = false;
                }
            } 
            catch (IOException ex) 
            {
                System.out.println("IO error"+ex.getMessage());
            }
        } 
        catch (FileNotFoundException ex) 
        {
            System.out.println("File not found"+ex.getMessage());           
        }
    }
    
//    public static void main(String[] arg)
//    {
//        TestSIM test =  new TestSIM();
//        //test.parseFile("E:/init/init.txt");
//    }
}
