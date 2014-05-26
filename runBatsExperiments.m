% Load data for the bats from file.
readData = 0;
if ~readData
    [trajA, trajB, timeIndices] = loadBatData();

    % Pad the 2D trajectories to 3D ones.
    trajA = pad2DTrajectoryTo3D(trajA);
    trajB = pad2DTrajectoryTo3D(trajB);
    readData = 1;
end

typeDistanceTerrain = 'directionalDistance';

% Compute the distance terrain.
switch typeDistanceTerrain
    case 'normal'
        distanceTerrain = matching.compute_distance_terrain(trajA,trajB);
        matchingName = 'batsMatching.dump';
    case 'directionalDistance'
        distanceTerrain = matching.directionalDistanceTerrain(trajA,trajB);
        matchingName = 'batsMatchingdirectionalDistance.dump';
    case 'dynamicIteraction'
        alpha = 2;
        distanceTerrain = matching.dynamicInteractionTerrain(trajA,trajB,alpha);
        matchingName = 'batsMatchingDynamicIteraction.dump';
    otherwise
        error('Cannot handle this choice');
end
% Plot the distance terrain.
visualise.plotDistanceTerrain(distanceTerrain);

% Compute a Locally Correct Frechet Matchting.
lcfMatching = matching.discrete_lcfm(trajA,trajB,distanceTerrain);
% Save matching on disk.
matching.writeMatching(lcfMatching,matchingName);
% Plot the trajectories with its lcf matching.
visualise.plot_matching(trajA,trajB,lcfMatching);
% Plot free space with its matching.
visualise.plotMatchingInFreeSpace(distanceTerrain, lcfMatching);
