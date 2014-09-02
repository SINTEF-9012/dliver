/**
 * Copyright (C) 2014 SINTEF <steffen.dalgard@sintef.no>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thingml.dliver.desktop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.thingml.dliver.driver.DliverListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.thingml.dliver.driver.Dliver;
import javax.swing.Timer;

/**
 *
 * @author steffend
 */
public class FileLogCombiner {
    
    private SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS"); // HH:mm:ss.SSS
    private String SEPARATOR = "\t";
    
    private String fileName;
    private boolean logging = false;
    private PrintWriter log;
    private PrintWriter logRt;
    private PrintWriter logPb;
    private String[] header;
    public    String[] data;

    private final int tsNone = 50000;
    private final int tsOnlyEpoch = 50001;
    private final int epochNone = 0;
    private int currTs = tsNone;
    private long currEpoch = epochNone;
    
    private Dliver belt;
    
    public FileLogCombiner(Dliver belt, String[] header, String[] data) {
        this.belt = belt;
        this.header = header;
        this.data = data;
        this.header[0] = "RXTime" + SEPARATOR + "Corrtime_HMS" + SEPARATOR + "Corrtime_Epoch" + SEPARATOR + "RawTime";
    }
    
    public boolean isLogging() {
        return logging;
    }
    
    public void createLogInFolder(String fileName, File sFolder) {

        try {
            this.fileName = fileName;
            logRt = new PrintWriter(new FileWriter(new File(sFolder, fileName + ".txt")));
            logPb = new PrintWriter(new FileWriter(new File(sFolder, fileName + "_playback.txt")));
            logRt.print(header[0]);
            logPb.print(header[0]);
            for ( int i = 1; i < header.length; i++) {
                logRt.print(SEPARATOR + header[i]);
                logPb.print(SEPARATOR + header[i]);
            }
            logRt.println(" ");
            logPb.println(" ");
            log = logRt;
           
        } catch (IOException ex) {
            Logger.getLogger(FileLogCombiner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void startLogging() {
        System.out.println("FileLogCombiner(" + fileName + " - Starting logger");
        currTs = tsNone;
        currEpoch = epochNone;
        clearData();
        logging = true;
    }
    
    
    public void stopLogging() {
        //if (logging || request_start) {
        if (logging) {
            flushData();
            logging = false;
            //request_start = false;
            logRt.close();
            logPb.close();
            logRt = null;
            logPb = null;
            log = null;
        }
    }
    
    private String currentTimeStampEpoch() {
        //return timestampFormat.format( Calendar.getInstance().getTime());
        //return timestampFormat.format( Calendar.getInstance().getTime()) + SEPARATOR + (System.currentTimeMillis()-startTime);
        return "" + System.currentTimeMillis();
    }
    
    private String currentTimeStampHms() {
        //return timestampFormat.format( Calendar.getInstance().getTime());
        //return timestampFormat.format( Calendar.getInstance().getTime()) + SEPARATOR + (System.currentTimeMillis()-startTime);
        return "" + timestampFormat.format(System.currentTimeMillis());
    }
    
    private String calculatedAndRawTimeStamp(int belt_timestamp) {
        //return timestampFormat.format( Calendar.getInstance().getTime());
        //return (t+refTime-cbStartTime)*4;
        //long delta = System.currentTimeMillis() - belt.getEpochTimestamp(belt_timestamp);
        //if ((delta > 2000) || (delta < -2000)) System.out.println("Large delta detected: " + delta);
        long epochTimestamp = belt.getEpochTimestamp(belt_timestamp);
        return "" + timestampFormat.format(epochTimestamp) + SEPARATOR + epochTimestamp + SEPARATOR + belt_timestamp*4;
    }

    private String calculatedAndRawEpoch(long epochTimestamp) {
        return "" + timestampFormat.format(epochTimestamp) + SEPARATOR + epochTimestamp + SEPARATOR + " ";
    }

    private void clearData() {
        for ( int i=0; i<data.length; i++) data[i] = "";
    }

    private void flushData() {
        if (logging) {
            if( currEpoch != epochNone ) {     // Some data present
                log.print( data[0] );              // Print data
                data[0] = "";                      // Clear data
                for ( int i=1; i<data.length; i++) {
                    log.print( SEPARATOR + data[i] );  // Print data
                    data[i] = "";                      // Clear data
                }
                log.println(" ");
                currTs = tsNone;
                currEpoch = epochNone;
            }
        }
    }

    public void newLogEntryTs(int newTimestamp) {
        long newEpoch = belt.getEpochTimestamp(newTimestamp);
        boolean doFlush = false;
        
        if ( logging) {
            if (currTs == tsOnlyEpoch) doFlush = true;  // Previous of different type 
            if (currTs != newTimestamp) doFlush = true; // New time ts
            if (currEpoch != newEpoch) doFlush = true;  // New time epoch

            if ( doFlush == true ) { // Different data ... start on new
                flushData();
                // Store new timestamp
                currTs = newTimestamp;
                currEpoch = newEpoch;
                data[0] = "" + currentTimeStampEpoch() + SEPARATOR + calculatedAndRawTimeStamp(currTs);
            }
        }
    }

    public void newLogEntryEpoch(long newEpoch) {
        boolean doFlush = false;
        
        if (logging) {
            if (currTs != tsOnlyEpoch) doFlush = true;  // Previous of different type 
            if (currEpoch != newEpoch) doFlush = true;  // New time epoch

            if ( doFlush == true ) { // Different data ... start on new
                flushData();
                // Store new timestamp
                currTs = tsOnlyEpoch;
                currEpoch = newEpoch;
                data[0] = "" + currentTimeStampEpoch() + SEPARATOR + calculatedAndRawEpoch(currEpoch);
            }
        }
    }


    public void playStart() {
        flushData();
        log = logPb;
    }

    public void playStop() {
        flushData();
        log = logRt;
    }

    
    
}
