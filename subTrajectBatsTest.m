% Load data for the bats from file.
readData = 0;
if ~readData
    [trajA, trajB, timeIndices] = loadBatData();

    % Pad the 2D trajectories to 3D ones.
    trajA = pad2DTrajectoryTo3D(trajA);
    trajA = trajA(82:296, :);
    trajB = pad2DTrajectoryTo3D(trajB);
    trajB = trajB(66:296, :);
    readData = 1;
end

chosenNorm = 2;
experimentExtension = ['Norm', num2str(chosenNorm)];

% Compute the distance terrain.
distanceTerrain = matching.directionalDistanceTerrain(trajA,trajB,chosenNorm);
% Cut distance terrain and trajectories to ignore last row and column
distanceTerrain = distanceTerrain(1:end-1, 1:end-1);
trajA = trajA(1:end-1,:);
trajB = trajB(1:end-1,:);
experimentExtension = [experimentExtension, 'DirectionalDistance'];
% Generate file names.
matchingName = ['batsMatching_test296', experimentExtension, '.dump'];
delayPlotName = ['delaySpace_test296', experimentExtension, '.png'];
% Plot the distance terrain.
visualise.plotDistanceTerrain(distanceTerrain);

% Compute a Locally Correct Frechet Matchting.
lcfMatching = matching.discrete_lcfm(trajA,trajB,distanceTerrain);
% Save matching on disk.
matching.writeMatching(lcfMatching,matchingName);
% Plot the trajectories with its lcf matching.
visualise.plotMatching(lcfMatching);
% Plot free space with its matching.
delayPlot = visualise.plotMatchingInFreeSpace(distanceTerrain, lcfMatching);
saveas(delayPlot,delayPlotName);
