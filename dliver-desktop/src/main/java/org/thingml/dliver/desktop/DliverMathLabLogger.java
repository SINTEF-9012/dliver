/**
 * Copyright (C) 2015 SINTEF <steffen.dalgard@sintef.no>
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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.io.PrintWriter;
import java.net.SocketException;
import java.net.UnknownHostException;
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
public class DliverMathLabLogger implements DliverListener, ActionListener {
    
    private SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS"); // HH:mm:ss.SSS
    private String SEPARATOR = "\t";
    
    protected boolean logging = false;
    //protected boolean request_start = false;
    //protected long startTime = 0;
    //protected long cbStartTime = 0;

    //protected FileLogCombiner measLog;
    //protected final int measIdxTime   = 0;
    //protected final int measIdxPos  = 1;
    //protected final int measIdxAct = 2;
    //protected final int measIdxStepCount = 3;
    //protected final int measIdxHr = 4;
    //protected final int measIdxSkinTemp = 5;
    //protected final int measIdxSize   = 6;
    //protected String[] measHeader = new String[measIdxSize];
    //protected String[] measData = new String[measIdxSize];
    
    //protected FileLogCombiner icgLog;
    //protected final int icgIdxTime   = 0;
    //protected final int icgIdxIcgAbsAc  = 1;
    //protected final int icgIdxIcgAbsDer = 2;
    //protected final int icgIdxIcgAbsDerPc = 3;
    //protected final int icgIdxSize   = 4;
    //protected String[] icgHeader = new String[icgIdxSize];
    //protected String[] icgData = new String[icgIdxSize];
    
    //protected FileLogCombiner ppgLog;
    //protected final int ppgIdxTime  = 0;
    //protected final int ppgIdxPpgRaw    = 1;
    //protected final int ppgIdxPpgDer    = 2;
    //protected final int ppgIdxPpgDerPc  = 3;
    //protected final int ppgIdxSize   = 4;
    //protected String[] ppgHeader = new String[ppgIdxSize];
    //protected String[] ppgData = new String[ppgIdxSize];
    
    protected MathLabLogCombiner comLog;
    protected final int comIdxTime           =  0;
    protected final int comIdxPpgRaw         =  1;
    protected final int comIdxPpgDer         =  2;
    protected final int comIdxPpgDerPc       =  3;
    protected final int comIdxIcgAbsAc       =  4;
    protected final int comIdxIcgAbsDer      =  5;
    protected final int comIdxIcgAbsDerPc    =  6;
    protected final int comIdxEcg            =  7;
    protected final int comIdxHr             =  8;
    protected final int comIdxHri            =  9;
    protected final int comIdxPtt            = 10;
    protected final int comIdxAct            = 11;
    protected final int comIdxPos            = 12;
    protected final int comIdxEvEcgR         = 13;
    protected final int comIdxEvIcgC         = 14;
    protected final int comIdxEvIcgB         = 15;
    protected final int comIdxEvPpgMin       = 16;
    protected final int comIdxEvPpgFoot      = 17;
    protected final int comIdxEvPpgTangent   = 18;
    protected final int comIdxEvDppgMax      = 19;
    protected final int comIdxEvPttActive    = 20;
    protected final int comIdxEvPttResultErr = 21;
    protected final int comIdxSize           = 22;
    protected String[] comHeader = new String[comIdxSize];
    protected String[] comData = new String[comIdxSize];
    
    protected Timer startTimer = null;

    protected DatagramSocket udpSocket;
    protected InetAddress IPAddress;
    protected int IPPort;
    
    
    protected boolean eCGEpoch = false;

    public boolean iseCGEpoch() {
        return eCGEpoch;
    }
    
    private Dliver belt;
    
    public DliverMathLabLogger(String address, int port, Dliver belt) {
        
        this.belt = belt;
        this.eCGEpoch = false;
        belt.getSerialNumber();
        
        
        try {
            this.udpSocket = new DatagramSocket();
        } catch (SocketException ex) {
            Logger.getLogger(DliverMathLabLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            this.IPAddress = InetAddress.getByName(address);
        } catch (UnknownHostException ex) {
            Logger.getLogger(DliverMathLabLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.IPPort = port;

    }
        
        
    public boolean isLogging() {
        return logging;
    }
    
    public void startLoggingToUdp() {
        //ReadVersionInfo version = new ReadVersionInfo();
        String[] modules = {"dliver-desktop", 
                            "dliver-driver", 
                            "rtcharts-swing",
                            "rtsync-core",
                            "rtsync-ui"};
        imu_data_reset();

        //icgHeader[icgIdxIcgAbsAc] = "IMP (Ac)";
        //icgHeader[icgIdxIcgAbsDer] = "ICG (IMP Der)";
        //icgHeader[icgIdxIcgAbsDerPc] = "ICG (IMP Der)Pc";
        //icgLog = new FileLogCombiner(belt, icgHeader, icgData);
        //icgLog.createLogInFolder("d-LIVER_icg", sFolder);

        //ppgHeader[ppgIdxPpgRaw] = "PPG (Raw)";
        //ppgHeader[ppgIdxPpgDer] = "PPG (Der)";
        //ppgHeader[ppgIdxPpgDerPc] = "PPG (Der)Pc";
        //ppgLog = new FileLogCombiner(belt, ppgHeader, ppgData);
        //ppgLog.createLogInFolder("d-LIVER_ppg", sFolder);

        comHeader[comIdxPpgRaw] = "PPG (Raw)";
        comHeader[comIdxPpgDer] = "PPG (Der)";
        comHeader[comIdxPpgDerPc] = "PPG (Der)Pc";
        comHeader[comIdxIcgAbsAc] = "IMP (Ac)";
        comHeader[comIdxIcgAbsDer] = "ICG (IMP Der)";
        comHeader[comIdxIcgAbsDerPc] = "ICG (IMP Der)Pc";
        comHeader[comIdxEcg] = "ECG";
        comHeader[comIdxHr] = "HR";
        comHeader[comIdxHri] = "HRi";
        comHeader[comIdxPtt] = "PTT";
        comHeader[comIdxAct] = "Activity";
        comHeader[comIdxPos] = "Posture";
        comHeader[comIdxEvEcgR] = "EvEcgR";
        comHeader[comIdxEvIcgC] = "EvIcgC";
        comHeader[comIdxEvIcgB] = "EvIcgB";
        comHeader[comIdxEvPpgMin] = "EvPpgMin";
        comHeader[comIdxEvPpgFoot] = "EvPpgFoot";
        comHeader[comIdxEvPpgTangent] = "EvPpgTangent";
        comHeader[comIdxEvDppgMax] = "EvDppgMax";
        comHeader[comIdxEvPttActive] = "EvPttActive";
        comHeader[comIdxEvPttResultErr] = "EvPttResultErr";
        comLog = new MathLabLogCombiner(belt, comHeader, comData);
        //comLog.createLogInFolder("d-LIVER_com", sFolder);
        comLog.openLogToUdp(IPAddress, IPPort);

        //measHeader[measIdxPos] = "Posture";
        //measHeader[measIdxAct] = "Activity";
        //measHeader[measIdxStepCount] = "StepCount";
        //measHeader[measIdxHr] = "Heart Rate (BPM)";
        //measHeader[measIdxSkinTemp] = "Temperature (°C)";
        //measLog = new FileLogCombiner(belt, measHeader, measData);
        //measLog.createLogInFolder("d-LIVER_meas", sFolder);
           
        heartrate = 0;
    }
    
    public void startLogging() {
       //String sName = createSessionName(); 
       //File sFolder = new File(folder, sName);
       
       // To avoid overwriting an exiting folder (in case several logs are created at the same time)
       //int i=1;
       //while (sFolder.exists()) {
       //    sFolder = new File(folder, sName + "-" + i);
       //    i++;
       //}
       
       //sFolder.mkdir();
       startLoggingToUdp();
       startTimer = new Timer(2000, this);
       startTimer.setRepeats(false);
       startTimer.start();
       System.out.println("DliverMathLabLogger.class - Starting timer");
       
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
       System.out.println("DliverMathLabLogger.class - Starting logger");
       logging = true;
       //icgLog.startLogging();
       //measLog.startLogging();
       //ppgLog.startLogging();
       comLog.startLogging();
    }
    
    
    public void stopLogging() {
        //if (logging || request_start) {
        if (logging) {
            logging = false;
            //request_start = false;
            //logRt.close();
            //ecgRt.close();
            //imuRt.close();
            //phiRt.close();
            //hrvRt.close();
            //pttRt.close();
            //measRt.close();
            //eventRt.close();
            //logPb.close();
            //ecgPb.close();
            //imuPb.close();
            //phiPb.close();
            //hrvPb.close();
            //pttPb.close();
            //measPb.close();
            //eventPb.close();
            //logRt = null;
            //ecgRt = null;
            //imuRt = null;
            //phiRt = null;
            //hrvRt = null;
            //pttRt = null;
            //measRt = null;
            //eventRt = null;
            //logPb = null;
            //ecgPb = null;
            //imuPb = null;
            //phiPb = null;
            //hrvPb = null;
            //pttPb = null;
            //measPb = null;
            //eventPb = null;
            //log = null;
            //ecg = null;
            //imu = null;
            //phi = null;
            //hrv = null;
            //ptt = null;
            //meas = null;
            //event = null;
            
            //icgLog.stopLogging();
            //measLog.stopLogging();
            //ppgLog.stopLogging();
            comLog.stopLogging();
            //icgLog = null;
            //measLog = null;
            //ppgLog = null;
            comLog = null;
        }
    }
    
    public String createSessionName() {
        SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return timestampFormat.format( Calendar.getInstance().getTime());
    }
    
    public String currentTimeStampEpoch() {
        //return timestampFormat.format( Calendar.getInstance().getTime());
        //return timestampFormat.format( Calendar.getInstance().getTime()) + SEPARATOR + (System.currentTimeMillis()-startTime);
        return "" + System.currentTimeMillis();
    }
    
    public String currentTimeStampHms() {
        //return timestampFormat.format( Calendar.getInstance().getTime());
        //return timestampFormat.format( Calendar.getInstance().getTime()) + SEPARATOR + (System.currentTimeMillis()-startTime);
        return "" + timestampFormat.format(System.currentTimeMillis());
    }
    
    public String calculatedAndRawTimeStamp(int belt_timestamp) {
        //return timestampFormat.format( Calendar.getInstance().getTime());
        //return (t+refTime-cbStartTime)*4;
        //long delta = System.currentTimeMillis() - belt.getEpochTimestamp(belt_timestamp);
        //if ((delta > 2000) || (delta < -2000)) System.out.println("Large delta detected: " + delta);
        long epochTimestamp = belt.getEpochTimestamp(belt_timestamp);
        return "" + timestampFormat.format(epochTimestamp) + SEPARATOR + epochTimestamp + SEPARATOR + belt_timestamp;
    }

    @Override
    public void cUSerialNumber(long value) {
        //if (logging) log.println("[SerialNumber]" + SEPARATOR + currentTimeStampEpoch() + SEPARATOR + currentTimeStampHms() + SEPARATOR + value);
    }

    @Override
    public void cUFWRevision(String value) {
        //if (logging) log.println("[FWRevision]" + SEPARATOR + currentTimeStampEpoch() + SEPARATOR + currentTimeStampHms() + SEPARATOR + value);
    }

    @Override
    public void batteryStatus(int value) {
        //if (logging) log.println("[Battery]" + SEPARATOR + currentTimeStampEpoch() + SEPARATOR + currentTimeStampHms() + SEPARATOR + value);
    }

    @Override
    public void indicationDev(int value) {
        //if (logging) log.println("[Indication]" + SEPARATOR + currentTimeStampEpoch() + SEPARATOR + currentTimeStampHms() + SEPARATOR + value);
    }

    //private int posture = 0;
    //private int activity = 0;
    @Override
    public void measurementPatient(int value, int timestamp) {
        if (logging) {
            if (value >= 1 && value <= 6) { // This is orientation
                //posture = value;
                comLog.newLogEntryTs(timestamp);
                comLog.data[comIdxPos] = "" + value;
                
                //measLog.newLogEntryTs(timestamp);
                //measLog.data[measIdxPos] = "" + value;
            }
            else if (value >=10 && value <=13) { // This is activity
                //activity = value;  
                comLog.newLogEntryTs(timestamp);
                comLog.data[comIdxAct] = "" + value;
                
                //measLog.newLogEntryTs(timestamp);
                //measLog.data[measIdxAct] = "" + value;
            }
        }
        
    }

    //private long stepCount = 0;
    @Override
    public void stepCount(long step, int timestamp) {
        //stepCount = stepCount;
        if (logging) {
            //measLog.newLogEntryTs(timestamp);
            //measLog.data[measIdxStepCount] = "" + step;
        }
    }

    
    @Override
    public void messageOverrun(int value) {
        //if (logging) log.println("[MsgOverrun]" + SEPARATOR + currentTimeStampEpoch() + SEPARATOR + currentTimeStampHms() + SEPARATOR + value);
    }
    

    @Override
    public void referenceClockTime(long value, boolean seconds) {
        //if (logging) log.println("[RefClock]" + SEPARATOR + currentTimeStampEpoch() + SEPARATOR + currentTimeStampHms() + SEPARATOR + value);
    }

    @Override
    public void fullClockTimeSync(long value, boolean seconds) {
        //if (logging) log.println("[FullClock]" + SEPARATOR + currentTimeStampEpoch() + SEPARATOR + currentTimeStampHms() + SEPARATOR + value);
    }

    private int heartrate = 0;
    @Override
    public void heartRate(int valueHR, int timestamp) {
        heartrate = valueHR;
        double hr = heartrate/10.0;
        
        if (logging) {
            comLog.newLogEntryTs(timestamp);
            comLog.data[comIdxHr] = "" + valueHR;

            //measLog.newLogEntryTs(timestamp);
            //measLog.data[measIdxHr] = "" + numFormat.format(hr);
        }
        
    }

    @Override
    public void heartRateInterval(int valueHri, int timestamp) {
        if (logging) {
            //hrv.println(currentTimeStampEpoch() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + valueHri);
            comLog.newLogEntryTs(timestamp);
            comLog.data[comIdxHri] = "" + valueHri;
        }
    }

    private int ecg_timestamp = 0;
    @Override
    public void eCGData(int value) {
        ecg_timestamp += 1;
        if (logging) {
            if(!eCGEpoch) {
                // This can be used to log without timestamp for each sample to keep the file smaller.
                //ecg.println(value);
            } else {
                // This can be used to log the timestamp for each sample but it makes the file really big.
                //long ts = belt.getEpochTimestampFromMs(ecg_timestamp);
                //ecg.println(currentTimeStampEpoch() + SEPARATOR + timestampFormat.format(ts) + SEPARATOR + ts + SEPARATOR + ecg_timestamp + SEPARATOR + 0 + SEPARATOR + value);
            }
            comLog.newLogEntryTs(ecg_timestamp);
            comLog.data[comIdxEcg] = "" + value;
        }

    }

    @Override
    public void eCGSignalQuality(int value, int timestamp) {
        //if (logging) log.println("[ECGSignalQuality]" + SEPARATOR + currentTimeStampEpoch() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + value);
    }

    long oldTs = 0;
    @Override
    public void eCGRaw(int value, int timestamp) {
        ecg_timestamp = timestamp;
        if (logging) {
            if(!eCGEpoch) {
                // This can be used to log without timestamp for each sample to keep the file smaller.
                //ecg.println(value);
            } else {
                // This can be used to log the timestamp for each sample but it makes the file really big.
                long ts = belt.getEpochTimestampFromMs(ecg_timestamp);
                
                if ((ts-oldTs) != 1) System.out.println("ECG epoch timestamp diff is " + (ts-oldTs));
                oldTs = ts;
                
                //ecg.println(currentTimeStampEpoch() + SEPARATOR + timestampFormat.format(ts) + SEPARATOR + ts + SEPARATOR + ecg_timestamp + SEPARATOR + 1 + SEPARATOR + value);
            }
            comLog.newLogEntryTs(ecg_timestamp);
            comLog.data[comIdxEcg] = "" + value;
        }
    }
    
    
    int ax, ay, az, gx, gy, gz;
  
    private void imu_data_reset() {
        //System.out.println("reset");
        ax = Integer.MIN_VALUE;
        ay = Integer.MIN_VALUE;
        az = Integer.MIN_VALUE;
        gx = Integer.MIN_VALUE;
        gy = Integer.MIN_VALUE;
        gz = Integer.MIN_VALUE;
    }
    
    private boolean imu_data_ready() {
        return ax != Integer.MIN_VALUE && ay != Integer.MIN_VALUE && az != Integer.MIN_VALUE && 
               gx != Integer.MIN_VALUE && gy != Integer.MIN_VALUE && gz != Integer.MIN_VALUE;
    }
    
    @Override
    public void gyroPitch(int value, int timestamp) {
        if (logging) {
             //System.out.println("gy");
            if (gy == Integer.MIN_VALUE) {
                gy = value;
                if (imu_data_ready()) {
                    //imu.println(currentTimeStampEpoch() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + A(ax) + SEPARATOR + A(ay) + SEPARATOR + A(az) + SEPARATOR + G(gx) + SEPARATOR + G(gy) + SEPARATOR + G(gz));
                    imu_data_reset();
                }
            }
            else {
                imu_data_reset();
                gy = value;
            }
        }
    }

    @Override
    public void gyroRoll(int value, int timestamp) {
        if (logging) {
            //System.out.println("gx");
            if (gx == Integer.MIN_VALUE) {
                gx = value;
                if (imu_data_ready()) {
                    //imu.println(currentTimeStampEpoch() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + A(ax) + SEPARATOR + A(ay) + SEPARATOR + A(az) + SEPARATOR + G(gx) + SEPARATOR + G(gy) + SEPARATOR + G(gz));
                    imu_data_reset();
                }
            }
            else {
                imu_data_reset();
                gx = value;
            }
        }
    }

    @Override
    public void gyroYaw(int value, int timestamp) {
        if (logging) {
             //System.out.println("gz");
            if (gz == Integer.MIN_VALUE) {
                gz = value;
                if (imu_data_ready()) {
                    //imu.println(currentTimeStampEpoch() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + A(ax) + SEPARATOR + A(ay) + SEPARATOR + A(az) + SEPARATOR + G(gx) + SEPARATOR + G(gy) + SEPARATOR + G(gz));
                    imu_data_reset();
                }
            }
            else {
                imu_data_reset();
                gz = value;
            }
        }
    }

    @Override
    public void accLateral(int value, int timestamp) {
        if (logging) {
             //System.out.println("ay");
            if (ay == Integer.MIN_VALUE) {
                ay = value;
                if (imu_data_ready()) {
                    //imu.println(currentTimeStampEpoch() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + A(ax) + SEPARATOR + A(ay) + SEPARATOR + A(az) + SEPARATOR + G(gx) + SEPARATOR + G(gy) + SEPARATOR + G(gz));
                    imu_data_reset();
                }
            }
            else {
                imu_data_reset();
                ay = value;
            }
        }
    }

    @Override
    public void accLongitudinal(int value, int timestamp) {
        if (logging) {
             //System.out.println("az");
            if (az == Integer.MIN_VALUE) {
                az = value;
                if (imu_data_ready()) {
                    //imu.println(currentTimeStampEpoch() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + A(ax) + SEPARATOR + A(ay) + SEPARATOR + A(az) + SEPARATOR + G(gx) + SEPARATOR + G(gy) + SEPARATOR + G(gz));
                    imu_data_reset();
                }
            }
            else {
                imu_data_reset();
                az = value;
            }
        }
    }

    @Override
    public void accVertical(int value, int timestamp) {
        if (logging) {
            //System.out.println("ax");
            if (ax == Integer.MIN_VALUE) {
                ax = value;
                if (imu_data_ready()) {
                    //imu.println(currentTimeStampEpoch() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + A(ax) + SEPARATOR + A(ay) + SEPARATOR + A(az) + SEPARATOR + G(gx) + SEPARATOR + G(gy) + SEPARATOR + G(gz));
                    imu_data_reset();
                }
            }
            else {
                imu_data_reset();
                ax = value;
            }
        }
    }

    @Override
    public void rawActivityLevel(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private DecimalFormat imuFormat = new DecimalFormat("0.00000");
    
    protected String A(int v) {
        return imuFormat.format(v * 0.0039); // Changed from 0.004
    }
    protected String G(int v) {
        return imuFormat.format(v * 0.07); // Changed from 0.069565
    }
    

    @Override
    public void combinedIMU(int ax, int ay, int az, int gx, int gy, int gz, int timestamp) {
        if (logging) {
            //imu.println(currentTimeStampEpoch() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + A(ax) + SEPARATOR + A(ay) + SEPARATOR + A(az) + SEPARATOR + G(gx) + SEPARATOR + G(gy) + SEPARATOR + G(gz));
        }
    }

    private DecimalFormat numFormat = new DecimalFormat("##.0");
    //private int temperature = 0;
    @Override
    public void skinTemperature(int value, int timestamp) {
        int temperature = value;
        double temp = temperature/10.0;
        double hr = heartrate/10.0;
        //System.out.println(currentTimeStampEpoch() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + numFormat.format(hr) + SEPARATOR + numFormat.format(temp));
        if (logging) {
            //phi.println(currentTimeStampEpoch() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + numFormat.format(hr) + SEPARATOR + numFormat.format(temp));
            //meas.println(currentTimeStampEpoch() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + posture + SEPARATOR + activity + SEPARATOR + stepCount + SEPARATOR + numFormat.format(hr) + SEPARATOR + numFormat.format(temp));
            //measLog.newLogEntryTs(timestamp);
            //measLog.data[measIdxSkinTemp] = "" + numFormat.format(temp);
        }
    }

    
    
    @Override
    public void connectionLost() {
       
    }

    @Override
    public void referenceClockTimeSync(int timeSyncSeqNum, long value) {
        
    }

    @Override
    public void quaternion(int w, int x, int y, int z, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void magnetometer(int x, int y, int z, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ptt(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (logging) {
            //ptt.println(currentTimeStampEpoch() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + value);
            comLog.newLogEntryTs(timestamp);
            comLog.data[comIdxPtt] = "" + value;
        }
    }

    @Override
    public void iCGAbs(int icgAbs, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //if (logging) log.println("[ICG Abs]" + SEPARATOR + currentTimeStampEpoch() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + icgAbs);
    }

    private SimpleFirDifferentiator icgAbsDerPc = new SimpleFirDifferentiator();
    @Override
    public void iCGAbsAc(int icgAbsAc, int timestamp) {

        icgAbsDerPc.addSample(icgAbsAc);
        if (logging) {
            //icgLog.newLogEntryTs(timestamp);
            //icgLog.data[icgIdxIcgAbsAc] = "" + icgAbsAc;
            //icgLog.data[icgIdxIcgAbsDerPc] = "" + icgAbsDerPc.getDeriv();

            comLog.newLogEntryTs(timestamp);
            comLog.data[comIdxIcgAbsAc] = "" + icgAbsAc;
            comLog.data[comIdxIcgAbsDerPc] = "" + icgAbsDerPc.getDeriv();
            
        }
    }

    @Override
    public void iCGDer(int icgAbsDer, int timestamp) {
        if (logging) {
            //icgLog.newLogEntryTs(timestamp);
            //icgLog.data[icgIdxIcgAbsDer] = "" + icgAbsDer;

            comLog.newLogEntryTs(timestamp);
            comLog.data[comIdxIcgAbsDer] = "" + icgAbsDer;
        }
    }

    private SimpleFirDifferentiator ppgDerPc = new SimpleFirDifferentiator();
    @Override
    public void ppgRaw(int ppgRaw, int timestamp) {
        ppgDerPc.addSample(ppgRaw);
        
        if (logging) {
            //ppgLog.newLogEntryTs(timestamp);
            //ppgLog.data[ppgIdxPpgRaw] = "" + ppgRaw;
            //ppgLog.data[ppgIdxPpgDerPc] = "" + ppgDerPc.getDeriv() * 16;

            comLog.newLogEntryTs(timestamp);
            comLog.data[comIdxPpgRaw] = "" + ppgRaw;
            comLog.data[comIdxPpgDerPc] = "" + ppgDerPc.getDeriv() * 16;
        }
    }

    @Override
    public void ppgDer(int ppgDer, int timestamp) {
        if (logging) {
            //ppgLog.newLogEntryTs(timestamp);
            //ppgLog.data[ppgIdxPpgDer] = "" + ppgDer;

            comLog.newLogEntryTs(timestamp);
            comLog.data[comIdxPpgDer] = "" + ppgDer;
        }
    }

    @Override
    public void btPutChar(int value) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eventEpoch(int eventNum, int val, long epoch) {
        if (logging) {
            //event.println(currentTimeStampEpoch() + SEPARATOR + timestampFormat.format(epoch) + SEPARATOR + epoch + SEPARATOR + eventNum + SEPARATOR + val);

            switch ( eventNum ) {
                case 63:
                    comLog.newLogEntryEpoch(epoch);
                    comLog.data[comIdxEvEcgR] = "" + val;
                    break;
                case 62:
                    comLog.newLogEntryEpoch(epoch);
                    comLog.data[comIdxEvIcgC] = "" + val;
                    break;
                case 61:
                    comLog.newLogEntryEpoch(epoch);
                    comLog.data[comIdxEvPpgMin] = "" + val;
                    break;
                case 60:
                    comLog.newLogEntryEpoch(epoch);
                    comLog.data[comIdxEvPpgFoot] = "" + val;
                    break;
                case 59:
                    comLog.newLogEntryEpoch(epoch);
                    comLog.data[comIdxEvPpgTangent] = "" + val;
                    break;
                case 58:
                    comLog.newLogEntryEpoch(epoch);
                    comLog.data[comIdxEvDppgMax] = "" + val;
                    break;
                case 57:
                    comLog.newLogEntryEpoch(epoch);
                    comLog.data[comIdxEvPttActive] = "" + val;
                    break;
                case 56:
                    comLog.newLogEntryEpoch(epoch);
                    comLog.data[comIdxEvIcgB] = "" + val;
                    break;
                case 55:
                    comLog.newLogEntryEpoch(epoch);
                    comLog.data[comIdxEvPttResultErr] = "" + val;
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void playStart(long epoch) {
        //if (logging) logRt.println("[PlayStart]" + SEPARATOR + currentTimeStampEpoch() + SEPARATOR + timestampFormat.format(epoch) + SEPARATOR + epoch);
        //if (logging) logPb.println("[PlayStart]" + SEPARATOR + currentTimeStampEpoch() + SEPARATOR + timestampFormat.format(epoch) + SEPARATOR + epoch);
        //icgLog.playStart(); // Flush old data before changing log fileset
        //measLog.playStart(); // Flush old data before changing log fileset
        //ppgLog.playStart(); // Flush old data before changing log fileset
        comLog.playStart(); // Flush old data before changing log fileset

        //log = logPb;
        //ecg = ecgPb;
        //imu = imuPb;
        //phi = phiPb;
        //hrv = hrvPb;
        //ptt = pttPb;
        //meas = measPb;
        //event = eventPb;
    }

    @Override
    public void playStop() {
        //if (logging) logRt.println("[PlayStop]" + SEPARATOR + currentTimeStampEpoch());
        //if (logging) logPb.println("[PlayStop]" + SEPARATOR + currentTimeStampEpoch());
        //icgLog.playStop(); // Flush old data before changing log fileset
        //measLog.playStop(); // Flush old data before changing log fileset
        //ppgLog.playStop(); // Flush old data before changing log fileset
        comLog.playStop(); // Flush old data before changing log fileset

        //log = logRt;
        //ecg = ecgRt;
        //imu = imuRt;
        //phi = phiRt;
        //hrv = hrvRt;
        //ptt = pttRt;
        //meas = measRt;
        //event = eventRt;
    }

    
    
}
