/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visualization;

import frechet.Matching;
import matlabconversion.MatchingReader;
import utils.DistanceNorm;
import utils.Utils;

/**
 *
 * @author max
 */
public class AnalyticsDelayUI extends javax.swing.JFrame {
    public enum DelaySpaceType {

        USUAL,
        DIRECTIONAL_DISTANCE,
        DYNAMIC_INTERACTION,
        HEADING
    };
    
    public static final String PATH_TO_DATA = "results/frisbee/";

    private Matching matching = null;
    private MatchingPlot matchingPlot;
    private DistancePlotPanel distancePlot;
    private DelaySpacePanel delaySpacePlot;
    private FollowingPlotPanel followingDelayPlot;
    private double[] distancesOnMatching;
    private DistanceNorm currentDistance;
    private String imageName;
    private DelaySpaceType delaySpaceType;
    private int threshold;
    private int translucentFocus;
    private double samplingRate;
    private boolean logScaled;

    /**
     * Creates new form AnalyticsDelayUI
     */
    public AnalyticsDelayUI() {
        initComponents();
        this.matching = MatchingReader.readMatching(PATH_TO_DATA + "matchingNorm2.dump");
        this.delaySpaceType = DelaySpaceType.USUAL;
        this.logScaled = false;
        this.threshold = 1;
        this.samplingRate = 0.2;
        this.translucentFocus = 50;
        updateDistanceAndMatching(Utils.EuclideanDistance, this.delaySpaceType, this.logScaled);
        initSlider();
        initDelaySpace();
        initMatchingPlot();
        initDelayPlot();
        initFollowingPlot();
    }

    private void initSlider() {
        this.matchingSlider.setMinimum(0);
        this.matchingSlider.setMaximum(this.matching.i.length - 1);
    }

    private void initDelaySpace() {
        String delaySpaceName = "delaySpaceNorm2.png";
        if (logScaled) {
            delaySpaceName = "delaySpaceNorm2logScale.png";
        }
        this.delaySpacePlot = new DelaySpacePanel(PATH_TO_DATA + delaySpaceName, matching.getTrajectory1().length, threshold);
        this.delaySpacePanel.add(this.delaySpacePlot);
    }

    private void initMatchingPlot() {
        this.matchingPlot = new MatchingPlot(matching, threshold, translucentFocus);
        this.trajectoryPlotPanel.add(matchingPlot);
    }

    private void initDelayPlot() {
        this.distancePlot = new DistancePlotPanel(matching, Utils.EuclideanDistance);
        this.distancePanel.add(distancePlot);
    }

    private void initFollowingPlot() {
        this.followingDelayPlot = new FollowingPlotPanel(matching, threshold, 0.2);
        this.delayPanel.add(followingDelayPlot);
    }

    private void updateDistanceAndMatching(DistanceNorm distance, DelaySpaceType delaySpace, boolean logScale) {
        this.currentDistance = distance;
        this.delaySpaceType = delaySpace;
        this.logScaled = logScale;
        String normString = distance.toString();
        String delaySpaceSuffix = "";
        switch (delaySpace) {
            case USUAL:
                delaySpaceSuffix = "";
                break;
            case DYNAMIC_INTERACTION:
                delaySpaceSuffix = "DynamicInteraction";
                break;
            case DIRECTIONAL_DISTANCE:
                delaySpaceSuffix = "DirectionalDistance";
                break;
            case HEADING:
                delaySpaceSuffix = "Heading";
                break;
        }
        String combinedSuffix = normString + delaySpaceSuffix;
        this.matching = MatchingReader.readMatching(PATH_TO_DATA + "matching" + combinedSuffix + ".dump");
        String logScaleSuffix = "";
        if (logScaled) {
            logScaleSuffix = "logScale";
        }
        this.imageName = PATH_TO_DATA + "delaySpace" + combinedSuffix + logScaleSuffix + ".png";
        switch (delaySpace) {
            case USUAL:
                this.distancesOnMatching = Utils.distancesOnMatching(matching, currentDistance);
                break;
            case DYNAMIC_INTERACTION:
                double alpha = 2.0;
                this.distancesOnMatching = Utils.dynamicInteractionOnMatching(matching, distance, alpha);
                break;
            case DIRECTIONAL_DISTANCE:
                this.distancesOnMatching = Utils.directionalDistancesOnMatching(matching, currentDistance);
                break;
            case HEADING:
                this.distancesOnMatching = Utils.headingOnMatching(matching);
                break;
        }
    }

