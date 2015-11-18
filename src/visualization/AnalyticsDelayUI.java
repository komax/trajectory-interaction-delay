/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visualization;

import delayspace.DelaySpace;
import delayspace.DelaySpaceType;
import frechet.Matching;
import frechet.TripleFrechetMatching;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Experiment;
import utils.IntTriple;
import utils.MatchingType;
import utils.PairInTriple;
import utils.Trajectory;
import utils.TrajectoryReader;
import utils.TripleDTWMatching;
import utils.TripletTrajectoryReader;
import utils.distance.DistanceNorm;
import utils.distance.DistanceNormFactory;

/**
 *
 * @author max
 */
public class AnalyticsDelayUI extends javax.swing.JFrame {
 //   public static final String PATH_TO_TRAJ_DATA = "/home/max/Documents/phd/pigeon_trajectory_data/pigeon_trajectory.txt";
  //  public static final String PATH_TO_TRAJ_DATA = "/home/max/Documents/phd/ultimate_frisbee_data/ultimate_frisbee_interpolated.txt";
  //    public static final String PATH_TO_TRAJ_DATA = "/home/max/Documents/phd/ultimate_frisbee_data/uf_loop_interpolated.txt";
  //    public static final String PATH_TO_TRAJ_DATA = "/home/max/Documents/phd/flock_pigeon_data/homing_pigeons_2-4_sample.txt";
   //public static final String PATH_TO_TRAJ_DATA = "/home/max/Documents/phd/flock_pigeon_data/homing_pigeons_2_3_sample.txt";
  //  public static final String PATH_TO_TRAJ_DATA = "/home/max/Documents/phd/flock_pigeon_data/homing_pigeons_2_4_sample.txt";
    public static final String PATH_TO_TRAJ_DATA = "/home/max/Documents/phd/flock_pigeon_data/homing_pigeons_3_4_sample.txt";
   // public static final String PATH_TO_TRAJ_DATA = "/home/max/Documents/phd/caribou_data/caribou_g22_g24.txt";
    // TODO Add an interface to load data at first.
  //  public static final String PATH_TO_TRAJ_DATA = "data/zig_zac_data.txt";

    // Plots in the GUI
    private MatchingPlot matchingPlot;
    private DistancePlotPanel distancePlot;
    private DelaySpacePanel delaySpacePlot;
    private DelayPlotPanel delayPlot;
    
    // Settings from the UI
    private int threshold;
    private int translucentFocus;
    private double samplingRate;
    private boolean logScaled;
    
    // Data members
    private Trajectory trajectory1;
    private Trajectory trajectory2;
    private DelaySpace delaySpace;
    private Matching matching = null;
    private double epsilon;
    
    private final boolean isTriplet;
    private final PairInTriple pairInTriple;
    // is null when only 2 trajectories have been used.
    private Trajectory trajectory3 = null;
    
    // Trajectory selection.
    private Trajectory trajectA;
    private Trajectory trajectB;

    /**
     * Creates new form AnalyticsDelayUI
     */
    public AnalyticsDelayUI() {
        initComponents();
        this.logScaled = false;
        this.threshold = 1;
        this.samplingRate = 0.2;
        this.translucentFocus = 50;
        this.epsilon = 1.7;
        this.isTriplet = false;
        this.pairInTriple = PairInTriple.TRAJ_12;
        initTrajectories();
        setDelaySpace(DelaySpaceType.USUAL, DistanceNormFactory.EuclideanDistance);
        computeMatching();
        
        initSlider();
        initDelaySpace();
        initMatchingPlot();
        initDelayPlot();
        initFollowingPlot();
    }

