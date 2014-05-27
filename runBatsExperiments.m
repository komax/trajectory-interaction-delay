% Load data for the bats from file.
readData = 0;
if ~readData
    [trajA, trajB, timeIndices] = loadBatData();

    % Pad the 2D trajectories to 3D ones.
    trajA = pad2DTrajectoryTo3D(trajA);
    trajB = pad2DTrajectoryTo3D(trajB);
    readData = 1;
end

%typeDistanceTerrain = 'normal';
%typeDistanceTerrain = 'directionalDistance';
typeDistanceTerrain = 'dynamicIteraction';
chosenNorm = Inf;
experimentExtension = ['Norm', num2str(chosenNorm)];

% Compute the distance terrain.
switch typeDistanceTerrain
    case 'normal'
        distanceTerrain = matching.compute_distance_terrain(trajA,trajB,chosenNorm);
    case 'directionalDistance'
        distanceTerrain = matching.directionalDistanceTerrain(trajA,trajB,chosenNorm);
        experimentExtension = [experimentExtension, 'DirectionalDistance'];
    case 'dynamicIteraction'
        alpha = 2;
        distanceTerrain = matching.dynamicInteractionTerrain(trajA,trajB,chosenNorm,alpha);
        experimentExtension = [experimentExtension, 'DynamicIteraction'];
    otherwise
        error('Cannot handle this choice');
end
% Generate file names.
matchingName = ['batsMatching', experimentExtension, '.dump'];
delayPlotName = ['delaySpace', experimentExtension, '.png'];
% Plot the distance terrain.
%visualise.plotDistanceTerrain(distanceTerrain);

% Compute a Locally Correct Frechet Matchting.
lcfMatching = matching.discrete_lcfm(trajA,trajB,distanceTerrain);
% Save matching on disk.
matching.writeMatching(lcfMatching,matchingName);
% Plot the trajectories with its lcf matching.
visualise.plotMatching(lcfMatching);
% Plot free space with its matching.
delayPlot = visualise.plotMatchingInFreeSpace(distanceTerrain, lcfMatching);
saveas(delayPlot,delayPlotName);
