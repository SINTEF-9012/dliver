/**
 * Copyright (C) 2012 SINTEF <steffen.dalgard@sintef.no>
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

import org.thingml.dliver.driver.Dliver;
import org.thingml.dliver.driver.DliverListener;

/**
 *
 * @author steffend
 */
public class DliverUDPLogger  implements DliverListener {

    private Dliver belt;
    private String probeName;
    private UDPOscComm vOscEcg;
//    private UDPOscComm vOscEcgTsDiff;
    private UDPOscComm vOscIcgAc;
    private UDPOscComm vOscIcgDer;
    private UDPOscComm vOscPpgRaw;
    private UDPOscComm vOscPpgDer;
    private UDPOscComm vOscAccX;
    private UDPOscComm vOscAccY;
    private UDPOscComm vOscAccZ;
    private UDPOscComm vOscGyrX;
    private UDPOscComm vOscGyrY;
    private UDPOscComm vOscGyrZ;
    private UDPOscComm vOscEcgR;
    private UDPOscComm vOscDppgMax;
    private UDPOscComm vOscPpgTangent;
    private UDPOscComm vOscPpgFoot;
    private UDPOscComm vOscPpgMin;
    private UDPOscComm vOscIcgC;
    private UDPOscComm vOscIcgB;
    private UDPOscComm vOscPttActive;
    private UDPOscComm vOscPttErr;
    private boolean logging = false;

    
    public DliverUDPLogger(String probeName, Dliver belt) {
        this.belt = belt;
        this.probeName = probeName;
        this.logging = false;
    }
    
    public void startLogging() {
           
           vOscEcg = new UDPOscComm();
           vOscEcg.open_communication("127.0.0.1", this.probeName + ".Ecg");
//           vOscEcgTsDiff = new UDPOscComm();
//           vOscEcgTsDiff.open_communication("127.0.0.1", this.probeName + ".EcgTsDiff");
           vOscIcgAc = new UDPOscComm();
           vOscIcgAc.open_communication("127.0.0.1", this.probeName + ".IcgAc");
           vOscIcgDer = new UDPOscComm();
           vOscIcgDer.open_communication("127.0.0.1", this.probeName + ".IcgDer(PC)");
           vOscPpgRaw = new UDPOscComm();
           vOscPpgRaw.open_communication("127.0.0.1", this.probeName + ".PpgRaw");
           vOscPpgDer = new UDPOscComm();
           vOscPpgDer.open_communication("127.0.0.1", this.probeName + ".PpgDer(PC)");
           vOscAccX = new UDPOscComm();
           vOscAccX.open_communication("127.0.0.1", this.probeName + ".AccX");
           vOscAccY = new UDPOscComm();
           vOscAccY.open_communication("127.0.0.1", this.probeName + ".AccY");
           vOscAccZ = new UDPOscComm();
           vOscAccZ.open_communication("127.0.0.1", this.probeName + ".AccZ");
           vOscGyrX = new UDPOscComm();
           vOscGyrX.open_communication("127.0.0.1", this.probeName + ".GyrX");
           vOscGyrY = new UDPOscComm();
           vOscGyrY.open_communication("127.0.0.1", this.probeName + ".GyrY");
           vOscGyrZ = new UDPOscComm();
           vOscGyrZ.open_communication("127.0.0.1", this.probeName + ".GyrZ");
           vOscEcgR = new UDPOscComm();
           vOscEcgR.open_communication("127.0.0.1", this.probeName + ".EcgR");
           vOscDppgMax = new UDPOscComm();
           vOscDppgMax.open_communication("127.0.0.1", this.probeName + ".DppgMax");
           vOscPpgTangent = new UDPOscComm();
           vOscPpgTangent.open_communication("127.0.0.1", this.probeName + ".PpgTan");
           vOscPpgFoot = new UDPOscComm();
           vOscPpgFoot.open_communication("127.0.0.1", this.probeName + ".PpgFoot");
           vOscPpgMin = new UDPOscComm();
           vOscPpgMin.open_communication("127.0.0.1", this.probeName + ".PpgMin");
           vOscIcgC = new UDPOscComm();
           vOscIcgC.open_communication("127.0.0.1", this.probeName + ".IcgC");
           vOscIcgB = new UDPOscComm();
           vOscIcgB.open_communication("127.0.0.1", this.probeName + ".IcgB");
           vOscPttActive = new UDPOscComm();
           vOscPttActive.open_communication("127.0.0.1", this.probeName + ".PttAct");
           vOscPttErr = new UDPOscComm();
           vOscPttErr.open_communication("127.0.0.1", this.probeName + ".PttErr");
           logging = true;
    }
    
