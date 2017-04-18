# Build
1. Run
    `$ make (or $ make clean && make)`
2. To wipe out old class files, use
    `$ make clean`
(Or compile src directory with an IDE and put the class files into bin
directory)

# Run
The current pipeline is the following:

1. Matlab code reads trajectories into matrices
2. From the trajectories compute a distance terrain
    i. distance norm based
    ii. dynamic interaction (using heading and vector length)
    iii. directional distance (using a combination of heading and i.)
3. Compute a locally correct Frechet Matching by passing distance terrain
   and trajectories to java implementation
4. Store results as matching *.dump (serialized java) object and delay space as
*.png
5. Launch analytics GUI (java) using *.dump and *.png to visualize matchings

## Java-only
Launch analytics GUI by
   `$ java -cp bin visualization.AnalyticsDelayUI`
(If your IDE compiles your class files into a different directory replace bin
by that directory)

## Matlab
- Execute runBatsExperiment.m to conduct an experiment on the bats data
- You need to adjust the path (pathToBatData) to the data within loadBatData.m

# Package Structure
A short summary how the implementation is roughly structured
## java
- frechet: LocallyCorrectFrechet is the main class that returns a matching
  object. Details are in the other classes. Mainly used in matlab code (step
  3.)
- matlabconversion: MatchingReader is a helper class to load a serialized
  matching object within step 5.
- utils: Helper functions on matchings to compute different distance norms
  (interface to extend it as well), delays on a matching object.
- visualization: AnalyticsDelayUI.java is the main class to visualize
  matchings, delays, ... ColorMap is a simple implementation to use custom
  color mapping. All panels can be used within matlab by calling methods on
  VisualizerLauncher.java
  
## matlab
- project level: generic functions to load data, pad 2D trajectories to 3D
  and conduct experiments
- matching: discrete_lcfm.m returns a matching on the trajectories.
  compute<method>.m computes a terrain as a 2D matrix
  from two trajectories. A method can be heading, displacement or distance
  terrain. directionalDistance.m and dynamicInteraction.m combine heading
  information and a distance terrain or vector length respectively.
  writeMatching.m enables serializing the java matching object.
- visualise: plotMatchingInFreeSpace.m generates a plot using 8-color heated
  body color map for the distance terrain and plots the matching as well.
  plotDistanceTerrain.m plots the distance terrain with a full heated body
  color map. plotMatching.m wraps a call to the java visualization trajectory
  plot.

# Conduct New Experiments
Have look into runBatsExperiments.m:

1. Write your own data loading mechanism for the trajectories
2. Do not forget to padding 2D trajectories to 3D
3. Compute a distance terrain (for methods using heading: trim off last row and
column to avoid undefined results)
4. Compute a matching given the trajectories and a distance terrain
5. Visualize or store the matching.
