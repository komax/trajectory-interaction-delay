% Load data for the bats from file.
[trajA, trajB, timeIndices] = loadBatData();

% Pad the 2D trajectories to 3D ones.
trajA = pad2DTrajectoryTo3D(trajA);
trajB = pad2DTrajectoryTo3D(trajB);

% Compute a Locally Correct Frechet Matchting.
lcfMatching = matching.discrete_lcfm(trajA,trajB);
% Plot the trjactories with its lcf matching.
visualise.plot_matching(trajA,trajB,lcfMatching);
