function runPigeonsExperiment(typeDistanceTerrain,chosenNorm)
% Load data for the pigeons from file.
readData = 0;
if ~readData
    [trajA, trajB, timeIndices] = loadPigeonData();

    % Pad the 2D trajectories to 3D ones.
    trajA = pad2DTrajectoryTo3D(trajA);
    trajB = pad2DTrajectoryTo3D(trajB);
    readData = 1;
end

%typeDistanceTerrain = 'normal';
%typeDistanceTerrain = 'directionalDistance';
%typeDistanceTerrain = 'dynamicInteraction';
%typeDistanceTerrain = 'dynamicDistance';
%chosenNorm = 2;
experimentExtension = ['Norm', num2str(chosenNorm)];

% Compute the distance terrain.
switch typeDistanceTerrain
    case 'normal'
        distanceTerrain = matching.compute_distance_terrain(trajA,trajB,chosenNorm);
    case 'directionalDistance'
        distanceTerrain = matching.directionalDistanceTerrain(trajA,trajB,chosenNorm);
        experimentExtension = [experimentExtension, 'DirectionalDistance'];
    case 'dynamicInteraction'
        alpha = 1;
        distanceTerrain = matching.dynamicInteractionTerrain(trajA,trajB,chosenNorm,alpha);
        experimentExtension = [experimentExtension, 'DynamicInteraction'];
    case 'dynamicDistance'
        alpha = 1;
        distanceTerrain = matching.dynamicDistanceTerrain(trajA,trajB,chosenNorm,alpha);
        experimentExtension = [experimentExtension, 'DynamicDistance'];
    case 'heading'
        distanceTerrain = matching.headingTerrain(trajA,trajB,chosenNorm);
        experimentExtension = [experimentExtension, 'Heading'];
    otherwise
        error('Cannot handle this choice');
end

% Trim off last point from distance terrain if the terrain is direction
% sensitive.
if ~strcmp('normal',typeDistanceTerrain)
    [trajA, trajB, distanceTerrain] = trimOffLastPoint(trajA,trajB,distanceTerrain);
end

% Generate file names.
pathToResults = 'results/pigeons/';
matchingName = [pathToResults, 'matching', experimentExtension, '.dump'];
delayPlotName = [pathToResults, 'delaySpace', experimentExtension, '.png'];
logScaleDelayPlotName = [pathToResults, 'delaySpace', experimentExtension, 'logScale.png'];
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
logScaleDelayPlot = visualise.plotMatchingInFreeSpace(logScaleDelaySpace(distanceTerrain), lcfMatching);
saveas(logScaleDelayPlot,logScaleDelayPlotName);
