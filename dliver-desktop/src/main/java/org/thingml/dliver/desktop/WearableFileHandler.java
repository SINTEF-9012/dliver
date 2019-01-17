/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thingml.dliver.desktop;
import java.nio.charset.StandardCharsets;
import javax.xml.bind.DatatypeConverter;
import org.thingml.dliver.driver.DliverListener;
import org.thingml.dliver.driver.Dliver;

/**
 *
 * @author nkm
 */
public class WearableFileHandler implements DliverListener {
    
    /* Message Type ''' = 0x60 = 96 */
    protected final byte MESSAGE_TYPE               =  0x60;
    /* File Message Types, FMT */
    protected final byte FMT_REQUEST_FILE_LIST      =  0x01;
    protected final byte FMT_REQUEST_FILE           =  0x02;
    protected final byte FMT_REQUEST_DELETE_FILE    =  0x03;
    protected final byte FMT_REQUEST_DELETE_ALL     =  0x04;
    protected final byte FMT_REQUEST_CREATE_FILE    =  0x07;
    
    protected final byte FMT_RESPONSE_FILENAMES     =  0x21;

    // FMT Data Type, DT
    protected final byte DT_FILENAME                =  0x41;
    protected final byte DT_FILE_SIZE               =  0x42;
    protected final byte DT_DATE                    =  0x43;
    protected final byte DT_TIME                    =  0x44;
    
    
    /* Minimum message size: Header (4B) + CRC (2B) */
    protected final int minMessageSize          = 6;
    protected final int filenameSize            = 26;
    protected final int filesizeSize            = 8;
    protected final int dateSize                = 8;
    protected final int timeSize                = 4;
    
    
    private Dliver belt;
    private WearableFileHandlerFrame frame;
    
    public WearableFileHandler(Dliver b, WearableFileHandlerFrame f) {
        this.belt = b;
        this.frame = f;
        if (b != null) b.addDliverListener(this);
    }

    
    void CommandRequestFileList() {
        byte[] dataArray = new byte[6];
        int    idx = 0;
        
        dataArray[idx++] = MESSAGE_TYPE;
        dataArray[idx++] = 0x00;
        dataArray[idx++] = minMessageSize;
        dataArray[idx++] = FMT_REQUEST_FILE_LIST;
        
        int crc = CalculateCrc(dataArray, idx);
        System.out.println("File idx = " + idx + " CRC = " + Integer.toHexString(crc));
        
        dataArray[idx++] = (byte) ((crc >> 8) & 0xff);
        dataArray[idx++] = (byte) ((crc     ) & 0xff);
        
        for (int i=0; i<idx; i++) {
            System.out.println(i + " : " + Integer.toHexString(dataArray[i]));             
        }
        
        // Send data to wearable
        if (belt != null) {
            belt.sendDataArray(dataArray, idx);
            System.out.println("Sent command");
        }
        
        // Calculate over entire
        crc = CalculateCrc(dataArray, idx);
        System.out.println("File idx1 = " + idx + " CRC1 = " + Integer.toHexString(crc));        
    }

    void CommandRequestFile(String name) {
        byte[] dataArray = new byte[32];
        int    idx = 0;
        int size = minMessageSize + name.length();
        
        dataArray[idx++] = MESSAGE_TYPE;        
        dataArray[idx++] = (byte) ((size >> 8) & 0xff);
        dataArray[idx++] = (byte) ((size     ) & 0xff);
        dataArray[idx++] = FMT_REQUEST_FILE;
        
        for (int i=0; i<name.length(); i++) {
            dataArray[idx++] = (byte) name.charAt(i);
        }

        int crc = CalculateCrc(dataArray, idx);
        System.out.println("File idx = " + idx + " CRC = " + Integer.toHexString(crc));
        
        dataArray[idx++] = (byte) ((crc >> 8) & 0xff);
        dataArray[idx++] = (byte) ((crc     ) & 0xff);
        
        for (int i=0; i<idx; i++) {
            System.out.println(i + " : " + Integer.toHexString(dataArray[i]));             
        }
        
        // Send data to wearable
        if (belt != null) {
            belt.sendDataArray(dataArray, idx);
            System.out.println("Sent command");
        }
        
        // Calculate over entire
        crc = CalculateCrc(dataArray, idx);
        System.out.println("File idx1 = " + idx + " CRC1 = " + Integer.toHexString(crc));
    }
    
