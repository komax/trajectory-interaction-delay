/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visualization;

import frechet.Matching;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import matlabconversion.MatchingReader;

/**
 *
 * @author max
 */
public class AnalyticsDelayUI extends javax.swing.JFrame {
    private Matching matching = null;
    private BufferedImage freeSpaceImage = null;
    private MatchingPlot matchingPlot;
    private DelayPlotPanel normalizedDelayPlot;

    /**
     * Creates new form AnalyticsDelayUI
     */
    public AnalyticsDelayUI() {
        initComponents();
        this.matching = MatchingReader.readMatching("batsMatching.dump");
        initSlider();
        initDelaySpace();
        initMatchingPlot();
        initDelayPlot();
    }
    
    private void initSlider() {
        this.matchingSlider.setMinimum(0);
        this.matchingSlider.setMaximum(this.matching.i.length - 1);
    }

    private void initDelaySpace() {
        try {
            this.freeSpaceImage = ImageIO.read(new File("delay_space_bats.png"));
            JLabel freeSpaceLabel = new JLabel(new ImageIcon(freeSpaceImage));
            this.delaySpacePanel.add(freeSpaceLabel);
        } catch (IOException ex) {
            Logger.getLogger(AnalyticsDelayUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void initMatchingPlot() {
        this.matchingPlot = new MatchingPlot(matching);
        this.trajectoryPlotPanel.add(matchingPlot);
    }
    
    private void initDelayPlot() {
        this.normalizedDelayPlot = new DelayPlotPanel(matching);
        this.normalizedDelayPanel.add(normalizedDelayPlot);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sliderPanel = new javax.swing.JPanel();
        matchingSlider = new javax.swing.JSlider();
        settingsPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        distanceComboBox = new javax.swing.JComboBox();
        distanceField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        unitComboBox = new javax.swing.JComboBox();
        unitDelayField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        thresholdUnitComboBox = new javax.swing.JComboBox();
        thresholdSpinner = new javax.swing.JSpinner();
        trajectoryPlotPanel = new javax.swing.JPanel();
        delaySpacePanel = new javax.swing.JPanel();
        normalizedDelayPanel = new javax.swing.JPanel();
        delayInUnitPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Visualization of Delays in Trajectories");

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
            .addComponent(matchingSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        sliderPanelLayout.setVerticalGroup(
            sliderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(matchingSlider, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
        );

        settingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Settings"));

        jLabel1.setText("Delay in");

        distanceComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Euclidean", "Absolute Value", "Infinity Norm" }));

        distanceField.setText("5.78");

        jLabel2.setText("Delay in Unit");

        unitComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "meters", "seconds" }));

        unitDelayField.setText("1.35");

        jLabel3.setText("Delay Threshold");

        thresholdUnitComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "meters", "seconds" }));

        javax.swing.GroupLayout settingsPanelLayout = new javax.swing.GroupLayout(settingsPanel);
        settingsPanel.setLayout(settingsPanelLayout);
        settingsPanelLayout.setHorizontalGroup(
            settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, settingsPanelLayout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(thresholdUnitComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(thresholdSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, settingsPanelLayout.createSequentialGroup()
                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(settingsPanelLayout.createSequentialGroup()
                        .addComponent(unitComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(unitDelayField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(settingsPanelLayout.createSequentialGroup()
                        .addComponent(distanceComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(distanceField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        settingsPanelLayout.setVerticalGroup(
            settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsPanelLayout.createSequentialGroup()
                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(distanceComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(distanceField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(unitComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unitDelayField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(thresholdUnitComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(thresholdSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        trajectoryPlotPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Trajectory Plot"));
        trajectoryPlotPanel.setPreferredSize(new java.awt.Dimension(626, 300));
        trajectoryPlotPanel.setLayout(new java.awt.BorderLayout());

        delaySpacePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Delay Space"));
        delaySpacePanel.setLayout(new java.awt.BorderLayout());

        normalizedDelayPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Delay Plot (Normalized)"));
        normalizedDelayPanel.setPreferredSize(new java.awt.Dimension(30, 170));
        normalizedDelayPanel.setLayout(new java.awt.BorderLayout());

        delayInUnitPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Delay Plot (in Unit)"));

        javax.swing.GroupLayout delayInUnitPanelLayout = new javax.swing.GroupLayout(delayInUnitPanel);
        delayInUnitPanel.setLayout(delayInUnitPanelLayout);
        delayInUnitPanelLayout.setHorizontalGroup(
            delayInUnitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        delayInUnitPanelLayout.setVerticalGroup(
            delayInUnitPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 159, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sliderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(settingsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(delaySpacePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(trajectoryPlotPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(normalizedDelayPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(delayInUnitPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(trajectoryPlotPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(normalizedDelayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(delayInUnitPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(sliderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(delaySpacePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(settingsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(74, 74, 74))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void matchingSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_matchingSliderStateChanged
        
    }//GEN-LAST:event_matchingSliderStateChanged

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
    private javax.swing.JPanel delayInUnitPanel;
    private javax.swing.JPanel delaySpacePanel;
    private javax.swing.JComboBox distanceComboBox;
    private javax.swing.JTextField distanceField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JSlider matchingSlider;
    private javax.swing.JPanel normalizedDelayPanel;
    private javax.swing.JPanel settingsPanel;
    private javax.swing.JPanel sliderPanel;
    private javax.swing.JSpinner thresholdSpinner;
    private javax.swing.JComboBox thresholdUnitComboBox;
    private javax.swing.JPanel trajectoryPlotPanel;
    private javax.swing.JComboBox unitComboBox;
    private javax.swing.JTextField unitDelayField;
    // End of variables declaration//GEN-END:variables
}
