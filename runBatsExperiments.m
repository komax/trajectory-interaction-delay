% Load data for the bats from file.
readData = 0;
if ~readData
    [trajA, trajB, timeIndices] = loadBatData();

    % Pad the 2D trajectories to 3D ones.
    trajA = pad2DTrajectoryTo3D(trajA);
    trajB = pad2DTrajectoryTo3D(trajB);
    readData = 1;
end

% Compute the distance terrain.
distanceTerrain = matching.compute_distance_terrain(trajA,trajB);
% Plot the distance terrain.
visualise.plotDistanceTerrain(distanceTerrain);

% Compute a Locally Correct Frechet Matchting.
lcfMatching = matching.discrete_lcfm(trajA,trajB);
% Plot the trajectories with its lcf matching.
visualise.plot_matching(trajA,trajB,lcfMatching);
% Plot free space with its matching
visualise.plotMatchingInFreeSpace(distanceTerrain, lcfMatching);