/**
 * Copyright (C) 2013 SINTEF <steffen.dalgard@sintef.no>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June
 * 2007; you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.thingml.dliver.desktop;

import org.thingml.dliver.driver.Dliver;
import org.thingml.dliver.driver.DliverMode;
import org.thingml.dliver.driver.DliverListener;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.CommPort;
import java.awt.Dialog;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import org.thingml.rtcharts.swing.*;
import org.thingml.rtsync.ui.TimeSyncDelayErrorFrame;
import org.thingml.rtsync.ui.TimeSyncFrame;
import org.thingml.rtsync.core.TimeSynchronizable;
import org.thingml.rtsync.core.TimeSynchronizer;
import org.thingml.rtsync.ui.TimeSyncPingFrame;
import org.thingml.rtsync.core.TimeSynchronizerPrintLogger;

/**
 *
 * @author ffl
 */
public class DliverMainFrame extends javax.swing.JFrame implements DliverListener, BitRateListemer, TsErrorListener {

    Dliver belt = null;
    //protected GraphBuffer becg = new GraphBuffer(1000);
    BitRateCounter counter = null;
    TsErrorStat tsError = null;

    /**
     * Creates new form MainFrame
     */
    public DliverMainFrame() {
        ReadVersionInfo version = new ReadVersionInfo();
        Properties prop = null;

        initComponents();
        setLocationRelativeTo(null);

        prop = version.GetVersionProperties("dliver-desktop");
        jButtonVersionNew.setText("Ver: " + prop.getProperty("Compiled"));
        setAdvancedMode(false);
        jButtonAdvanced.setSelected(false);
    }

    public void disableConnection() {
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        jButton1.setEnabled(false);
    }

    private SerialPort serialPort = null;

    private boolean activeVerifyTestMode = false;
    private boolean activeTestMode = false;
    DebugConsole VerifyTestMode = null;

    private boolean advancedMode = false;

    private void setAdvancedMode(boolean advanced) {
        advancedMode = advanced;
        jButton11.setVisible(advanced);
        jButton8.setVisible(advanced);
        jTextFieldTsErr.setVisible(advanced);
        jCheckBoxDebugCons.setVisible(advanced);
        jCheckBoxBTtrace.setVisible(advanced);
        jLabel4.setVisible(advanced);
        jComboBoxBTInt.setVisible(advanced);
        jButton2.setVisible(advanced);
        jComboBoxAlertLevel.setVisible(advanced);
        jButtonAlert.setVisible(advanced);
        jTextFieldStepCount.setVisible(advanced);
        jLabel10.setVisible(advanced);
        jButtonRec.setVisible(advanced);

    }

    public Dliver connectDliver() {

        ConnectDialog d = new ConnectDialog(this, true, advancedMode);
        d.setVisible(true);

        if (d.getSerialPort() != null) {
            try {
                serialPort = d.getSerialPort();

                return new Dliver(serialPort.getInputStream(), serialPort.getOutputStream(), jCheckBoxBTtrace.isSelected());
            } catch (IOException ex) {
                Logger.getLogger(DliverMainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public void tryToCloseSerialPort() {
        ExecutorService executor = Executors.newCachedThreadPool();
        Callable<Object> task = new Callable<Object>() {
            @Override
            public Object call() {
                serialPort.close();
                System.out.println("Serial port closed.");
                return null;
            }
        };
        Future<Object> future = executor.submit(task);
        try {
            Object result = future.get(5, TimeUnit.SECONDS);
        } catch (TimeoutException ex) {
            System.out.println("Timeout closing serial port.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            future.cancel(true);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupStatus = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel11 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jProgressBarPTT = new javax.swing.JProgressBar();
        jTextFieldPTTTime = new javax.swing.JTextField();
        jButton12 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jComboBoxAlertLevel = new javax.swing.JComboBox();
        jButtonAlert = new javax.swing.JButton();
        jButtonConsole = new javax.swing.JButton();
        jButtonRec = new javax.swing.JButton();
        jButtonPlay = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldStepCount = new javax.swing.JTextField();
        jButtonRecBtPause = new javax.swing.JButton();
        jButtonEraseRec = new javax.swing.JButton();
        jButtonSensorFiles = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jProgressBarBatt = new javax.swing.JProgressBar();
        jRadioButtonOn = new javax.swing.JRadioButton();
        jRadioButtonOff = new javax.swing.JRadioButton();
        jRadioButtonSBy = new javax.swing.JRadioButton();
        jRadioButtonCh = new javax.swing.JRadioButton();
        jRadioButtonSW = new javax.swing.JRadioButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jComboBoxMode = new javax.swing.JComboBox(org.thingml.dliver.driver.DliverMode.values());
        jLabel4 = new javax.swing.JLabel();
        jComboBoxBTInt = new javax.swing.JComboBox();
        jCheckBoxBTtrace = new javax.swing.JCheckBox();
        jCheckBoxDebugCons = new javax.swing.JCheckBox();
        jButtonBtPaused = new javax.swing.JButton();
        jButtonVersionNew = new javax.swing.JButton();
        jButtonAdvanced = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jTextFieldBitRate = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jTextFieldSID = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextFieldSFW = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTextFieldOver = new javax.swing.JTextField();
        jButton11 = new javax.swing.JButton();
        jTextFieldTsErr = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jTextFieldActTime = new javax.swing.JTextField();
        jLabelActivity = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jTextFieldPostTime = new javax.swing.JTextField();
        jLabelPosture = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jProgressBarHR = new javax.swing.JProgressBar();
        jTextFieldHRTime = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jProgressBarGx = new javax.swing.JProgressBar();
        jProgressBarGy = new javax.swing.JProgressBar();
        jProgressBarGz = new javax.swing.JProgressBar();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldIMUTime = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jProgressBarAx = new javax.swing.JProgressBar();
        jProgressBarAy = new javax.swing.JProgressBar();
        jProgressBarAz = new javax.swing.JProgressBar();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItemUDPConnect = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("HyperVision Test Application");

        jPanel11.setPreferredSize(new java.awt.Dimension(901, 620));

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Blood Pressure Change (PTT)"));

        jProgressBarPTT.setForeground(new java.awt.Color(255, 153, 0));
        jProgressBarPTT.setMaximum(220);
        jProgressBarPTT.setValue(75);
        jProgressBarPTT.setString("");
        jProgressBarPTT.setStringPainted(true);

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/chart-1.png"))); // NOI18N
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jProgressBarPTT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldPTTTime, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton12)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jProgressBarPTT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldPTTTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/file-3.png"))); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jComboBoxAlertLevel.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2" }));
        jComboBoxAlertLevel.setSelectedIndex(2);
        jComboBoxAlertLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxAlertLevelActionPerformed(evt);
            }
        });