    public void stopLogging() {
        if (logging ) {
            logging = false;
            vOscEcg.close_communication();
            vOscEcg = null;
//            vOscEcgTsDiff.close_communication();
//            vOscEcg = vOscEcgTsDiff;
            vOscIcgAc.close_communication();
            vOscIcgAc = null;
            vOscIcgDer.close_communication();
            vOscIcgDer = null;
            vOscPpgRaw.close_communication();
            vOscPpgRaw = null;
            vOscPpgDer.close_communication();
            vOscPpgDer = null;
            vOscAccX.close_communication();
            vOscAccX = null;
            vOscAccY.close_communication();
            vOscAccY = null;
            vOscAccZ.close_communication();
            vOscAccZ = null;
            vOscGyrX.close_communication();
            vOscGyrX = null;
            vOscGyrY.close_communication();
            vOscGyrY = null;
            vOscGyrZ.close_communication();
            vOscGyrZ = null;
            vOscEcgR.close_communication();
            vOscEcgR = null;
            vOscDppgMax.close_communication();
            vOscDppgMax = null;
            vOscPpgTangent.close_communication();
            vOscPpgTangent = null;
            vOscPpgFoot.close_communication();
            vOscPpgFoot = null;
            vOscPpgMin.close_communication();
            vOscPpgMin = null;
            vOscIcgC.close_communication();
            vOscIcgC = null;
            vOscIcgB.close_communication();
            vOscIcgB = null;
            vOscPttActive.close_communication();
            vOscPttActive = null;
            vOscPttErr.close_communication();
            vOscPttErr = null;
        }
    }
    
    
    @Override
    public void cUSerialNumber(long value) {
    }

    @Override
    public void cUFWRevision(String value) {
    }

    @Override
    public void batteryStatus(int value) {
    }

    @Override
    public void indicationDev(int value) {
    }

    @Override
    public void measurementPatient(int value, int timestamp) {
    }

    @Override
    public void messageOverrun(int value) {
    }

    @Override
    public void referenceClockTime(long value, boolean seconds) {
    }

    @Override
    public void fullClockTimeSync(long value, boolean seconds) {
    }

    @Override
    public void heartRate(int valueHr, int timestamp) {
    }

    @Override
    public void heartRateInterval(int value, int timestamp) {
    }

    
    private int ecg_timestamp = 0;
    @Override
    public void eCGData(int value) {
        ecg_timestamp += 1;
        if (logging) {
            long ts = belt.getEpochTimestampFromMs(ecg_timestamp);
//            long rxEpoc = System.currentTimeMillis();
            vOscEcg.send_ts_data(ts, value);
//            vOscEcgTsDiff.send_ts_data(ts, rxEpoc-ts);
        }

    }

    @Override
    public void eCGSignalQuality(int value, int timestamp) {
    }

    @Override
    public void eCGRaw(int value, int timestamp) {
        ecg_timestamp = timestamp;
        //System.out.println("ecgRaw" + logging);
        if (logging) {
            long ts = belt.getEpochTimestampFromMs(ecg_timestamp);
//            long rxEpoc = System.currentTimeMillis();
            vOscEcg.send_ts_data(ts, value);
//            vOscEcgTsDiff.send_ts_data(ts, rxEpoc-ts);
        }
    }
    
    @Override
    public void gyroPitch(int value, int timestamp) {
    }

    @Override
    public void gyroRoll(int value, int timestamp) {
    }

    @Override
    public void gyroYaw(int value, int timestamp) {
    }

    @Override
    public void accLateral(int value, int timestamp) {
    }

    @Override
    public void accLongitudinal(int value, int timestamp) {
    }

    @Override
    public void accVertical(int value, int timestamp) {
    }

    @Override
    public void rawActivityLevel(int value, int timestamp) {
    }

