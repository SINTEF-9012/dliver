/**
 * Copyright (C) 2012 SINTEF <franck.fleurey@sintef.no>
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
package org.thingml.dliver.driver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import org.thingml.rtsync.core.TimeSynchronizable;
import org.thingml.rtsync.core.TimeSynchronizer;

public class Dliver implements Runnable, TimeSynchronizable {

    protected InputStream in;
    protected OutputStream out;
    
    private Thread rxthread = null;
    private boolean activeTrace = false;
    
    // 12 bits timestamps with a 4ms resolution -> 14bits timestamps in ms -> Max value = 0x3FFF
    private TimeSynchronizer rtsync = new TimeSynchronizer(this, 0x3FFF);
    
    public TimeSynchronizer getTimeSynchronizer() {
        return rtsync;
    }
    
    public long getEpochTimestamp(int belt_timestamp) {
        // Mutiply by 4 to get a 14 bits timestamp in ms
        if (rtsync.isRunning()) return rtsync.getSynchronizedEpochTime(belt_timestamp*4);
        else return 0;
    }
    
    public long getEpochTimestampFromMs(int ms_timestamp) {
        // Mutiply by 4 to get a 14 bits timestamp in ms
        if (rtsync.isRunning()) return rtsync.getSynchronizedEpochTime(ms_timestamp);
        else return 0;
    }

    public Dliver(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
        rxthread = new Thread(this);
        rxthread.start();
        activeTrace = false;
        rtsync.start_timesync(); // Can be omitted if no timesync by default
    }

    public Dliver(InputStream in, OutputStream out, boolean openTrace) {
        this.in = in;
        this.out = out;
        rxthread = new Thread(this);
        rxthread.start();
        this.activeTrace = openTrace;
        //rtsync.start_timesync(); Do not start timesync by default
    }

    public void OpenTrace() {
        this.activeTrace = true;
    }

    private int msg_size(byte code) {
        if (code == 101 || code == 122 || code == 123) {
            return 3;
        } else if (code == 106 || code == 107 || code == 110 || code == 112 || code == 114 || code == 117 || code == 121) {
            return 6;
        } else if (code == 125) {
            return 15;
        }
        else if (code == 102) {
            return 12;
        } else if (code == 120) {
            return 18;
        } else {
            return 5; // default value for other messages
        }
    }
    private long receivedBytes = 0;

    public long getReceivedBytes() {
        return receivedBytes;
    }
    
    public boolean isConnected() {
        if (rxthread != null) return rxthread.isAlive();
        else return false;
    }
    
    public void close() {
        
        try {
            rtsync.stop_timesync();
            terminate = true;
            // Wait up to 3 seconds for the rx thread to die before closing the streams
            rxthread.join(3000); 
            in.close();
            out.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        for (DliverListener l : listeners) {
            l.connectionLost();
        }
    }
    
    private boolean terminate = false;

    public void run() {

        byte[] buffer = new byte[32];
        int len = -1;

        int code = 0;
        int target_length = 0;
        int msg_index = 0;
        byte[] message = new byte[32];

        byte[] last_message = new byte[32];
        int last_target_length = 0;
        
        TraceConsole traceCons = null;

        try {
            while (!terminate && ((len = this.in.read(buffer)) > -1)) {
                receivedBytes += len;
                //System.out.println("len = " + len);

                if (traceCons == null ) {
                    if (activeTrace == true) {
                        traceCons = new TraceConsole(50000,1000);
                        traceCons.setSize(600, 750);
                        traceCons.setVisible(true);
                        traceCons.putString("Open trace console\n");
                    }
                }

                for (int i = 0; i < len; i++) {
                    byte c = buffer[i];
                    // Check if this is the code for the start of a message
                    if (c > 95) {
                        // check if it was something in the buffer
                        if (msg_index != 0) {
                            System.err.println("Dliver: Received incomplete message (code = " + code + " len = " + msg_index + ") Expected len = " + target_length);
                            System.err.print("Dliver: Msg [ ");
                            for (int ix = 0; ix < msg_index; ix++) {
                                System.err.print(message[ix] + " ");
                            }
                            if (traceCons != null) {
                                traceCons.putString(" Too short message ");
                                traceCons.putInt(target_length);
                                traceCons.putInt(msg_index);
                            }
                            System.err.println("]");
                            System.err.println("New message staring has code = " + c);
                        }
                        if (traceCons != null) {
                            traceCons.putChar('\n');
                            traceCons.putInt(c);
                        }

                        code = c;
                        msg_index = 0;
                        target_length = msg_size(c);
                        message[msg_index++] = c;
                    } else {
                        if (msg_index > 0 && msg_index < target_length) {
                            if (traceCons != null) {
                                traceCons.putInt(c);
                            }
                            message[msg_index++] = c;
                            if (msg_index == target_length) {
                                last_message = message;  // For debug
                                last_target_length = target_length; // For debug
                                // We got a complete message: Forward to listeners
                                switch (code) {
                                    case 110:
                                        cUSerialNumber(message);
                                        break;
                                    case 117:
                                        cUFWRevision(message);
                                        break;
                                    case 98:
                                        batteryStatus(message);
                                        break;
                                    case 105:
                                        indication(message);
                                        break;
                                    case 109:
                                        status(message);
                                        break;
                                    case 100:
                                        messageOverrun(message);
                                        break;
                                    case 106:
                                        referenceClockTime(message);
                                        break;
                                    case 107:
                                        fullClockTimeSync(message);
                                        break;
                                    case 104:
                                        heartRate(message);
                                        break;
                                    case 99:
                                        heartRateConfidence(message);
                                        break;
                                    case 101:
                                        eCGData(message);
                                        break;
                                    case 102:
                                        combinedICG(message);
                                        // Not supported by d-LIVER    
                                        //eCGSignalQuality(message);
                                        break;
                                    case 103:
                                        eCGRaw(message);
                                        break;
                                    case 112:
                                        // Not supported by d-LIVER    
                                        //gyroPitch(message);
                                        break;
                                    case 114:
                                        // Not supported by d-LIVER    
                                        //gyroRoll(message);
                                        break;
                                    case 121:
                                        ppg(message);
                                        // Not supported by d-LIVER    
                                        //gyroYaw(message);
                                        break;
                                    case 115:
                                        // Not supported by d-LIVER    
                                        //accLateral(message);
                                        break;
                                    case 119:
                                        // Not supported by d-LIVER    
                                        //accLongitudinal(message);
                                        break;
                                    case 118:
                                        // Not supported by d-LIVER    
                                        //accVertical(message);
                                        break;
                                    case 97:
                                        rawActivityLevel(message);
                                        break;
                                    case 116:
                                        skinTemperature(message);
                                        break;
                                    case 120:
                                        combinedIMU(message);
                                        break;
                                    case 122:
                                        // Not supported by d-LIVER    
                                        //eMGData(message);
                                        break;
                                    case 123:
                                        btPutChar(message);
                                        // Not supported by d-LIVER    
                                        //eMGSignalQuality(message);
                                        break;
                                    case 124:
                                        // Not supported by d-LIVER    
                                        //eMGRaw(message);
                                        break;
                                    case 125:
                                        // Not supported by d-LIVER    
                                        //eMGRMS(message);
                                        break;
                                     
                                    default:
                                        break;
                                        //System.err.println("Dliver: Received unknown message (code = " + code + ").");
                                }
                                // Re-initialize for the next message
                                code = 0;
                                msg_index = 0;
                                target_length = 0;
                            }
                        } else {
                            char ch = (char) c;
                            
                            System.err.println("Dliver: Received Corrupted Data.");
                            System.err.print("Last msg len = " + last_target_length + " data = ");
                            if (traceCons != null) {
                                traceCons.putString(" Additional");
                                traceCons.putInt(c);
                                traceCons.putString(" (" + ch + ")"); 
                            }
                            int idx;
                            for (idx = 0; idx<last_target_length; idx++) {
                                System.err.print("" + last_message[idx] + " ");
                            }
                            System.err.println("Additional received = " + c + " (" + ch + ")"); 
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            System.err.println("Dliver: Receiver thread stopped.");
            if (traceCons != null) {
                traceCons.putString("\nClose trace console\n");
            }
            if (!terminate) {
                for (DliverListener l : listeners) {
                    l.connectionLost();
                }
            }
        }
    }

    long decodeLong(byte d1, byte d2, byte d3) {
        long result = 0;
        result += ((d1 - 32) & 0x3F) << 12;
        result += ((d2 - 32) & 0x3F) << 6;
        result += (d3 - 32) & 0x3F;
        return result;
    }

    synchronized void cUSerialNumber(byte[] message) {
        long value = decodeLong(message[1], message[2], message[3]);
        int timestamp = ((message[4] - 32) * 64 + (message[5] - 32));
        for (DliverListener l : listeners) {
            l.cUSerialNumber(value, timestamp);
        }
    }

    synchronized void cUFWRevision(byte[] message) {
        long value = decodeLong(message[1], message[2], message[3]);
        int timestamp = ((message[4] - 32) * 64 + (message[5] - 32));
        for (DliverListener l : listeners) {
            l.cUFWRevision(""+(message[1]-32) +"."+ (message[2]-32) +"."+ (message[3]-32), timestamp);
        }
    }

    synchronized void batteryStatus(byte[] message) {
        int value = ((message[1] - 32) * 64 + (message[2] - 32));
        int timestamp = ((message[3] - 32) * 64 + (message[4] - 32));
        for (DliverListener l : listeners) {
            l.batteryStatus(value, timestamp);
        }
    }

    synchronized void indication(byte[] message) {
        int value = ((message[1] - 32) * 64 + (message[2] - 32));
        int timestamp = ((message[3] - 32) * 64 + (message[4] - 32));
        for (DliverListener l : listeners) {
            l.indication(value, timestamp);
        }
    }

    synchronized void status(byte[] message) {
        int value = ((message[1] - 32) * 64 + (message[2] - 32));
        int timestamp = ((message[3] - 32) * 64 + (message[4] - 32));
        for (DliverListener l : listeners) {
            l.status(value, timestamp);
        }
    }

    synchronized void messageOverrun(byte[] message) {
        int value = ((message[1] - 32) * 64 + (message[2] - 32));
        int timestamp = ((message[3] - 32) * 64 + (message[4] - 32));
        for (DliverListener l : listeners) {
            l.messageOverrun(value, timestamp);
        }
    }
    
    long decode6byteLong(byte d1, byte d2, byte d3, byte d4, byte d5, byte d6) {
        long result = 0;
        result += ((d1 - 32) & 0x3F) << 30;
        result += ((d2 - 32) & 0x3F) << 24;
        result += ((d3 - 32) & 0x3F) << 18;
        result += ((d4 - 32) & 0x3F) << 12;
        result += ((d5 - 32) & 0x3F) << 6;
        result += (d6 - 32) & 0x3F;
        return result;
    }
    
    long decode4byteLong(byte d2, byte d3, byte d4, byte d5) {
        long result = 0;
//        result += ((d1 - 32) & 0x1F) << 24; // dicard bit 29 which is used for something else
        result += ((d2 - 32) & 0x3F) << 18;
        result += ((d3 - 32) & 0x3F) << 12;
        result += ((d4 - 32) & 0x3F) << 6;
        result += (d5 - 32) & 0x3F;
        return result;
    }

    // Time and clock synchronization messages
    synchronized void referenceClockTime(byte[] message) {
        long value = decode4byteLong(message[2], message[3], message[4], message[5]);
        boolean seconds = ((message[1]-32) & 0x20) == 0;
        for (DliverListener l : listeners) {
              l.referenceClockTime(value, seconds);
        }
    }

    synchronized void fullClockTimeSync(byte[] message) {
        long value = decode4byteLong(message[2], message[3], message[4], message[5]);
        boolean seconds = ((message[1]-32) & 0x20) == 0;
        int timeSyncSeqNum = ((message[1]-32) & 0x1f);
        boolean timeSync = timeSyncSeqNum != 0;
        //System.out.println("Message[1] = " + message[1] + " timeSyncSeqNum = " + timeSyncSeqNum);
        
        if (timeSync) {
            int ts = (int) (value & 0x0FFF); // get the 12 bits timestamp
            ts = ts*4; // Put the timestamp in ms (that makes a 14bits timestamp)
            rtsync.receive_TimeResponse(timeSyncSeqNum-2, ts);
            for (DliverListener l : listeners) {
              l.referenceClockTimeSync(timeSyncSeqNum-2, ts);
            }
        } else {
            for (DliverListener l : listeners) {
              l.fullClockTimeSync(value, seconds);
            }
        }
    }


    // ECG and Heart rate messages
    synchronized void heartRate(byte[] message) {
        int value = ((message[1] - 32) * 64 + (message[2] - 32));
        int timestamp = ((message[3] - 32) * 64 + (message[4] - 32));
        for (DliverListener l : listeners) {
            l.heartRate(value, timestamp);
        }
    }

    synchronized void heartRateConfidence(byte[] message) {
        int value = ((message[1] - 32) * 64 + (message[2] - 32));
        int timestamp = ((message[3] - 32) * 64 + (message[4] - 32));
        for (DliverListener l : listeners) {
            l.heartRateConfidence(value, timestamp);
        }
    }

    synchronized void eCGData(byte[] message) {
        int value = ((message[1] - 32) * 64 + (message[2] - 32));
        for (DliverListener l : listeners) {
            l.eCGData(value);
        }
    }

// Not supported by d-LIVER    
//    synchronized void eCGSignalQuality(byte[] message) {
//        int value = ((message[1] - 32) * 64 + (message[2] - 32));
//        int timestamp = ((message[3] - 32) * 64 + (message[4] - 32));
//        for (DliverListener l : listeners) {
//            l.eCGSignalQuality(value, timestamp);
//        }
//    }

    synchronized void eCGRaw(byte[] message) {
        int value = ((message[1] - 32) * 64 + (message[2] - 32));
        int timestamp = ((message[3] - 32) * 64 + (message[4] - 32));
        for (DliverListener l : listeners) {
            l.eCGRaw(value, timestamp);
        }
    }

    synchronized void ppg(byte[] message) {
        int value = decodeGyro(message[1], message[2], message[3]);
        int timestamp = ((message[4] - 32) * 64 + (message[5] - 32));
        for (DliverListener l : listeners) {
            l.ppg(value, timestamp);
        }
    }

    synchronized void btPutChar(byte[] message) {
        int value = ((message[1] - 32) * 64 + (message[2] - 32));
        for (DliverListener l : listeners) {
            l.btPutChar(value);
        }
    }
    
    // EMG Messages
// Not supported by d-LIVER    
//    synchronized void eMGSignalQuality(byte[] message) {
//        int value = ((message[1] - 32) * 64 + (message[2] - 32));
//        int timestamp = ((message[3] - 32) * 64 + (message[4] - 32));
//        for (DliverListener l : listeners) {
//            l.eMGSignalQuality(value, timestamp);
//        }
//    }

// Not supported by d-LIVER    
//    synchronized void eMGRaw(byte[] message) {
//        int value = ((message[1] - 32) * 64 + (message[2] - 32));
//        int timestamp = ((message[3] - 32) * 64 + (message[4] - 32));
//        for (DliverListener l : listeners) {
//            l.eMGRaw(value, timestamp);
//        }
//    }
    
// Not supported by d-LIVER    
//    synchronized void eMGData(byte[] message) {
//        int value = ((message[1] - 32) * 64 + (message[2] - 32));
//        for (DliverListener l : listeners) {
//            l.eMGData(value);
//        }
//    }
    
// Not supported by d-LIVER    
//    synchronized void eMGRMS(byte[] message) {
//        long channelA = decode6byteLong(message[1], message[2], message[3], message[4], message[5], message[6]);
//        long channelB = decode6byteLong(message[7], message[8], message[9], message[10], message[11], message[12]);
//        int timestamp = ((message[13] - 32) * 64 + (message[14] - 32));
//        for (DliverListener l : listeners) {
//            l.eMGRMS((int)Math.sqrt(channelA), (int)Math.sqrt(channelB), timestamp);
//        }
//    }

    // Gyroscope messages
                                        // Not supported by d-LIVER    
//    synchronized void gyroPitch(byte[] message) {
//        int value = decodeGyro(message[1], message[2], message[3]);
//        int timestamp = ((message[4] - 32) * 64 + (message[5] - 32));
//        for (DliverListener l : listeners) {
//            l.gyroPitch(value, timestamp);
//        }
//    }

// Not supported by d-LIVER    
//    synchronized void gyroRoll(byte[] message) {
//        int value = decodeGyro(message[1], message[2], message[3]);
//        int timestamp = ((message[4] - 32) * 64 + (message[5] - 32));
//        for (DliverListener l : listeners) {
//            l.gyroRoll(value, timestamp);
//        }
//    }

// Not supported by d-LIVER    
//    synchronized void gyroYaw(byte[] message) {
//        int value = decodeGyro(message[1], message[2], message[3]);
//        int timestamp = ((message[4] - 32) * 64 + (message[5] - 32));
//        for (DliverListener l : listeners) {
//            l.gyroYaw(value, timestamp);
//        }
//    }
    // Accelerometer and activity messages
// Not supported by d-LIVER    
//    synchronized void accLateral(byte[] message) {
//        int value = decodeAcc(message[1], message[2]);
//        int timestamp = ((message[3] - 32) * 64 + (message[4] - 32));
//        for (DliverListener l : listeners) {
//            l.accLateral(value, timestamp);
//        }
//    }

// Not supported by d-LIVER    
//    synchronized void accLongitudinal(byte[] message) {
//        int value = decodeAcc(message[1], message[2]);
//        int timestamp = ((message[3] - 32) * 64 + (message[4] - 32));
//        for (DliverListener l : listeners) {
//            l.accLongitudinal(value, timestamp);
//        }
//    }

// Not supported by d-LIVER    
//    synchronized void accVertical(byte[] message) {
//        int value = decodeAcc(message[1], message[2]);
//        int timestamp = ((message[3] - 32) * 64 + (message[4] - 32));
//        for (DliverListener l : listeners) {
//            l.accVertical(value, timestamp);
//        }
//    }

    synchronized void rawActivityLevel(byte[] message) {
        int value = ((message[1] - 32) * 64 + (message[2] - 32));
        int timestamp = ((message[3] - 32) * 64 + (message[4] - 32));
        for (DliverListener l : listeners) {
            l.rawActivityLevel(value, timestamp);
        }
    }

    // IR Temperature sensor messages
    synchronized void skinTemperature(byte[] message) {
        // TODO: This will not handle negative temperatures
        int value = ((message[1] - 32) * 64 + (message[2] - 32));
        int timestamp = ((message[3] - 32) * 64 + (message[4] - 32));
        for (DliverListener l : listeners) {
            l.skinTemperature(value, timestamp);
        }
    }

    synchronized int decodeGyro(byte d1, byte d2, byte d3) {
        int result = 0;
        result += ((d1 - 32) & 0x3F) << 12;
        result += ((d2 - 32) & 0x3F) << 6;
        result += (d3 - 32) & 0x3F;
        if (result > (1 << 17)) {
            result = result - (1 << 18);
        }
        return result;
    }

    synchronized int decodeAcc(byte d1, byte d2) {
        int result = 0;
        result += ((d1 - 32) & 0x3F) << 6;
        result += (d2 - 32) & 0x3F;
        if (result > (1 << 11)) {
            result = result - (1 << 12);
        }
        return result;
    }

    synchronized void combinedICG(byte[] message) {
        int icgAbs = decodeGyro(message[1], message[2], message[3]);
        int icgAbsDer = decodeGyro(message[4], message[5], message[6]);
        int icgAbsAc = decodeGyro(message[7], message[8], message[9]);
        int timestamp = ((message[10] - 32) * 64 + (message[11] - 32));
        for (DliverListener l : listeners) {
            l.combinedICG(icgAbs, icgAbsDer, icgAbsAc, timestamp);
        }
    }
    synchronized void combinedIMU(byte[] message) {
        int ax = decodeAcc(message[1], message[2]);
        int ay = decodeAcc(message[3], message[4]);
        int az = decodeAcc(message[5], message[6]);
        int gx = decodeGyro(message[7], message[8], message[9]);
        int gy = decodeGyro(message[10], message[11], message[12]);
        int gz = decodeGyro(message[13], message[14], message[15]);
        int timestamp = ((message[16] - 32) * 64 + (message[17] - 32));
        for (DliverListener l : listeners) {
            l.combinedIMU(ax, ay, az, gx, gy, gz, timestamp);
        }
    }
    private ArrayList<DliverListener> listeners = new ArrayList<DliverListener>();

    public synchronized void addDliverListener(DliverListener l) {
        listeners.add(l);
    }

    public synchronized void removeDliverListener(DliverListener l) {
        listeners.remove(l);
    }

    public void connectionRestored() {
        sendData(120, 0);
    }

    public void getSerialNumber() {
        sendData(110, 0);
    }

    public void getModelInfo() {
        sendData(114, 0);
    }

    public void setBTUpdateInterval(int value) {
        sendData(122, 32 + value);
    }

    public void requestCUTime(int value) {
        sendData(107, 32 + value);
    }

    public void setBatchDataMode() {
        sendData(98, 0);
    }

    public void setLiveDataMode() {
        sendData(108, 0); /* This is not implemented on CU */
        System.out.println(" Method setLiveDataMode() is not implemented by CU");
    }

    public void setDataMode(DliverMode mode) {
        sendData(109, 32 + mode.getCode());
    }

    public void setHRUpdateInterval(int value) {
        sendData(103, 32 + value);
    }

    public void setHRAverageInterval(int value) {
        sendData(104, 32 + value);
    }

    public void getCUFWRevision(int value) {
        sendData(117, 32 + value);
    }

    public void setTempAverageInterval(int value) {
        sendData(116, 32 + value);
    }

    public void sendAlert(int level) {
        sendData(97, 32 + level);
    }
    
    public void sendBtGetChar(int ch) {
        sendData(100, 32 + ((ch >> 6) & 0x3f));
        sendData(99, 32 + (ch & 0x3f));
    }
    
    public void sendBtConStart() {
        sendBtGetChar(0x0f00);
    }
    
// Not supported by d-LIVER    
//    public void sendRmsWinPramsCh1(int size, int rate) {
//        int param = (size*8) + rate;
//        sendData(118, 32 + param);
//    }
    
// Not supported by d-LIVER    
//    public void sendRmsWinPramsCh2(int size, int rate) {
//        int param = (size*8) + rate;
//        sendData(119, 32 + param);
//    }
    
    protected void sendData(int code, int value) {
        try {
            // send the code
            out.write((int) code);
            // send the value
            out.write((int) value);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void sendTimeRequest(int seq_num) {
        requestCUTime(seq_num+2);
    }
}