        jButtonAlert.setText("Alert");
        jButtonAlert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAlertActionPerformed(evt);
            }
        });

        jButtonConsole.setText("Console");
        jButtonConsole.setMaximumSize(new java.awt.Dimension(57, 33));
        jButtonConsole.setMinimumSize(new java.awt.Dimension(57, 33));
        jButtonConsole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConsoleActionPerformed(evt);
            }
        });

        jButtonRec.setText("Record");
        jButtonRec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRecActionPerformed(evt);
            }
        });

        jButtonPlay.setText("Playback");
        jButtonPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPlayActionPerformed(evt);
            }
        });

        jLabel10.setText("Step count");

        jButtonRecBtPause.setText("Rec+BTpause");
        jButtonRecBtPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRecBtPauseActionPerformed(evt);
            }
        });

        jButtonEraseRec.setText("Erase rec");
        jButtonEraseRec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEraseRecActionPerformed(evt);
            }
        });

        jButtonSensorFiles.setText("Sensor Files...");
        jButtonSensorFiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSensorFilesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonEraseRec, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonRecBtPause, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonRec)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonPlay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldStepCount, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonAlert)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBoxAlertLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonSensorFiles)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonConsole, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton7)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jButton7)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButtonSensorFiles, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                            .addComponent(jButtonConsole, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBoxAlertLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonAlert)
                        .addComponent(jButtonRec)
                        .addComponent(jButtonPlay)
                        .addComponent(jLabel10)
                        .addComponent(jTextFieldStepCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonRecBtPause)
                        .addComponent(jButtonEraseRec)))
                .addGap(47, 47, 47))
        );

        jPanel9.setBackground(new java.awt.Color(204, 204, 204));

        jLabel12.setText("Battery :");

        jProgressBarBatt.setForeground(new java.awt.Color(102, 102, 102));
        jProgressBarBatt.setString("");
        jProgressBarBatt.setStringPainted(true);

        buttonGroupStatus.add(jRadioButtonOn);
        jRadioButtonOn.setText("On Body");
        jRadioButtonOn.setEnabled(false);

        buttonGroupStatus.add(jRadioButtonOff);
        jRadioButtonOff.setText("Off Body");
        jRadioButtonOff.setEnabled(false);

        buttonGroupStatus.add(jRadioButtonSBy);
        jRadioButtonSBy.setText("Standby");
        jRadioButtonSBy.setEnabled(false);

        buttonGroupStatus.add(jRadioButtonCh);
        jRadioButtonCh.setText("Charging");
        jRadioButtonCh.setEnabled(false);
        jRadioButtonCh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonChActionPerformed(evt);
            }
        });

        buttonGroupStatus.add(jRadioButtonSW);
        jRadioButtonSW.setText("SW Update");
        jRadioButtonSW.setEnabled(false);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBarBatt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jRadioButtonOn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButtonOff)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButtonSBy)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButtonCh)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButtonSW)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel12)
                    .addComponent(jProgressBarBatt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButtonOn)
                    .addComponent(jRadioButtonOff)
                    .addComponent(jRadioButtonSBy)
                    .addComponent(jRadioButtonCh)
                    .addComponent(jRadioButtonSW))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(204, 204, 204));

        jLabel3.setText("Mode :");

        jComboBoxMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxModeActionPerformed(evt);
            }
        });

        jLabel4.setText("BT upd int :");

        jComboBoxBTInt.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "3", "5", "10", "15", "30", "60", "120" }));
        jComboBoxBTInt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxBTIntActionPerformed(evt);
            }
        });

        jCheckBoxBTtrace.setText("BT-trace");
        jCheckBoxBTtrace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxBTtraceActionPerformed(evt);
            }
        });

        jCheckBoxDebugCons.setText("Verify Test Mode");
        jCheckBoxDebugCons.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxDebugConsActionPerformed(evt);
            }
        });

        jButtonBtPaused.setBackground(new java.awt.Color(204, 204, 204));
        jButtonBtPaused.setForeground(new java.awt.Color(153, 153, 153));
        jButtonBtPaused.setText("BT paused");
        jButtonBtPaused.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBtPausedActionPerformed(evt);
            }
        });

        jButtonVersionNew.setForeground(new java.awt.Color(102, 102, 102));
        jButtonVersionNew.setText("Version ???");
        jButtonVersionNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVersionNewActionPerformed(evt);
            }
        });

        jButtonAdvanced.setForeground(new java.awt.Color(102, 102, 102));
        jButtonAdvanced.setText("+");
        jButtonAdvanced.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAdvancedActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBoxMode, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jCheckBoxDebugCons)
                .addGap(18, 18, 18)
                .addComponent(jCheckBoxBTtrace)
                .addGap(18, 18, 18)
                .addComponent(jButtonBtPaused)
                .addGap(18, 18, 18)
                .addComponent(jButtonVersionNew, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBoxBTInt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonAdvanced)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBoxMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxBTInt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jCheckBoxBTtrace)
                    .addComponent(jCheckBoxDebugCons)
                    .addComponent(jButtonBtPaused)
                    .addComponent(jButtonVersionNew)
                    .addComponent(jButtonAdvanced))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));
        jPanel5.setPreferredSize(new java.awt.Dimension(869, 47));

        jButton1.setText("Connect...");
        jButton1.setMaximumSize(null);
        jButton1.setMinimumSize(null);
        jButton1.setPreferredSize(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel20.setText("BW :");

        jTextFieldBitRate.setText("0");

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/chart-1.png"))); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel14.setText("Sensor ID :");

        jLabel15.setText("FW revision :");

        jLabel16.setForeground(new java.awt.Color(255, 0, 0));
        jLabel16.setText("Overrun :");

        jButton11.setText("Time...");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jTextFieldTsErr.setText("0");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldSID, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldSFW, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldOver, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldBitRate, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTextFieldTsErr, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel14)
                        .addComponent(jTextFieldSID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextFieldBitRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel20)
                        .addComponent(jLabel15)
                        .addComponent(jTextFieldSFW, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel16)
                        .addComponent(jTextFieldOver, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton11)
                        .addComponent(jTextFieldTsErr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Activity"));

        jLabelActivity.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Activity_1.png"))); // NOI18N

        jLabel9.setText("Timestamp :");

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/chart-1.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldActTime, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2))
                    .addComponent(jLabelActivity, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelActivity, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(jTextFieldActTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Posture"));

        jLabelPosture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/upright.png"))); // NOI18N

        jLabel19.setText("Timestamp :");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldPostTime, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabelPosture))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addComponent(jLabelPosture, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jTextFieldPostTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Heart Rate"));

        jProgressBarHR.setForeground(new java.awt.Color(255, 51, 51));
        jProgressBarHR.setMaximum(2200);
        jProgressBarHR.setValue(750);
        jProgressBarHR.setString("");
        jProgressBarHR.setStringPainted(true);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/chart-1.png"))); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jProgressBarHR, javax.swing.GroupLayout.DEFAULT_SIZE, 772, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldHRTime, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton5)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jProgressBarHR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldHRTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("IMU"));

        jProgressBarGx.setForeground(new java.awt.Color(255, 153, 0));
        jProgressBarGx.setMaximum(2500);
        jProgressBarGx.setMinimum(-2500);

        jProgressBarGy.setForeground(new java.awt.Color(255, 153, 0));
        jProgressBarGy.setMaximum(2500);
        jProgressBarGy.setMinimum(-2500);

        jProgressBarGz.setForeground(new java.awt.Color(255, 153, 0));
        jProgressBarGz.setMaximum(2500);
        jProgressBarGz.setMinimum(-2500);

        jLabel7.setText("Timestamp :");

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/chart-1.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel1.setText("Accel");

        jLabel2.setText("Gyro");

        jProgressBarAx.setForeground(new java.awt.Color(255, 204, 0));
        jProgressBarAx.setMaximum(500);
        jProgressBarAx.setMinimum(-500);

        jProgressBarAy.setForeground(new java.awt.Color(255, 204, 0));
        jProgressBarAy.setMaximum(500);
        jProgressBarAy.setMinimum(-500);

        jProgressBarAz.setForeground(new java.awt.Color(255, 204, 0));
        jProgressBarAz.setMaximum(500);
        jProgressBarAz.setMinimum(-500);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldIMUTime, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 180, Short.MAX_VALUE)
                        .addComponent(jButton3))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(32, 32, 32)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jProgressBarAx, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                            .addComponent(jProgressBarAy, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                            .addComponent(jProgressBarAz, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(35, 35, 35)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jProgressBarGz, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
                            .addComponent(jProgressBarGy, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
                            .addComponent(jProgressBarGx, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addComponent(jProgressBarAx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jProgressBarAy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jProgressBarAz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1))
                .addGap(28, 28, 28)
                .addComponent(jProgressBarGx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jProgressBarGy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBarGz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldIMUTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7))
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel11Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel11Layout.createSequentialGroup()
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 965, Short.MAX_VALUE)
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap()))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(446, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel11Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(250, Short.MAX_VALUE)))
        );

        jPanel2.getAccessibleContext().setAccessibleName("");

        jScrollPane1.setViewportView(jPanel11);

        jMenuBar1.setBorderPainted(false);

        jMenu1.setText("File");

        jMenuItemUDPConnect.setText("UDP Connect");
        jMenuItemUDPConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemUDPConnectActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemUDPConnect);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Help");

        jMenuItem1.setText("About");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 987, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 686, Short.MAX_VALUE)
        );

        setBounds(0, 0, 1003, 746);
    }// </editor-fold>//GEN-END:initComponents

    public void do_connect(Dliver b) {
        belt = b;
        belt.addDliverListener(this);
        //((GraphPanel)jPanelECG).start();
        counter = new BitRateCounter(belt);
        counter.addDliverListener(this);
        counter.start();
        tsError = new TsErrorStat(belt);
        tsError.addDliverListener(this);
        tsError.start();
        belt.requestCUTime(1); // default to 4ms timestamps
        belt.getModelInfo();
        belt.getSerialNumber();
        belt.getCUFWRevision(0);
        belt.setBTUpdateInterval(1);
        //jCheckBox1.setSelected(false);
        total_overrun = 0;
        jTextFieldOver.setText("" + total_overrun + " (0)");
    }


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (belt == null) { // Connect
            belt = connectDliver();
            if (belt == null) {
                return;
            }
            do_connect(belt);
            // start the timesync
        } else { // Disconnect
            try {
                // start the timesync
                belt.close();
                belt = null;
                if (serialPort != null) {
                    tryToCloseSerialPort();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (belt != null) {
            jButton1.setText("Disconnect");
        } else {
            jButton1.setText("Connect...");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    IMUGraphForm imuform = new IMUGraphForm(belt);
    imuform.setSize(600, 800);
    imuform.setLocationRelativeTo(this);
    imuform.setVisible(true);
}//GEN-LAST:event_jButton2ActionPerformed

private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
    ECGGraphForm ecgform = new ECGGraphForm(belt);
    ecgform.setSize(600, 300);
    ecgform.setLocationRelativeTo(this);
    ecgform.setVisible(true);
}//GEN-LAST:event_jButton5ActionPerformed

private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
    BitRateGraphFrame tempform = new BitRateGraphFrame(counter);
    tempform.setSize(600, 200);
    tempform.setLocationRelativeTo(this);
    tempform.setVisible(true);
}//GEN-LAST:event_jButton8ActionPerformed

private void jComboBoxModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxModeActionPerformed
    if (belt != null) {
        int lastMode = belt.getLastMode();
        int selectedMode = ((DliverMode) jComboBoxMode.getSelectedItem()).getCode();
        if (lastMode != selectedMode) {
            belt.setDataMode(((DliverMode) jComboBoxMode.getSelectedItem()));
            if (((DliverMode) jComboBoxMode.getSelectedItem()) == DliverMode.Test) {
                activeTestMode = true;
            } else {
                activeTestMode = false;
            }
        }
    }
}//GEN-LAST:event_jComboBoxModeActionPerformed

