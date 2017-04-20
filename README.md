# Visual Analytics of Delays and Interaction
This visualization tool computes an alignment between two or three trajectories
and enables the user to explore such an alignment interactively.

The tool supports these alignment methods
 * Dynamic Time Warping
 * (discrete and locally correct) Fréchet Distance
 * Edit Distance on Real Sequences

# Build
Run
```
$ ant
```

(Or compile and build the project within the IDE Netbeans)

# Data Format
You can find example files in `data` folder
## A Pair of Trajectories
```
         t     bx     by     ax     ay
```
 * ```t``` is the current time stamp
 * ```bx by``` x- and y-coordinates from the first trajectory
 * ```ax ay``` x- and y-coordinates from the second trajectory

## A Triplet of Trajectories
```
     t     cx     cy     bx     by     ax     ay
```
 * ```t``` is the current time stamp
 * ```cx cy``` x- and y-coordinates from the first trajectory
 * ```bx by``` x- and y-coordinates from the second trajectory
 * ```ax ay``` x- and y-coordinates from the third trajectory

# Run
1. Launch the application by
```
    $ java -jar dist/TrajectoryDelay.jar
```
2. Load a data set into the application by choosing `File` in the menu bar
   and select:
   * `Open Trajectory Data (Pairwise)` to compute and show an alignment between two trajectories
   * `Open Triplet Trajectory Data (Pair 1 2)` to compute an alignment among three trajectories and show their projections onto the delay space from trajectory 1 and 2
   * `Open Triplet Trajectory Data (Pair 1 3)` and `Open Triplet Trajectory Data (Pair 2 3)` similarly for the other pairs in the triplet
3. Use the slider to browse the alignment

# Components of the Visual Analytics tool
1. Settings to adjust the visualization and/the computation of the matching
2. Delay Space: the pairwise distances as a grid from the start of the trajectories `(1, 1)`, bottome-left corner, to the end `(n, m)`, top-right corner, as a heated body color map
3. Trajectory Plot: The visualization of the trajectories and their corresponding
alignment over time
4. Matching slider: to navigate and browse the matching/alignment
5. Distance Plot: Distances from the matching over time
6. Delay Plot: Time differences, delay, from the matching over time