    private void initTrajectories() {
        try {
            if (isTriplet) {
                readTripleTrajectories(PATH_TO_TRAJ_DATA);
                switch(pairInTriple) {
                    case TRAJ_12:
                        trajectA = trajectory1;
                        trajectB = trajectory2;
                        break;
                    case TRAJ_13:
                        trajectA = trajectory1;
                        trajectB = trajectory3;
                        break;
                    case TRAJ_23:
                        trajectA = trajectory2;
                        trajectB = trajectory3;
                        break;
                }
            } else {
                readTrajectories(PATH_TO_TRAJ_DATA);
                trajectA = trajectory1;
                trajectB = trajectory2;
            }

        } catch (Exception ex) {
            Logger.getLogger(AnalyticsDelayUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initSlider() {
        this.matchingSlider.setMinimum(0);
        this.matchingSlider.setMaximum(this.matching.i.length - 1);
    }

    private void initDelaySpace() {
        this.delaySpacePlot = new DelaySpacePanel(delaySpace, matching, threshold, samplingRate, logScaled);
        this.delaySpacePanel.add(this.delaySpacePlot);
    }

    private void initMatchingPlot() {
        this.matchingPlot = new MatchingPlot(matching, trajectA, trajectB, threshold, translucentFocus);
        this.trajectoryPlotPanel.add(matchingPlot);
    }

    private void initDelayPlot() {
        this.distancePlot = new DistancePlotPanel(matching, delaySpace);
        this.distancePanel.add(distancePlot);
    }

    private void initFollowingPlot() {
        this.delayPlot = new DelayPlotPanel(matching, threshold, 0.2);
        this.delayPanel.add(delayPlot);
    }
    
    private void readTrajectories(String filename) throws Exception {
        TrajectoryReader reader = TrajectoryReader.createTrajectoryReader(filename, true);
        this.trajectory1 = reader.getTrajectory1();
        this.trajectory2 = reader.getTrajectory2();
    }
    
    private void readTripleTrajectories(String filename) throws Exception {
        TripletTrajectoryReader reader = TripletTrajectoryReader.createTrajectoryReader(filename, true);
        this.trajectory1 = reader.getTrajectory1();
        this.trajectory2 = reader.getTrajectory2();
        this.trajectory3 = reader.getTrajectory3();
    }
    
    private void computeMatching() {
        if (isTriplet) {
            MatchingType matchingType = getMatchingType();
            switch(matchingType) {
                case FRECHET:
                    TripleFrechetMatching m = new TripleFrechetMatching(trajectory1, trajectory2, trajectory3);
                    List<IntTriple> tripleMatching = m.compute();
                    this.matching = Matching.fromTripletMatching(tripleMatching, pairInTriple);
                    break;
                case DTW:
                    TripleDTWMatching mDTW = new TripleDTWMatching(trajectory1, trajectory2, trajectory3);
                    List<IntTriple> tripleDTWMatching = mDTW.compute();
                    this.matching = Matching.fromTripletMatching(tripleDTWMatching, pairInTriple);
                    break;
                default:
                    throw new RuntimeException("");
            }
        } else {
            Experiment experiment = new Experiment(delaySpace);
            MatchingType matchingType = getMatchingType();
            this.matching = experiment.run(matchingType, epsilon);
        }
    }
    
    private void setDelaySpace(DelaySpaceType delaySpaceType, DistanceNorm currentDistance) {
        this.delaySpace = DelaySpace.createDelaySpace(trajectA, trajectB, delaySpaceType, currentDistance);
    }
    
    private void updateDelaySpace() {
        DelaySpaceType delaySpaceType = getDelaySpaceType();
        DistanceNorm distanceNorm = getDistanceNorm();
        setDelaySpace(delaySpaceType, distanceNorm);
    }
    
    private DelaySpaceType getDelaySpaceType() {
        int selectedIndex = delaySpaceComboBox.getSelectedIndex();
        switch (selectedIndex) {
            case 0:
                return DelaySpaceType.USUAL;
            case 1:
                return DelaySpaceType.DIRECTIONAL_DISTANCE;
            case 2:
                return DelaySpaceType.DYNAMIC_INTERACTION;
            case 3:
                return DelaySpaceType.DISPLACEMENT;
            case 4:
                return DelaySpaceType.HEADING;
            default:
                throw new RuntimeException("Invalid selection");       
        }
    }
    
    private MatchingType getMatchingType() {
        if (computationMethodComboBox == null) {
            // If not UI is not ready yet, use a frechet matching as a default.
            return MatchingType.FRECHET;
        }
        int selectedIndex = computationMethodComboBox.getSelectedIndex();
        switch(selectedIndex) {
            case 0:
                return MatchingType.FRECHET;
            case 1:
                return MatchingType.IDENTIY;
            case 2:
                return MatchingType.DTW;
            case 3:
                return MatchingType.EDR;
            default:
                throw new RuntimeException("Invalid selection for a matching type");
        }
    }
    
    private DistanceNorm getDistanceNorm() {
        int selectedIndex = distanceNormComboBox.getSelectedIndex();
        int lastElement = distanceNormComboBox.getItemCount() - 1;        
        if (selectedIndex == lastElement) {
            return DistanceNormFactory.LInfDistance;
        } else if (selectedIndex == 1) {
            return DistanceNormFactory.L1Distance;
        } else {
            return DistanceNormFactory.EuclideanDistance;
        }
    }

    // TODO Check whether this method plots and behaves correctly.
    private void updateAndRepaintPlots() {
        this.matchingSlider.setMaximum(this.matching.getLength() - 1);
        if (delayPlot != null) {
            delayPlot.updateMatching(matching, threshold, samplingRate);
            delayPlot.repaint();
        }
        if (distancePlot != null) {
            // FIXME this plot works the other not. Why?
            distancePlot.update(matching, delaySpace);
            distancePlot.repaint();
        }
        if (matchingPlot != null) {
            matchingPlot.updateMatching(matching, threshold, translucentFocus);
            matchingPlot.repaint();
        }
        if (delaySpacePlot != null) {
            delaySpacePlot.updateDelaySpace(delaySpace);
            delaySpacePlot.updateMatching(matching);
//            delaySpacePlot.updateMatching(matching);
//            delayPanel.repaint();
            delaySpacePlot.repaint();
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
        computationMethodComboBox = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        epsField = new javax.swing.JTextField();
        jSplitPane3 = new javax.swing.JSplitPane();
        trajectoryPlotPanel = new javax.swing.JPanel();
        jSplitPane4 = new javax.swing.JSplitPane();
        sliderPanel = new javax.swing.JPanel();
        matchingSlider = new javax.swing.JSlider();
        jSplitPane6 = new javax.swing.JSplitPane();
        distancePanel = new javax.swing.JPanel();
        delayPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Visualization of Delays in Trajectories");

        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        delaySpacePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Delay Space"));
        delaySpacePanel.setPreferredSize(new java.awt.Dimension(560, 420));
        delaySpacePanel.setLayout(new java.awt.BorderLayout());
        jSplitPane2.setBottomComponent(delaySpacePanel);

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
        samplingRateField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                samplingRateFieldPropertyChange(evt);
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

        delaySpaceComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Usual Distance", "Directional Distance", "Dynamic Interaction", "Displacement", "Heading" }));
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

        computationMethodComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Frechet Matching", "Identity Matching", "Dynamic Time Warping", "Edit Distance on Real" }));
        computationMethodComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                computationMethodComboBoxItemStateChanged(evt);
            }
        });
        computationMethodComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                computationMethodComboBoxActionPerformed(evt);
            }
        });

        jLabel6.setText("eps");

        epsField.setText("1.7");
        epsField.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                epsFieldCaretUpdate(evt);
            }
        });
        epsField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                epsFieldActionPerformed(evt);
            }
        });
        epsField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                epsFieldPropertyChange(evt);
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(settingsPanelLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(delaySpaceComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(settingsPanelLayout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(focusSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(jLabel6)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(epsField))
                        .addGroup(settingsPanelLayout.createSequentialGroup()
                            .addComponent(logScalingCheckBox)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(computationMethodComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
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
                    .addComponent(focusSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(epsField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(thresholdComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(thresholdSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(logScalingCheckBox)
                    .addComponent(computationMethodComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jSplitPane2.setLeftComponent(settingsPanel);

        jSplitPane1.setLeftComponent(jSplitPane2);

        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane3.setMinimumSize(new java.awt.Dimension(500, 433));

        trajectoryPlotPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Trajectory Plot"));
        trajectoryPlotPanel.setMinimumSize(new java.awt.Dimension(500, 400));
        trajectoryPlotPanel.setPreferredSize(new java.awt.Dimension(500, 400));
        trajectoryPlotPanel.setRequestFocusEnabled(false);
        trajectoryPlotPanel.setLayout(new java.awt.BorderLayout());
        jSplitPane3.setTopComponent(trajectoryPlotPanel);

        jSplitPane4.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        sliderPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Matching"));

        matchingSlider.setMajorTickSpacing(50);
        matchingSlider.setMinorTickSpacing(25);
        matchingSlider.setPaintLabels(true);
        matchingSlider.setPaintTicks(true);
        matchingSlider.setToolTipText("");
        matchingSlider.setValue(0);
        matchingSlider.setPreferredSize(new java.awt.Dimension(150, 400));
        matchingSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                matchingSliderStateChanged(evt);
            }
        });

        javax.swing.GroupLayout sliderPanelLayout = new javax.swing.GroupLayout(sliderPanel);
        sliderPanel.setLayout(sliderPanelLayout);
        sliderPanelLayout.setHorizontalGroup(
            sliderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(matchingSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
        );
        sliderPanelLayout.setVerticalGroup(
            sliderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(matchingSlider, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
        );

        jSplitPane4.setTopComponent(sliderPanel);

        jSplitPane6.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        distancePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Distance Plot"));
        distancePanel.setPreferredSize(new java.awt.Dimension(30, 170));
        distancePanel.setLayout(new java.awt.BorderLayout());
        jSplitPane6.setTopComponent(distancePanel);

        delayPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Delay Plot"));
        delayPanel.setPreferredSize(new java.awt.Dimension(30, 170));
        delayPanel.setLayout(new java.awt.BorderLayout());
        jSplitPane6.setBottomComponent(delayPanel);

        jSplitPane4.setBottomComponent(jSplitPane6);

        jSplitPane3.setRightComponent(jSplitPane4);

        jSplitPane1.setRightComponent(jSplitPane3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 874, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void matchingSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_matchingSliderStateChanged
        if (!matchingSlider.getValueIsAdjusting()) {
            // TODO updates the slider all plots correctly?
            int newValue = matchingSlider.getValue();
            int indexTrajA = matching.i[newValue];
            int indexTrajB = matching.j[newValue];
            EdgeCursor selection = new EdgeCursor(indexTrajA, indexTrajB, newValue);
            matchingSlider.setToolTipText(Integer.toString(newValue));
            if (distancePlot != null) {
                distancePlot.updateSelection(selection);
                distancePanel.repaint();
            }
            if (matchingPlot != null) {
                matchingPlot.updateSelection(selection);
                trajectoryPlotPanel.repaint();
            }
            if (delayPlot != null) {
                delayPlot.updateSelection(selection);
                delayPanel.repaint();
            }
            if (delaySpacePlot != null) {
                delaySpacePlot.updateSelection(selection);
                delaySpacePanel.repaint();
            }
        }
    }//GEN-LAST:event_matchingSliderStateChanged

    private void distanceNormComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_distanceNormComboBoxItemStateChanged
        updateDelaySpace();
        computeMatching();
        updateAndRepaintPlots();
    }//GEN-LAST:event_distanceNormComboBoxItemStateChanged

    private void delaySpaceComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_delaySpaceComboBoxItemStateChanged
        updateDelaySpace();
        computeMatching();
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
        if (logScalingCheckBox.isSelected()) {
            this.logScaled = true;
        } else {
            this.logScaled = false;
        }
        
        if (delaySpacePlot != null) {
            delaySpacePlot.setLogScaled(logScaled);
            delaySpacePlot.repaint();
        }
        
        if (distancePlot != null) {
            distancePlot.setLogScaled(logScaled);
            distancePlot.repaint();
        }
    }//GEN-LAST:event_logScalingCheckBoxItemStateChanged

    private void computationMethodComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_computationMethodComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_computationMethodComboBoxActionPerformed

    private void computationMethodComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_computationMethodComboBoxItemStateChanged
        updateDelaySpace();
        computeMatching();
        updateAndRepaintPlots();
    }//GEN-LAST:event_computationMethodComboBoxItemStateChanged

    private void epsFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_epsFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_epsFieldActionPerformed

    private void samplingRateFieldPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_samplingRateFieldPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_samplingRateFieldPropertyChange

    private void epsFieldPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_epsFieldPropertyChange

    }//GEN-LAST:event_epsFieldPropertyChange

    private void epsFieldCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_epsFieldCaretUpdate
        this.epsilon = Double.valueOf(epsField.getText());
        if (matching != null) {
            computeMatching();
            updateAndRepaintPlots();
        }
    }//GEN-LAST:event_epsFieldCaretUpdate

    
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
        // FIXME update the plots on the new sampling rate.
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
                AnalyticsDelayUI myAnalyticsUI = new AnalyticsDelayUI();
                myAnalyticsUI.setVisible(true);
                myAnalyticsUI.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox computationMethodComboBox;
    private javax.swing.JPanel delayPanel;
    private javax.swing.JComboBox delaySpaceComboBox;
    private javax.swing.JPanel delaySpacePanel;
    private javax.swing.JComboBox distanceNormComboBox;
    private javax.swing.JPanel distancePanel;
    private javax.swing.JTextField epsField;
    private javax.swing.JSpinner focusSpinner;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JSplitPane jSplitPane4;
    private javax.swing.JSplitPane jSplitPane6;
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
