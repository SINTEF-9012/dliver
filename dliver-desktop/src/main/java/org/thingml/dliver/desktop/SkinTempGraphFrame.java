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
 * SkinTempGraphFrame.java
 *
 * Created on 1 juil. 2012, 17:21:18
 */
package org.thingml.dliver.desktop;

import org.thingml.dliver.driver.Dliver;
import org.thingml.dliver.driver.DliverListener;
import org.thingml.rtcharts.swing.*;

/**
 *
 * @author franck
 */
public class SkinTempGraphFrame extends javax.swing.JFrame implements DliverListener {

    protected GraphBuffer btemp = new GraphBuffer(200);
    
    protected Dliver belt;
    
    /** Creates new form SkinTempGraphFrame */
    public SkinTempGraphFrame(Dliver b) {
        this.belt = b;
        if (b != null) b.addDliverListener(this);
        initComponents();
        ((GraphPanel)jPanel1).start();
        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new LineGraphPanel(btemp, "Skin Temperature (x 0.1°C)", 250, 450, 50, new java.awt.Color(0, 102, 255));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Dliver Skin Temperature Graph");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                SkinTempGraphFrame.this.windowClosed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
     if (belt != null) belt.removeDliverListener(this);
    ((GraphPanel)jPanel1).stop();
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
       
    }

    @Override
    public void eCGSignalQuality(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void eCGRaw(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
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
        btemp.insertData(value);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
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
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void pPGSignalQuality(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void pPGRaw(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void iCGData(int value) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void iCGSignalQuality(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void iCGRaw(int value, int timestamp) {
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
    public void pTT(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}