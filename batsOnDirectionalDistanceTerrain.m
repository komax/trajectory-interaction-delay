% Load data for the bats from file.
readData = 0;
if ~readData
    [trajA, trajB, timeIndices] = loadBatData();

    % Pad the 2D trajectories to 3D ones.
    trajA = pad2DTrajectoryTo3D(trajA);
    trajB = pad2DTrajectoryTo3D(trajB);
    readData = 1;
end

% Compute the directional distance terrain.
distanceTerrain = matching.directionalDistanceTerrain(trajA,trajB);
% Plot the distance terrain.
visualise.plotDistanceTerrain(distanceTerrain);

% Compute a Locally Correct Frechet Matchting.
lcfMatching = matching.discrete_lcfm(trajA,trajB,distanceTerrain);
% Save matching on disk.
matching.writeMatching(lcfMatching,'batsMatchingOnDirectionalDistance.dump');
% Plot the trajectories with its lcf matching.
visualise.plot_matching(trajA,trajB,lcfMatching);
% Plot free space with its matching.
visualise.plotMatchingInFreeSpace(distanceTerrain, lcfMatching);