    void CommandRequestDeleteFile(String name) {
        byte[] dataArray = new byte[32];
        int    idx = 0;
        int size = minMessageSize + name.length();
        
        dataArray[idx++] = MESSAGE_TYPE;        
        dataArray[idx++] = (byte) ((size >> 8) & 0xff);
        dataArray[idx++] = (byte) ((size     ) & 0xff);
        dataArray[idx++] = FMT_REQUEST_DELETE_FILE;
        
        for (int i=0; i<name.length(); i++) {
            dataArray[idx++] = (byte) name.charAt(i);
        }

        int crc = CalculateCrc(dataArray, idx);
        System.out.println("File idx = " + idx + " CRC = " + Integer.toHexString(crc));
        
        dataArray[idx++] = (byte) ((crc >> 8) & 0xff);
        dataArray[idx++] = (byte) ((crc     ) & 0xff);
        
        for (int i=0; i<idx; i++) {
            System.out.println(i + " : " + Integer.toHexString(dataArray[i]));             
        }
        
        // Send data to wearable
        if (belt != null) {
            belt.sendDataArray(dataArray, idx);
            System.out.println("Sent command");
        }
        
        // Calculate over entire
        crc = CalculateCrc(dataArray, idx);
        System.out.println("File idx1 = " + idx + " CRC1 = " + Integer.toHexString(crc));
    }
    
    void CommandRequestDeleteAll() {
        byte[] dataArray = new byte[6];
        int    idx = 0;
        
        dataArray[idx++] = MESSAGE_TYPE;
        dataArray[idx++] = 0x00;
        dataArray[idx++] = minMessageSize;
        dataArray[idx++] = FMT_REQUEST_DELETE_ALL;
        
        int crc = CalculateCrc(dataArray, idx);
        System.out.println("File idx = " + idx + " CRC = " + Integer.toHexString(crc));
        
        dataArray[idx++] = (byte) ((crc >> 8) & 0xff);
        dataArray[idx++] = (byte) ((crc     ) & 0xff);
        
        for (int i=0; i<idx; i++) {
            System.out.println(i + " : " + Integer.toHexString(dataArray[i]));             
        }
        
        // Send data to wearable
        if (belt != null) {
            belt.sendDataArray(dataArray, idx);
            System.out.println("Sent command");
        }
        
        // Calculate over entire
        crc = CalculateCrc(dataArray, idx);
        System.out.println("File idx1 = " + idx + " CRC1 = " + Integer.toHexString(crc));        
    }

    void CommandCreateFile(String name) {
        byte[] dataArray = new byte[32];
        int    idx = 0;
        int size = minMessageSize + name.length();
        
        dataArray[idx++] = MESSAGE_TYPE;        
        dataArray[idx++] = (byte) ((size >> 8) & 0xff);
        dataArray[idx++] = (byte) ((size     ) & 0xff);
        dataArray[idx++] = FMT_REQUEST_CREATE_FILE;
        
        for (int i=0; i<name.length(); i++) {
            dataArray[idx++] = (byte) name.charAt(i);
        }

        int crc = CalculateCrc(dataArray, idx);
        System.out.println("File idx = " + idx + " CRC = " + Integer.toHexString(crc));
        
        dataArray[idx++] = (byte) ((crc >> 8) & 0xff);
        dataArray[idx++] = (byte) ((crc     ) & 0xff);
        
        for (int i=0; i<idx; i++) {
            System.out.println(i + " : " + Integer.toHexString(dataArray[i]));             
        }
        
        // Send data to wearable
        if (belt != null) {
            belt.sendDataArray(dataArray, idx);
            System.out.println("Sent command");
        }
        
        // Calculate over entire
        crc = CalculateCrc(dataArray, idx);
        System.out.println("File idx1 = " + idx + " CRC1 = " + Integer.toHexString(crc));
    }
    
    void ParseFmtMessage(byte[] message) {
        // Determine message received
        switch(message[2]) {
            case FMT_RESPONSE_FILENAMES:
                FmtReceivedFilenames(message);
                break;
            default:
                System.out.println("Unknown FMT message " + Integer.toHexString(message[3]));
                break;
        }
    }
    
