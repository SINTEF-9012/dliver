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

import java.lang.Math;
import org.thingml.dliver.driver.Dliver;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.thingml.rtsync.core.ITimeSynchronizerLogger;

/**
 *
 * @author steffen
 */
public class TsErrorStat extends Thread  implements ITimeSynchronizerLogger{
    
    private int update_rate = 1000; // 1000 ms
    
    protected Dliver belt;
    protected int bitrate = 0;
    
    private ArrayList<TsErrorListener> listeners = new ArrayList<TsErrorListener>();
    
    private final int errorBuffSize = 40;
    private int errorIdx = 0;
    private long[] errorBuff = new long[errorBuffSize];
    private int timeSyncCount = 0;

    public void addDliverListener(TsErrorListener l) {
        listeners.add(l);
    }

    public void removeDliverListener(TsErrorListener l) {
        listeners.remove(l);
    }
    
    public TsErrorStat(Dliver b) {
        this.belt = b;
        
        for ( int i = 0; i < errorBuff.length; i++) {
            errorBuff[i] = 0;
        }
        errorIdx = 0;

        belt.getTimeSynchronizer().addLogger(this);
    }
    
    public void run() {
        long errorAverage;
        int  errorLevel;

        timeSyncCount = 0;
        
       
        while (belt.isConnected()) {
            try {
                Thread.sleep(update_rate);
            } catch (InterruptedException ex) {
                Logger.getLogger(TsErrorStat.class.getName()).log(Level.SEVERE, null, ex);
            }
            errorAverage = 0;
            for ( int i = 0; i < errorBuff.length; i++) {
                errorAverage += errorBuff[i];
            }
            errorAverage /= errorBuff.length;

            if ( Math.abs(errorAverage) < 6) {
                errorLevel = 0;
            } 
            else if ( Math.abs(errorAverage) < 12) {
                errorLevel = 1;
            }
            else if ( Math.abs(errorAverage) < 24) {
                errorLevel = 2;
            }
            else if ( Math.abs(errorAverage) < 48) {
                errorLevel = 3;
            }
            else if ( Math.abs(errorAverage) < 100) {
                errorLevel = 4;
            } 
            else {
                errorLevel = 5;
            }
            
            if (timeSyncCount == 0) {
                // Error - No information received
                errorLevel = -1;
                
                for ( int i = 0; i < errorBuff.length; i++) {
                    errorBuff[i] = 0;
                }
                errorIdx = 0;
            }
            for (TsErrorListener l : listeners) {
                l.errorAverage(errorAverage, errorLevel);
            }
            timeSyncCount = 0;
        }
        
        errorAverage = 0;
        errorLevel = -1;
        for (TsErrorListener l : listeners) {
            l.errorAverage(errorAverage, errorLevel);
        }
        belt.getTimeSynchronizer().removeLogger(this);

    }

    @Override
    public void timeSyncStart() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void timeSyncReady() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void timeSyncStop() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void timeSyncPingTimeout(int pingSeqNum, long tmt) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void timeSyncWrongSequence(int pingSeqNum, int pongSeqNum) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void timeSyncPong(int delay, int dtt, int dtr, int dts, long tsNoWrap) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void timeSyncDtsFilter(int dts) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void timeSyncErrorFilter(int error) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void timeSyncLog(String time, long ts, long tmt, long tmr, long delay, long offs, long error, long errorSum, long zeroOffset, long regOffsMs, int skipped, long tsOffset, long unlimError) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        errorBuff[errorIdx] = unlimError;
        errorIdx = (errorIdx + 1) % errorBuffSize;
        timeSyncCount++;
    }

    @Override
    public void timeSyncPongRaw(String time, int rcvPingSeqNum, int expectedPingSeqNum, long tmt, long tmr, long ts) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
