/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.thingml.dliver.desktop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author steffend
 */
public class ReadVersionInfo {

    public ReadVersionInfo() {
    }

    public String GetVersionString(String moduleName) {
        String ret = "";
        String fileName = "/"+ moduleName +".version/version.txt";
        try {
            InputStream in = getClass().getResourceAsStream(fileName);
            if (in != null ) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                while (reader.ready()) {
                    ret += reader.readLine() + "\n";
                }
            } else {
                System.out.println("Cannot find version file = " + fileName);
            }
        } catch (IOException ex) {
            Logger.getLogger(ReadVersionInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    public Properties GetVersionProperties(String moduleName) {
        Properties ret = new Properties();
        String fileName = "/"+ moduleName +".version/version.txt";
        try {
            InputStream in = getClass().getResourceAsStream(fileName);
            if (in != null ) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                while (reader.ready()) {
                    String line = reader.readLine();
                    int colonPos = line.indexOf(":");
                    if (colonPos != -1) {
                        if (line.substring(colonPos+1, colonPos+2).contentEquals("$") == false) {
                            String prev = ret.getProperty(line.substring(0, colonPos));
                            if (prev == null) {
                                ret.setProperty(line.substring(0, colonPos), line.substring(colonPos+1));
                            } else {
                                ret.setProperty(line.substring(0, colonPos), prev + " " + line.substring(colonPos+1));
                            }
                        }
                    }
                }
            } else {
                System.out.println("Cannot find version file = " + fileName);
            }
        } catch (IOException ex) {
            Logger.getLogger(ReadVersionInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }
    
}