private void jRadioButtonChActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonChActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_jRadioButtonChActionPerformed

private void jComboBoxBTIntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxBTIntActionPerformed
    if (belt != null) {
        belt.setBTUpdateInterval(
                Integer.parseInt(jComboBoxBTInt.getSelectedItem().toString()));
    }
}//GEN-LAST:event_jComboBoxBTIntActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItemUDPConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemUDPConnectActionPerformed

        UDPConnectDialog d = new UDPConnectDialog(this, true);
        d.setVisible(true);
        if (d.getResult() != null) {
            belt = d.getResult();
            belt.addDliverListener(this);
            //((GraphPanel)jPanelECG).start();
            counter = new BitRateCounter(belt);
            counter.addDliverListener(this);
            counter.start();
            tsError = new TsErrorStat(belt);
            tsError.addDliverListener(this);
            tsError.start();
            //belt.requestCUTime(0);
            //belt.getModelInfo();
            //belt.getSerialNumber();
            //belt.getCUFWRevision(0);
            //belt.setBTUpdateInterval(1);
            //jCheckBox1.setSelected(false);
            total_overrun = 0;
            jTextFieldOver.setText("" + total_overrun + " (0)");
        }

    }//GEN-LAST:event_jMenuItemUDPConnectActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        if (belt != null) {
            TimeSyncFrame f = new TimeSyncFrame(belt.getTimeSynchronizer());
            f.setLocationRelativeTo(this);
            f.pack();
            f.setVisible(true);
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        if (belt != null) {
            PTTGraphForm pttform = new PTTGraphForm(belt);
            pttform.setSize(600, 750);
            pttform.setLocationRelativeTo(this);
            pttform.setVisible(true);
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jCheckBoxBTtraceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxBTtraceActionPerformed
        if (jCheckBoxBTtrace.isSelected()) {
            belt.OpenTrace();
        }
    }//GEN-LAST:event_jCheckBoxBTtraceActionPerformed

    private void jCheckBoxDebugConsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxDebugConsActionPerformed
        if (jCheckBoxDebugCons.isSelected()) {
            if (belt != null) {
                VerifyTestMode = new DebugConsole(50000, 1000);
                VerifyTestMode.setLocationRelativeTo(this);
                VerifyTestMode.setSize(600, 750);
                VerifyTestMode.setTitle("Verify Test Mode");
                VerifyTestMode.setVisible(true);
                VerifyTestMode.putString("***Verify Test mode***\n***Select Test Mode***\n");
                activeVerifyTestMode = true;
            }
        } else {
            activeVerifyTestMode = false;
        }
    }//GEN-LAST:event_jCheckBoxDebugConsActionPerformed

    private void jButtonBtPausedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBtPausedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonBtPausedActionPerformed

    private void jButtonVersionNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVersionNewActionPerformed
        DebugConsole verCons = new DebugConsole(5000, 1000);
        ReadVersionInfo version = new ReadVersionInfo();
        String keyInfo = "\nKey information\n";
        String detailedInfo = "Detailed information\n";
        String[] modules = {"dliver-desktop",
            "dliver-driver",
            "rtcharts-swing",
            "rtsync-core",
            "rtsync-ui"};

        keyInfo = keyInfo + version.getKeyInfoString(modules);
        detailedInfo = detailedInfo + version.getDetailedInfoString(modules);

        verCons.setSize(700, 200);
        verCons.setLocationRelativeTo(this);
        verCons.setTitle("Version information for jar-files");
        verCons.setVisible(true);
        verCons.putString(detailedInfo);
        verCons.putString(keyInfo);
    }//GEN-LAST:event_jButtonVersionNewActionPerformed

    private void jButtonAdvancedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAdvancedActionPerformed
        boolean sel = jButtonAdvanced.isSelected();
        sel = !sel;
        jButtonAdvanced.setSelected(sel);
        setAdvancedMode(sel);
    }//GEN-LAST:event_jButtonAdvancedActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        IMUGraphForm imuform = new IMUGraphForm(belt);
        imuform.setSize(600, 800);
        imuform.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButtonEraseRecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEraseRecActionPerformed
        if (belt != null) {
            EraseRecConfirmDialog dialog = new EraseRecConfirmDialog(this, true, belt);
            dialog.setLocationRelativeTo(this);
            dialog.pack();
            dialog.setVisible(true);
        }
    }//GEN-LAST:event_jButtonEraseRecActionPerformed

    private void jButtonRecBtPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRecBtPauseActionPerformed
        if (belt != null) {
            RecBtPauseDialog dialog = new RecBtPauseDialog(this, true, belt);
            dialog.setLocationRelativeTo(this);
            dialog.pack();
            dialog.setVisible(true);
        }
    }//GEN-LAST:event_jButtonRecBtPauseActionPerformed

    private void jButtonPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPlayActionPerformed
        if (belt != null) {
            if (playStateActive == true) {
                belt.sendBtGetStr("\nbuffer 11\n");  // Stop playback
            } else {
                belt.sendBtGetStr("\nbuffer 12\n");  // Start playback
            }
        }
    }//GEN-LAST:event_jButtonPlayActionPerformed

    private void jButtonRecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRecActionPerformed
        if (belt != null) {
            if (recStateActive == true) {
                belt.sendBtGetStr("\nbuffer 1\n");  // Stop record
            } else {
                belt.sendBtGetStr("\nbuffer 2\n");  // Start record append
            }
        }
    }//GEN-LAST:event_jButtonRecActionPerformed

    private void jButtonConsoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConsoleActionPerformed
        if (belt != null) {
            ConsoleFrame form = new ConsoleFrame(belt, 50000, 1000);
            form.setSize(600, 750);
            form.setLocationRelativeTo(this);
            form.setVisible(true);
        }
    }//GEN-LAST:event_jButtonConsoleActionPerformed

    private void jButtonAlertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAlertActionPerformed
        int level = Integer.parseInt(jComboBoxAlertLevel.getSelectedItem().toString());
        if (belt != null) {
            belt.sendAlert(level);
        }
    }//GEN-LAST:event_jButtonAlertActionPerformed

    private void jComboBoxAlertLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxAlertLevelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxAlertLevelActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        if (belt != null) {
            FileLoggerForm form = new FileLoggerForm(belt);
            form.setLocationRelativeTo(this);
            form.pack();
            form.setVisible(true);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButtonSensorFilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSensorFilesActionPerformed
        if (belt != null) {
            WearableFileHandlerFrame form = new WearableFileHandlerFrame(belt);
            form.setSize(450, 350);
            form.setLocationRelativeTo(this);
            form.setVisible(true);
        }
    }//GEN-LAST:event_jButtonSensorFilesActionPerformed

   
    
    int ECG_test_value = 0;
    int ECG_mismatch_cnt = 0;
    int ECG_wrap_cnt = 0;

    private void VerifyCount(int value) {
        if (activeVerifyTestMode && activeTestMode) {
            //VerifyTestMode.putString("Value " + value + "\n");
            if ((value == 0) && (ECG_test_value == 4095)) {
                ECG_wrap_cnt++;
                VerifyTestMode.putString("ECG test wrap " + ECG_wrap_cnt + " (Error count = " + ECG_mismatch_cnt + ")\n");
            } else if (value != ECG_test_value + 1) {
                ECG_mismatch_cnt++;
                VerifyTestMode.putString("ECG test data mismatch, expected " + (ECG_test_value + 1) + " got " + value + " (" + ECG_mismatch_cnt + ")\n");
            }
            ECG_test_value = value;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        try {
            // This is a very dirty hack to try and set the java.library.path dynamically.
            System.setProperty("java.library.path", ".");
            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // Set System L&F 
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            System.out.println(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new DliverMainFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupStatus;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButtonAdvanced;
    private javax.swing.JButton jButtonAlert;
    private javax.swing.JButton jButtonBtPaused;
    private javax.swing.JButton jButtonConsole;
    private javax.swing.JButton jButtonEraseRec;
    private javax.swing.JButton jButtonPlay;
    private javax.swing.JButton jButtonRec;
    private javax.swing.JButton jButtonRecBtPause;
    private javax.swing.JButton jButtonSensorFiles;
    private javax.swing.JButton jButtonVersionNew;
    private javax.swing.JCheckBox jCheckBoxBTtrace;
    private javax.swing.JCheckBox jCheckBoxDebugCons;
    private javax.swing.JComboBox jComboBoxAlertLevel;
    private javax.swing.JComboBox jComboBoxBTInt;
    private javax.swing.JComboBox jComboBoxMode;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelActivity;
    private javax.swing.JLabel jLabelPosture;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItemUDPConnect;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JProgressBar jProgressBarAx;
    private javax.swing.JProgressBar jProgressBarAy;
    private javax.swing.JProgressBar jProgressBarAz;
    private javax.swing.JProgressBar jProgressBarBatt;
    private javax.swing.JProgressBar jProgressBarGx;
    private javax.swing.JProgressBar jProgressBarGy;
    private javax.swing.JProgressBar jProgressBarGz;
    private javax.swing.JProgressBar jProgressBarHR;
    private javax.swing.JProgressBar jProgressBarPTT;
    private javax.swing.JRadioButton jRadioButtonCh;
    private javax.swing.JRadioButton jRadioButtonOff;
    private javax.swing.JRadioButton jRadioButtonOn;
    private javax.swing.JRadioButton jRadioButtonSBy;
    private javax.swing.JRadioButton jRadioButtonSW;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextFieldActTime;
    private javax.swing.JTextField jTextFieldBitRate;
    private javax.swing.JTextField jTextFieldHRTime;
    private javax.swing.JTextField jTextFieldIMUTime;
    private javax.swing.JTextField jTextFieldOver;
    private javax.swing.JTextField jTextFieldPTTTime;
    private javax.swing.JTextField jTextFieldPostTime;
    private javax.swing.JTextField jTextFieldSFW;
    private javax.swing.JTextField jTextFieldSID;
    private javax.swing.JTextField jTextFieldStepCount;
    private javax.swing.JTextField jTextFieldTsErr;
    // End of variables declaration//GEN-END:variables

    @Override
    public void cUSerialNumber(long value) {
        jTextFieldSID.setText("" + value);
        setTitle("d-LIVER [" + value + "]");
    }

    @Override
    public void cUFWRevision(String value) {
        jTextFieldSFW.setText("" + value);
    }

    @Override
    public void batteryStatus(int value) {
        jProgressBarBatt.setString("" + value + " %");
        jProgressBarBatt.setValue(value);

    }

    javax.swing.ImageIcon UPRIGHT = new javax.swing.ImageIcon(getClass().getResource("/upright.png"));
    javax.swing.ImageIcon PRONE = new javax.swing.ImageIcon(getClass().getResource("/prone.png"));
    javax.swing.ImageIcon SUPINE = new javax.swing.ImageIcon(getClass().getResource("/supine.png"));
    javax.swing.ImageIcon SIDE = new javax.swing.ImageIcon(getClass().getResource("/side.png"));
    javax.swing.ImageIcon INVERTED = new javax.swing.ImageIcon(getClass().getResource("/inverted.png"));

    javax.swing.ImageIcon[] ACTIVITY = {new javax.swing.ImageIcon(getClass().getResource("/Activity_0.png")),
        new javax.swing.ImageIcon(getClass().getResource("/Activity_1.png")),
        new javax.swing.ImageIcon(getClass().getResource("/Activity_2.png")),
        new javax.swing.ImageIcon(getClass().getResource("/Activity_3.png"))};

    boolean recStateActive = false;
    boolean playStateActive = false;

    @Override
    public void indicationDev(int value) {

        if (value >= 20 && value <= 24) { // Device status
            switch (value) {
                case 20:
                    jRadioButtonOn.setSelected(true);
                    break;
                case 21:
                    jRadioButtonOff.setSelected(true);
                    break;
                case 22:
                    jRadioButtonSBy.setSelected(true);
                    break;
                case 23:
                    jRadioButtonCh.setSelected(true);
                    break;
                case 24:
                    jRadioButtonSW.setSelected(true);
                    break;
                default:
                    break;
            }
        } else if (value >= 28 && value <= 34) {
            //jComboBoxMode.setSelectedItem(ChestBeltMode.fromCode(value));
            jComboBoxMode.setSelectedItem(DliverMode.fromCode(value));
            System.out.println("Indication : " + value);
        } else if (value >= 57 && value <= 63) { // BT status
            switch (value) {
                case 57:
                    break;
                case 58:
                    setButtonColor(jButtonBtPaused, new java.awt.Color(255, 51, 51));
                    break;
                case 59:
                    setButtonColor(jButtonBtPaused, new java.awt.Color(204, 204, 204));
                    break;
                case 60:
                    setButtonColor(jButtonRec, new java.awt.Color(255, 51, 51));
                    recStateActive = true;
                    break;
                case 61:
                    setButtonColor(jButtonRec, null);
                    recStateActive = false;
                    break;
                case 62:
                    setButtonColor(jButtonPlay, new java.awt.Color(51, 255, 51));
                    playStateActive = true;
                    break;
                case 63:
                    setButtonColor(jButtonPlay, null);
                    playStateActive = false;
                    break;
                default:
                    break;
            }
        }
    }

    private void setButtonColor(javax.swing.JButton button, java.awt.Color color) {
        if (color == null) {
            button.setBackground(new java.awt.Color(240, 240, 240));
            //button.setOpaque(false);
            //button.setContentAreaFilled(true);
        } else {
            button.setBackground(color);
            //button.setContentAreaFilled(false);
            //button.setOpaque(true);
        }
    }

    @Override
    public void measurementPatient(int value, int timestamp) {

        if (value >= 1 && value <= 6) { // This is orientation
            switch (value) {
                case 1:
                    jLabelPosture.setIcon(UPRIGHT);
                    break;
                case 2:
                    jLabelPosture.setIcon(PRONE);
                    break;
                case 3:
                    jLabelPosture.setIcon(SUPINE);
                    break;
                case 4:
                    jLabelPosture.setIcon(SIDE);
                    break;
                case 5:
                    jLabelPosture.setIcon(INVERTED);
                    break;
                case 6:
                    jLabelPosture.setIcon(null);
                    break;
                default:
                    break;
            }
            jTextFieldPostTime.setText("" + timestamp);
        } else if (value >= 10 && value <= 13) { // This is activity
            jLabelActivity.setIcon(ACTIVITY[value - 10]);
            jTextFieldActTime.setText("" + timestamp);
        }

    }

    @Override
    public void stepCount(long step, int timestamp) {
        jTextFieldStepCount.setText("" + step);
    }

    int total_overrun = 0;

    @Override
    public void messageOverrun(int value) {
        total_overrun += value;
        jTextFieldOver.setText("" + total_overrun + " (" + value + ")");
    }

    @Override
    public void referenceClockTime(long value, boolean seconds) {
        //jTextFieldRefTime.setText("" + value);
        //jCheckBox1.setSelected(!seconds);
        //System.out.println("[ReferenceClockTimeSync] " + value + " (" + seconds + ")");

    }

    @Override
    public void fullClockTimeSync(long value, boolean seconds) {
        System.out.println("[FullClockTimeSync] " + value + " (" + seconds + ")");

    }

    @Override
    public void heartRate(int value, int timestamp) {
        //System.out.println("Heart Rate = " + (value/10) + " (t=" + timestamp + ")" );
        jProgressBarHR.setString("" + new DecimalFormat("##.0").format((double) value / 10.0) + " BPM");
        jProgressBarHR.setValue(value);
        jTextFieldHRTime.setText("" + timestamp);

    }

    @Override
    public void heartRateInterval(int value, int timestamp) {

    }

    @Override
    public void eCGData(int value) {
        VerifyCount(value);
    }

    @Override
    public void eCGSignalQuality(int value, int timestamp) {
        //jTextFieldECGQ.setText(""+value +" %");
        //jProgressBarECGQ.setValue(value);

    }

    @Override
    public void eCGRaw(int value, int timestamp) {
        VerifyCount(value);
    }

    @Override
    public void gyroPitch(int value, int timestamp) {
        //jTextFieldGy.setText(""+value);
        //jProgressBarGy.setValue(value);
    }

    @Override
    public void gyroRoll(int value, int timestamp) {
        //jTextFieldGx.setText(""+value);
        //jProgressBarGx.setValue(value);
    }

    @Override
    public void gyroYaw(int value, int timestamp) {
        //jTextFieldGz.setText(""+value);
        //jProgressBarGz.setValue(value);

    }

    @Override
    public void accLateral(int value, int timestamp) {
        // jTextFieldAy.setText(""+value);
        //jProgressBarAy.setValue(value);
    }

    @Override
    public void accLongitudinal(int value, int timestamp) {
        //jTextFieldAz.setText(""+value);
        //jProgressBarAz.setValue(value);

    }

    @Override
    public void accVertical(int value, int timestamp) {
        //jTextFieldAx.setText(""+value);
        //jProgressBarAx.setValue(value);

    }

    @Override
    public void rawActivityLevel(int value, int timestamp) {
        if (value >= 10 && value <= 13) { // This is activity
            //jLabelActivity.setIcon(ACTIVITY[value-10]);
            //jTextFieldActTime.setText("R" + timestamp);  
        }
    }

    @Override
    public void combinedIMU(int ax, int ay, int az, int gx, int gy, int gz, int timestamp) {
        //System.out.println("IMU: " + ax + "\t" + ay + "\t" + az + "\t" + gx + "\t" + gy + "\t" + gz + "\t" + "(" + timestamp + ")");
        //jTextFieldAx.setText(""+ax);
        jProgressBarAx.setValue(ax);

        //jTextFieldAy.setText(""+ay);
        jProgressBarAy.setValue(ay);
        
        //jTextFieldAz.setText(""+az);
        jProgressBarAz.setValue(az);
        
        //jTextFieldGx.setText(""+gx);
        jProgressBarGx.setValue(gx);
        
        //jTextFieldGy.setText(""+gy);
        jProgressBarGy.setValue(gy);
        
        //jTextFieldGz.setText(""+gz);
        jProgressBarGz.setValue(gz);
        
        jTextFieldIMUTime.setText(""+timestamp);
    }

    @Override
    public void skinTemperature(int value, int timestamp) {
        //System.out.println("Skin Temp = " + (value/10) + " (t=" + timestamp + ")" );

        //jProgressBarT.setString(""+new DecimalFormat("##.0").format((double)value/10.0) + " °C");
        //jProgressBarT.setValue(value);
        //jTextFieldTTime.setText("" + timestamp);
    }

    @Override
    public void bitRate(int value) {
        jTextFieldBitRate.setText("" + value);
    }

    @Override
    public void errorAverage(long average, int errorLevel) {
        //jTextFieldBitRate1.setText("" + average);
        if (errorLevel >= 0) {
            jTextFieldTsErr.setText("" + average);
        }
        switch (errorLevel) {
            case -1:
                jTextFieldTsErr.setText("Off");
                jTextFieldTsErr.setBackground(new java.awt.Color(204, 0, 0)); // Red
                break;
            case 0:
                jTextFieldTsErr.setBackground(new java.awt.Color(0, 204, 0)); // Green
                break;
            case 1:
                jTextFieldTsErr.setBackground(new java.awt.Color(204, 204, 0)); // Yellow
                break;
            case 2:
                jTextFieldTsErr.setBackground(new java.awt.Color(204, 102, 0)); // Orange
                break;
            case 3:
                jTextFieldTsErr.setBackground(new java.awt.Color(204, 0, 204)); // Violet
                break;
            case 4:
            case 5:
                jTextFieldTsErr.setBackground(new java.awt.Color(204, 0, 0)); // Red
                break;
            default:
                jTextFieldTsErr.setText("?" + errorLevel);
                jTextFieldTsErr.setBackground(new java.awt.Color(204, 0, 0)); // Red
                break;
        }
    }

    static {

        try {
            String osName = System.getProperty("os.name");
            String osProc = System.getProperty("os.arch");

            //System.out.println(System.properties['java.library.path']);
            System.out.println("Load RxTx for os.name=" + osName + " os.arch=" + osProc + " sun.arch.data.model=" + System.getProperty("sun.arch.data.model"));
            System.out.println("Current path = " + new File(".").getAbsolutePath());

            if (osName.equals("Mac OS X")) {
                System.out.println("Copiying " + "nativelib/Mac_OS_X/librxtxSerial.jnilib");
                NativeLibUtil.copyFile(NativeLibUtil.class.getClassLoader().getResourceAsStream("nativelib/Mac_OS_X/librxtxSerial.jnilib"), "librxtxSerial.jnilib");
            }
            if (osName.contains("Win") && System.getProperty("sun.arch.data.model").contains("32")) {
                System.out.println("Copiying " + "nativelib/Windows/win32/rxtxSerial.dll");
                NativeLibUtil.copyFile(NativeLibUtil.class.getClassLoader().getResourceAsStream("nativelib/Windows/win32/rxtxSerial.dll"), "rxtxSerial.dll");
            }
            if (osName.contains("Win") && System.getProperty("sun.arch.data.model").contains("64")) {
                System.out.println("Copiying " + "nativelib/Windows/win64/rxtxSerial.dll");
                NativeLibUtil.copyFile(NativeLibUtil.class.getClassLoader().getResourceAsStream("nativelib/Windows/win64/rxtxSerial.dll"), "rxtxSerial.dll");
            }
            if (osName.equals("Linux") && osProc.equals("ia64")) {
                System.out.println("Copiying " + "nativelib/Linux/ia64-unknown-linux-gnu/librxtxSerial.so");
                NativeLibUtil.copyFile(NativeLibUtil.class.getClassLoader().getResourceAsStream("nativelib/Linux/ia64-unknown-linux-gnu/librxtxSerial.so"), "librxtxSerial.so");
            } else if (osName.equals("Linux") && System.getProperty("sun.arch.data.model").contains("64")) {
                System.out.println("Copiying " + "nativelib/Linux/x86_64-unknown-linux-gnu/librxtxSerial.so");
                NativeLibUtil.copyFile(NativeLibUtil.class.getClassLoader().getResourceAsStream("nativelib/Linux/x86_64-unknown-linux-gnu/librxtxSerial.so"), "librxtxSerial.so");
            }
            if (osName.equals("Linux") && System.getProperty("sun.arch.data.model").contains("32")) {
                System.out.println("Copiying " + "nativelib/Linux/i686-unknown-linux-gnu/librxtxSerial.so");
                NativeLibUtil.copyFile(NativeLibUtil.class.getClassLoader().getResourceAsStream("nativelib/Linux/i686-unknown-linux-gnu/librxtxParallel.so"), "librxtxParallel.so");
                NativeLibUtil.copyFile(NativeLibUtil.class.getClassLoader().getResourceAsStream("nativelib/Linux/i686-unknown-linux-gnu/librxtxSerial.so"), "librxtxSerial.so");
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        jProgressBarPTT.setString("" + value + " msec");
        jProgressBarPTT.setValue(value);
        jTextFieldPTTTime.setText("" + timestamp);
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
    public void ppgRaw(int ppgDer, int timestamp) {
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

    @Override
    public void fmtReceived(byte[] message, int size) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