    private void updateAndRepaintPlots() {
        this.matchingSlider.setMaximum(this.matching.i.length - 1);
        if (followingDelayPlot != null) {
            followingDelayPlot.updateMatching(matching, threshold, samplingRate);
            followingDelayPlot.repaint();
        }
        if (distancePlot != null) {
            distancePlot.updateMatching(matching, currentDistance);
            distancePlot.repaint();
        }
        if (matchingPlot != null) {
            matchingPlot.updateMatching(matching, threshold, translucentFocus);
            matchingPlot.repaint();
        }
        if (delaySpacePlot != null) {
            delaySpacePlot.updateImage(imageName);
            matchingPlot.repaint();
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

        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        sliderPanel = new javax.swing.JPanel();
        matchingSlider = new javax.swing.JSlider();
        jSplitPane5 = new javax.swing.JSplitPane();
        delaySpacePanel = new javax.swing.JPanel();
        settingsPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        distanceNormComboBox = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        samplingRateComboBox = new javax.swing.JComboBox();
        samplingRateField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        thresholdComboBox = new javax.swing.JComboBox();
        thresholdSpinner = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        delaySpaceComboBox = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        focusSpinner = new javax.swing.JSpinner();
        logScalingCheckBox = new javax.swing.JCheckBox();
        jSplitPane3 = new javax.swing.JSplitPane();
        trajectoryPlotPanel = new javax.swing.JPanel();
        jSplitPane4 = new javax.swing.JSplitPane();
        distancePanel = new javax.swing.JPanel();
        delayPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Visualization of Delays in Trajectories");

        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        sliderPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Matching"));

        matchingSlider.setMajorTickSpacing(50);
        matchingSlider.setMinorTickSpacing(25);
        matchingSlider.setPaintLabels(true);
        matchingSlider.setPaintTicks(true);
        matchingSlider.setToolTipText("");
        matchingSlider.setValue(0);
        matchingSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                matchingSliderStateChanged(evt);
            }
        });

        javax.swing.GroupLayout sliderPanelLayout = new javax.swing.GroupLayout(sliderPanel);
        sliderPanel.setLayout(sliderPanelLayout);
        sliderPanelLayout.setHorizontalGroup(
            sliderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(matchingSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
        );
        sliderPanelLayout.setVerticalGroup(
            sliderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(matchingSlider, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, Short.MAX_VALUE)
        );

        jSplitPane2.setTopComponent(sliderPanel);

        jSplitPane5.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        delaySpacePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Delay Space"));
        delaySpacePanel.setPreferredSize(new java.awt.Dimension(560, 420));
        delaySpacePanel.setLayout(new java.awt.BorderLayout());
        jSplitPane5.setBottomComponent(delaySpacePanel);

        settingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Settings"));

        jLabel1.setText("Distance Norm");

        distanceNormComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Absolute Value", "Euclidean", "Infinity Norm" }));
        distanceNormComboBox.setSelectedIndex(1);
        distanceNormComboBox.setToolTipText("");
        distanceNormComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                distanceNormComboBoxItemStateChanged(evt);
            }
        });

        jLabel2.setText("Sampling Rate");

        samplingRateComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "seconds", "Hertz" }));
        samplingRateComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                samplingRateComboBoxItemStateChanged(evt);
            }
        });

        samplingRateField.setText("0.2");
        samplingRateField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                samplingRateFieldActionPerformed(evt);
            }
        });

        jLabel3.setText("Delay Threshold");

        thresholdComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "timestamps", "seconds", "milliseconds" }));

        thresholdSpinner.setValue(1);
        thresholdSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                thresholdSpinnerStateChanged(evt);
            }
        });

        jLabel4.setText("Delay Space");

        delaySpaceComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Usual Distance", "Directional Distance", "Dynamic Interaction", "Heading" }));
        delaySpaceComboBox.setToolTipText("");
        delaySpaceComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                delaySpaceComboBoxItemStateChanged(evt);
            }
        });

        jLabel5.setText("Focus on Matching");

        focusSpinner.setValue(50);
        focusSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                focusSpinnerStateChanged(evt);
            }
        });

        logScalingCheckBox.setText("log Scaling");
        logScalingCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                logScalingCheckBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout settingsPanelLayout = new javax.swing.GroupLayout(settingsPanel);
        settingsPanel.setLayout(settingsPanelLayout);
        settingsPanelLayout.setHorizontalGroup(
            settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsPanelLayout.createSequentialGroup()
                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(settingsPanelLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(6, 6, 6)
                        .addComponent(thresholdComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(samplingRateField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                            .addComponent(thresholdSpinner, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(settingsPanelLayout.createSequentialGroup()
                        .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(samplingRateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(distanceNormComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(settingsPanelLayout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(focusSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(settingsPanelLayout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(delaySpaceComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(logScalingCheckBox))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        settingsPanelLayout.setVerticalGroup(
            settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsPanelLayout.createSequentialGroup()
                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(delaySpaceComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(distanceNormComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(samplingRateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(samplingRateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(focusSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(thresholdComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(thresholdSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(logScalingCheckBox)))
        );

        jSplitPane5.setLeftComponent(settingsPanel);

        jSplitPane2.setRightComponent(jSplitPane5);

        jSplitPane1.setLeftComponent(jSplitPane2);

        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        trajectoryPlotPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Trajectory Plot"));
        trajectoryPlotPanel.setPreferredSize(new java.awt.Dimension(500, 400));
        trajectoryPlotPanel.setRequestFocusEnabled(false);
        trajectoryPlotPanel.setLayout(new java.awt.BorderLayout());
        jSplitPane3.setTopComponent(trajectoryPlotPanel);

        jSplitPane4.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        distancePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Distance Plot"));
        distancePanel.setPreferredSize(new java.awt.Dimension(30, 170));
        distancePanel.setLayout(new java.awt.BorderLayout());
        jSplitPane4.setTopComponent(distancePanel);

        delayPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Delay Plot"));
        delayPanel.setPreferredSize(new java.awt.Dimension(30, 170));
        delayPanel.setLayout(new java.awt.BorderLayout());
        jSplitPane4.setBottomComponent(delayPanel);

        jSplitPane3.setRightComponent(jSplitPane4);

        jSplitPane1.setRightComponent(jSplitPane3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void matchingSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_matchingSliderStateChanged
        if (!matchingSlider.getValueIsAdjusting()) {
            int newValue = matchingSlider.getValue();
            matchingSlider.setToolTipText(Integer.toString(newValue));
            if (distancePlot != null) {
                distancePlot.setSelectedIndex(newValue);
                distancePanel.repaint();
            }
            if (matchingPlot != null) {
                matchingPlot.setSelectedIndex(newValue);
                trajectoryPlotPanel.repaint();
            }
            if (followingDelayPlot != null) {
                followingDelayPlot.setSelectedIndex(newValue);
                delayPanel.repaint();
            }
            if (delaySpacePlot != null) {
                delaySpacePlot.setSelectedIndices(matching.i[newValue], matching.j[newValue]);
                delaySpacePanel.repaint();
            }
        }
    }//GEN-LAST:event_matchingSliderStateChanged

    private void distanceNormComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_distanceNormComboBoxItemStateChanged
        int selectedIndex = distanceNormComboBox.getSelectedIndex();
        int lastElement = distanceNormComboBox.getItemCount() - 1;
        if (selectedIndex == lastElement) {
            updateDistanceAndMatching(Utils.LInfDistance, this.delaySpaceType, this.logScaled);
        } else {
            updateDistanceAndMatching(Utils.selectDistanceNorm(selectedIndex + 1), this.delaySpaceType, this.logScaled);
        }
        updateAndRepaintPlots();
    }//GEN-LAST:event_distanceNormComboBoxItemStateChanged

    private void delaySpaceComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_delaySpaceComboBoxItemStateChanged
        int selectedIndex = delaySpaceComboBox.getSelectedIndex();
        switch (selectedIndex) {
            case 0:
                updateDistanceAndMatching(currentDistance, DelaySpaceType.USUAL, this.logScaled);
                break;
            case 1:
                updateDistanceAndMatching(currentDistance, DelaySpaceType.DIRECTIONAL_DISTANCE, this.logScaled);
                break;
            case 2:
                updateDistanceAndMatching(currentDistance, DelaySpaceType.DYNAMIC_INTERACTION, this.logScaled);
                break;
            case 3:
                updateDistanceAndMatching(currentDistance, DelaySpaceType.HEADING, this.logScaled);
                break;
        }
        updateAndRepaintPlots();
    }//GEN-LAST:event_delaySpaceComboBoxItemStateChanged

    private void thresholdSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_thresholdSpinnerStateChanged
        int newThreshold = (int) thresholdSpinner.getValue();
        if (newThreshold > 0) {
            threshold = newThreshold;
            updateAndRepaintPlots();
        } else {
            thresholdSpinner.setValue(1);
        }
    }//GEN-LAST:event_thresholdSpinnerStateChanged

    private void samplingRateComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_samplingRateComboBoxItemStateChanged
        setSamplingRate();
        updateAndRepaintPlots();
    }//GEN-LAST:event_samplingRateComboBoxItemStateChanged

    private void samplingRateFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_samplingRateFieldActionPerformed
        setSamplingRate();
        updateAndRepaintPlots();
    }//GEN-LAST:event_samplingRateFieldActionPerformed

    private void focusSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_focusSpinnerStateChanged
        int newFocusValue = (int) focusSpinner.getValue();
        if (newFocusValue > 0) {
            translucentFocus = newFocusValue;
            updateAndRepaintPlots();
        } else {
            focusSpinner.setValue(1);
        }
    }//GEN-LAST:event_focusSpinnerStateChanged

    private void logScalingCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_logScalingCheckBoxItemStateChanged
        if(logScalingCheckBox.isSelected()) {
            this.logScaled = true;
        } else {
            this.logScaled = false;
        }
        updateDistanceAndMatching(currentDistance, delaySpaceType, logScaled);
        updateAndRepaintPlots();
    }//GEN-LAST:event_logScalingCheckBoxItemStateChanged

    
    private void setSamplingRate() {
        int selectedIndex = samplingRateComboBox.getSelectedIndex();
        switch(selectedIndex) {
            case 0:
                this.samplingRate = Double.valueOf(samplingRateField.getText());
                break;

            case 1:
                double hertzFrequenz = Double.valueOf(samplingRateField.getText());
                this.samplingRate = 1.0 / hertzFrequenz;
                break;
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AnalyticsDelayUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AnalyticsDelayUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AnalyticsDelayUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AnalyticsDelayUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AnalyticsDelayUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel delayPanel;
    private javax.swing.JComboBox delaySpaceComboBox;
    private javax.swing.JPanel delaySpacePanel;
    private javax.swing.JComboBox distanceNormComboBox;
    private javax.swing.JPanel distancePanel;
    private javax.swing.JSpinner focusSpinner;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JSplitPane jSplitPane4;
    private javax.swing.JSplitPane jSplitPane5;
    private javax.swing.JCheckBox logScalingCheckBox;
    private javax.swing.JSlider matchingSlider;
    private javax.swing.JComboBox samplingRateComboBox;
    private javax.swing.JTextField samplingRateField;
    private javax.swing.JPanel settingsPanel;
    private javax.swing.JPanel sliderPanel;
    private javax.swing.JComboBox thresholdComboBox;
    private javax.swing.JSpinner thresholdSpinner;
    private javax.swing.JPanel trajectoryPlotPanel;
    // End of variables declaration//GEN-END:variables
}