    protected float A(float v) {
        return (v * 0.004f);
    }
    protected float G(float v) {
        return (v * 0.069565f);
    }
    
    @Override
    public void combinedIMU(int ax, int ay, int az, int gx, int gy, int gz, int timestamp) {
        if (logging) {
            long ts = belt.getEpochTimestamp(timestamp);
            vOscAccX.send_ts_data(ts, A(ax));
            vOscAccY.send_ts_data(ts, A(ay));
            vOscAccZ.send_ts_data(ts, A(az));
            vOscGyrX.send_ts_data(ts, G(gx));
            vOscGyrY.send_ts_data(ts, G(gy));
            vOscGyrZ.send_ts_data(ts, G(gz));
        }
    }

    @Override
    public void skinTemperature(int value, int timestamp) {
    }

    @Override
    public void connectionLost() {
        stopLogging();
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
    }

    @Override
    public void iCGAbs(int icgAbs, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private SimpleFirDifferentiator icgAbsDerPc = new SimpleFirDifferentiator();
    @Override
    public void iCGAbsAc(int icgAbsAc, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        icgAbsDerPc.addSample(icgAbsAc);
        if (logging) {
            long ts = belt.getEpochTimestamp(timestamp);
            vOscIcgAc.send_ts_data(ts, icgAbsAc);
            vOscIcgDer.send_ts_data(ts, icgAbsDerPc.getDeriv());
        }
    }

    @Override
    public void iCGDer(int icgAbsDer, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //if (logging) {
        //    long ts = belt.getEpochTimestamp(timestamp);
        //    vOscIcgDer.send_ts_data(ts, icgAbsDer);
        //}
    }

    private SimpleFirDifferentiator ppgDerPc = new SimpleFirDifferentiator();
    @Override
    public void ppgRaw(int ppgRaw, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        ppgDerPc.addSample(ppgRaw);
        if (logging) {
            long ts = belt.getEpochTimestamp(timestamp);
            vOscPpgRaw.send_ts_data(ts, ppgRaw);
            vOscPpgDer.send_ts_data(ts, ppgDerPc.getDeriv() * 16);
        }
    }

    @Override
    public void ppgDer(int ppgDer, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //if (logging) {
        //    long ts = belt.getEpochTimestamp(timestamp);
        //    vOscPpgDer.send_ts_data(ts, ppgDer);
        //}
    }

    @Override
    public void btPutChar(int value) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eventEpoch(int eventNum, int val, long epoch) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (logging) {
            switch ( eventNum ) {
                case 63:
                    vOscEcgR.send_ts_data(epoch, val);
                    vOscEcgR.send_ts_data(epoch+10, 0);
                    vOscEcgR.send_ts_data(epoch+100, 0);
                    break;
                case 62:
                    vOscIcgC.send_ts_data(epoch, val);
                    break;
                case 61:
                    vOscPpgMin.send_ts_data(epoch, val);
                    vOscPpgMin.send_ts_data(epoch+10, 0);
                    vOscPpgMin.send_ts_data(epoch+100, 0);
                    break;
                case 60:
                    vOscPpgFoot.send_ts_data(epoch, val);
                    vOscPpgFoot.send_ts_data(epoch+10, 0);
                    vOscPpgFoot.send_ts_data(epoch+100, 0);
                    break;
                case 59:
                    vOscPpgTangent.send_ts_data(epoch, val);
                    break;
                case 58:
                    vOscDppgMax.send_ts_data(epoch, val);
                    vOscDppgMax.send_ts_data(epoch+10, 0);
                    vOscDppgMax.send_ts_data(epoch+100, 0);
                    break;
                case 57:
                    vOscPttActive.send_ts_data(epoch, val);
                    break;
                case 56:
                    vOscIcgB.send_ts_data(epoch, val);
                    vOscIcgB.send_ts_data(epoch+10, 0);
                    vOscIcgB.send_ts_data(epoch+100, 0);
                    break;
                case 55:
                    vOscPttErr.send_ts_data(epoch, val);
                    vOscPttErr.send_ts_data(epoch+10, 0);
                    vOscPttErr.send_ts_data(epoch+100, 0);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void playStart(long epoch) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void playStop() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void stepCount(long step, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

