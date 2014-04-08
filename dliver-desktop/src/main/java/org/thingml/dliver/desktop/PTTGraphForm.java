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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ECGGraphForm.java
 *
 * Created on 1 juil. 2012, 16:48:35
 */
package org.thingml.dliver.desktop;

import org.thingml.dliver.driver.Dliver;
import org.thingml.dliver.driver.DliverListener;
import java.awt.Color;
import org.thingml.rtcharts.swing.*;

/**
 *
 * @author franck
 */
public class PTTGraphForm extends javax.swing.JFrame implements DliverListener {

    
    protected GraphBuffer becg = new GraphBuffer(1000);
    protected GraphBuffer bppg = new GraphBuffer(1000);
    protected GraphBuffer bicgAbs = new GraphBuffer(1000);
    protected GraphBuffer bicgAbsDer = new GraphBuffer(1000);
    protected GraphBuffer bicgAbsAc = new GraphBuffer(1000);
    
     protected Dliver belt;
    
    /** Creates new form ECGGraphForm */
    public PTTGraphForm(Dliver b) {
        this.belt = b;
        if (b != null) b.addDliverListener(this);
        initComponents();
        ((GraphPanel)jPanel1).start();
        ((GraphPanel)jPanel2).start();
        ((GraphPanel)jPanel3).start();
        ((GraphPanel)jPanel4).start();
        ((GraphPanel)jPanel5).start();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new LineGraphPanel(becg, "ECG (Raw ADC value)", 0, 4096, 512, new java.awt.Color(255, 153, 0), 1.0, "0", "Avg: ", "Last: ");
        jPanel2 = new LineGraphPanel(bppg, "PPG", -32767, 32767, 8192, new java.awt.Color(255, 153, 0), 1.0, "0", "Avg: ", "Last: ");
        jPanel3 = new LineGraphPanel(bicgAbs, "IMP_ABS (Raw ADC value)", -32767, 32767, 8192, new java.awt.Color(255, 153, 0), 1.0, "0", "Avg: ", "Last: ");
        jPanel4 = new LineGraphPanel(bicgAbsAc, "IMP_ABS (AC value with gain)", -32767, 32767, 8192, new java.awt.Color(255, 153, 0), 1.0, "0", "Avg: ", "Last: ");
        jPanel5 = new LineGraphPanel(bicgAbsDer, "ICG (IMP Derived after gain)", -500, 500, 8192, new java.awt.Color(255, 153, 0), 1.0, "0", "Avg: ", "Last: ");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("d-LIVER Blood Pressure Graphs");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                PTTGraphForm.this.windowClosed(evt);
            }
        });
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.PAGE_AXIS));
        getContentPane().add(jPanel1);
        getContentPane().add(jPanel2);
        getContentPane().add(jPanel3);
        getContentPane().add(jPanel4);
        getContentPane().add(jPanel5);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
    if (belt != null) belt.removeDliverListener(this);
    ((GraphPanel)jPanel1).stop();
    ((GraphPanel)jPanel2).stop();
    ((GraphPanel)jPanel3).stop();
}//GEN-LAST:event_windowClosed

    
    
    @Override
    public void cUSerialNumber(long value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void cUFWRevision(String value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void batteryStatus(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void indication(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void status(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void messageOverrun(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void referenceClockTime(long value, boolean seconds) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fullClockTimeSync(long value, boolean seconds) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void heartRate(int value, int timestamp) {
    }

    @Override
    public void heartRateConfidence(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void eCGData(int value) {
        becg.insertData(value);
    }

    @Override
    public void eCGSignalQuality(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void eCGRaw(int value, int timestamp) {
        becg.insertData(value);
    }

    @Override
    public void gyroPitch(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void gyroRoll(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void gyroYaw(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void accLateral(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void accLongitudinal(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void accVertical(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void rawActivityLevel(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void combinedIMU(int ax, int ay, int az, int gx, int gy, int gz, int timestamp) {

    }

    @Override
    public void skinTemperature(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    // End of variables declaration//GEN-END:variables

    @Override
    public void connectionLost() {
        
    }

    @Override
    public void eMGData(int value) {
        
    }

    @Override
    public void eMGSignalQuality(int value, int timestamp) {
        
    }

    @Override
    public void eMGRaw(int value, int timestamp) {
        
    }

    @Override
    public void eMGRMS(int channelA, int channelB, int timestamp) {
        
    }
    @Override
    public void referenceClockTimeSync(int timeSyncSeqNum, long value) {
        
    }
    @Override
    public void pPGData(int value) {
        bppg.insertData(value);
    }

    @Override
    public void pPGSignalQuality(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void pPGRaw(int value, int timestamp) {
        bppg.insertData(value);
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
    public void pTT(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void combinedICG(int icgAbs, int icgAbsDer, int icgAbsAc, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        bicgAbs.insertData(icgAbs);
        bicgAbsDer.insertData(icgAbsDer);
        bicgAbsAc.insertData(icgAbsAc);
    }

    @Override
    public void ppg(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        bppg.insertData(value);
    }

    @Override
    public void btPutChar(int value) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