    void FmtReceivedFilenames(byte[] message) {
        System.out.println("FmtReceivedFilenames");
        
        String filename = "";
        String filesize = "";
        String date = "";
        String time = "";
        
        // Search for message components
        int[] idx = new int[4];
        for (int i=0; i<message.length; i++) {
            if (message[i] == DT_FILENAME)
                idx[0] = i;
            else if (message[i] == DT_FILE_SIZE)
                idx[1] = i;
            else if (message[i] == DT_DATE)
                idx[2] = i;
            else if (message[i] == DT_TIME)
                idx[3] = i;
        }
        
        if (idx[0] != 0) {
            idx[0]++;
            filename = new String(message, idx[0], filenameSize, StandardCharsets.UTF_8);
            System.out.println("Filename = " + filename);
        }
        if (idx[1] != 0) {
            idx[1]++;
            filesize = new String(message, idx[1], filesizeSize, StandardCharsets.UTF_8);
            System.out.println("Filesize = " + filesize);
        }
        if (idx[2] != 0) {
            idx[2]++;
            date = new String(message, idx[2], dateSize, StandardCharsets.UTF_8);
            System.out.println("Date " + date);
        }
        if (idx[3] != 0) {
            idx[3]++;
            time = new String(message, idx[3], timeSize, StandardCharsets.UTF_8);
            System.out.println("Time " + time);
        }
        
        frame.UpdateSensorFiles(filename, filesize, date, time);
    }
    
    
    /**
     * CRC16-CCITT:  1 + x + x^5 + x^12 + x^16 
     */
    synchronized int CalculateCrc(byte[] message, int length) { 
        int crc = 0xFFFF;          // initial value
        int polynomial = 0x1021;   // 0001 0000 0010 0001  (0, 5, 12) 
        
        //byte[] testBytes = "123456789".getBytes(); // CRC = 0x29B1
        int idx = 0;
        for (int j=0; j<length; j++) {
            for (int i = 0; i < 8; i++) {
                boolean bit = (((message[j] & 0xFF) >> (7-i) & 1) == 1);
                boolean c15 = ((crc >> 15    & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) crc ^= polynomial;
            }
            idx++;
        }
        System.out.print("Idx = " + idx + " ");

        crc &= 0xffff;
        System.out.println("CRC16-CCITT = " + Integer.toHexString(crc));
        return crc;
    }
    
    /**
     * CRC16-CCITT:  1 + x + x^5 + x^12 + x^16 
     */
/*    int CalculateCrc(byte[] message) { 
        int crc = 0xFFFF;          // initial value
        int polynomial = 0x1021;   // 0001 0000 0010 0001  (0, 5, 12) 
        
        //byte[] testBytes = "123456789".getBytes(); // CRC = 0x29B1
        byte[] testBytes = DatatypeConverter.parseHexBinary("60002c21413830355f32303030303930315f303333375f303030322e6c6f674200015200432921441cb81727");
        int idx = 0;
        //for (byte b : message) {
        for (byte b : testBytes) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b   >> (7-i) & 1) == 1);
                boolean c15 = ((crc >> 15    & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) crc ^= polynomial;
            }
            idx++;
        }
        System.out.print("Idx = " + idx + " ");

        crc &= 0xffff;
        System.out.println("CRC16-CCITT = " + Integer.toHexString(crc));
        return crc;
    }
*/
    
    @Override
    public void fmtReceived(byte[] message, int size) {
        System.out.print("\nFMT Received " + size + "B: ");
        for (byte b : message) {
            System.out.format("%02X ", b);
        }

        int crc = CalculateCrc(message, size);
        if (crc == 0) {
            System.out.println("FMT message received OK");
            ParseFmtMessage(message);
        }
        else {
            System.out.println("ERROR: FMT message received failed");
        }
    }

    @Override
    public void cUSerialNumber(long value) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cUFWRevision(String value) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void batteryStatus(int value) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void indicationDev(int value) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void measurementPatient(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void messageOverrun(int value) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void referenceClockTime(long value, boolean seconds) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void fullClockTimeSync(long value, boolean seconds) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void heartRate(int valueHr, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void heartRateInterval(int valueHri, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eCGData(int value) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eCGSignalQuality(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eCGRaw(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void iCGAbs(int icgAbs, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void iCGAbsAc(int icgAbsAc, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void iCGDer(int icgAbsDer, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ptt(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ppgRaw(int ppgRaw, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ppgDer(int ppgDer, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void btPutChar(int value) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void gyroPitch(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void gyroRoll(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void gyroYaw(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void accLateral(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void accLongitudinal(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void accVertical(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void rawActivityLevel(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void combinedIMU(int ax, int ay, int az, int gx, int gy, int gz, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void stepCount(long step, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public void skinTemperature(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void connectionLost() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void referenceClockTimeSync(int timeSyncSeqNum, long value) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eventEpoch(int eventNum, int val, long epoch) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void playStart(long epoch) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void playStop() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
